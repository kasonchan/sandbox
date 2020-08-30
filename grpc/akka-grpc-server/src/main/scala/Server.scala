import java.io.InputStream
import java.security.{KeyStore, SecureRandom}

import akka.actor.ActorSystem
import akka.grpc.scaladsl.ServiceHandler
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{
  ConnectionContext,
  Http,
  HttpConnectionContext,
  HttpsConnectionContext
}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import helloworld.grpc.{ServiceImpl, ServiceHandler => HWServiceHandler}
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import scala.concurrent.{ExecutionContext, Future}

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
    val password: Array[Char] =
      ConfigFactory.load("password.conf").getString("password").toCharArray
    val ks: KeyStore = KeyStore.getInstance("PKCS12")
    val keystore: InputStream =
      getClass.getClassLoader.getResourceAsStream("keystore.pkcs12")

    require(keystore != null, "Keystore required!")
    ks.load(keystore, password)

    val keyManagerFactory: KeyManagerFactory =
      KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(ks, password)

    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(ks)

    val sslContext: SSLContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagerFactory.getKeyManagers,
                    tmf.getTrustManagers,
                    new SecureRandom)
    val https: HttpsConnectionContext =
      ConnectionContext.https(sslContext)

    implicit val sys: ActorSystem = system
    implicit val mat: Materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = sys.dispatcher

    val service: PartialFunction[HttpRequest, Future[HttpResponse]] =
      HWServiceHandler.partial(new ServiceImpl())

    val services: HttpRequest => Future[HttpResponse] =
      ServiceHandler.concatOrNotFound(service)

    Http().setDefaultServerHttpContext(https)
    val binding = Http().bindAndHandleAsync(services,
                                            interface = "127.0.0.1",
                                            port = 8080,
                                            connectionContext = https)

    // report successful binding
    binding.foreach { binding =>
      system.log.info(s"gRPC server bound to: ${binding.localAddress}")
    }

    binding
  }
}
