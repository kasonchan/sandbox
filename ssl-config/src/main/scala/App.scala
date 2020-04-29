import java.io.InputStream
import java.security.{KeyStore, SecureRandom}

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.{ConnectionContext, Http, HttpsConnectionContext}
import com.typesafe.config.ConfigFactory
import javax.net.ssl._

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2020-04
  */
object App {
  private val routes: Route = pathEndOrSingleSlash {
    get {
      complete {
        "ssl-config"
      }
    }
  }

  def startHttpServer(routes: Route,
                      system: ActorSystem[_],
                      https: HttpsConnectionContext): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    implicit val classicSystem: akka.actor.ActorSystem = system.toClassic
    import system.executionContext

    Http().setDefaultServerHttpContext(https)
    val futureBinding =
      Http().bindAndHandle(routes,
                           "localhost",
                           11010,
                           connectionContext = https)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at https://{}:{}/",
                        address.getHostString,
                        address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
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
    val https: HttpsConnectionContext = ConnectionContext.https(sslContext)

    val rootBehavior = Behaviors.setup[Nothing] { context =>
      startHttpServer(routes, context.system, https)
      Behaviors.empty
    }

    val system = ActorSystem[Nothing](rootBehavior, "system")
  }
}
