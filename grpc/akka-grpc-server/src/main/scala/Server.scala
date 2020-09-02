import java.security.cert.{Certificate, CertificateFactory}
import java.security.{KeyStore, SecureRandom}

import akka.actor.ActorSystem
import akka.grpc.scaladsl.ServiceHandler
import akka.http.scaladsl.ConnectionContext.httpsServer
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{Http, HttpsConnectionContext}
import akka.pki.pem.{DERPrivateKeyLoader, PEMDecoder}
import com.typesafe.config.ConfigFactory
import helloworld.{Service, ServiceImpl, ServiceHandler => HWServiceHandler}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

/**
  * @author kasonchan
  * @since 2020-08
  */
object Server extends App {
  val conf = ConfigFactory.load()
  val system = ActorSystem("helloworld", conf)
  val server = new Server(system)
  server.run()
}

class Server(system: ActorSystem) {
  def run(): Future[Http.ServerBinding] = {
    // Configure HTTPs
    val privateKeyString = Source.fromResource("certs/server.key.pem").mkString
    val privateKey =
      DERPrivateKeyLoader.load(PEMDecoder.decode(privateKeyString))

    val certificateFactory = CertificateFactory.getInstance("X.509")
    val certificate = certificateFactory.generateCertificate(
      classOf[Service].getResourceAsStream("/certs/server.cert.pem")
    )

    val keyStore: KeyStore = KeyStore.getInstance("PKCS12")
    keyStore.load(null)
    keyStore.setKeyEntry(
      "private",
      privateKey,
      new Array[Char](0),
      Array[Certificate](certificate)
    )

    val keyManagerFactory: KeyManagerFactory =
      KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(keyStore, null)

    val trustManagerFactory: TrustManagerFactory =
      TrustManagerFactory.getInstance("SunX509")
    trustManagerFactory.init(keyStore)

    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.getKeyManagers,
                    trustManagerFactory.getTrustManagers,
                    new SecureRandom)
    val https: HttpsConnectionContext = httpsServer(sslContext)

    implicit val sys: ActorSystem = system
    implicit val ec: ExecutionContext = sys.dispatcher

    // PartialFunction allow multiple services concatenated
    val service: PartialFunction[HttpRequest, Future[HttpResponse]] =
      HWServiceHandler.partial(new ServiceImpl())
    val services: HttpRequest => Future[HttpResponse] =
      ServiceHandler.concatOrNotFound(service)

    val binding = Http()
      .newServerAt(interface = "localhost", port = 8080)
      .enableHttps(https)
      .bind(services)
      .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 3.minutes))

    // Report successful binding
    binding.foreach { binding =>
      system.log.info(s"gRPC server bound to ${binding.localAddress}")
    }

    binding
  }
}
