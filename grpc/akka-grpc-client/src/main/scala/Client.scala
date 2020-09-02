import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import com.typesafe.config.ConfigFactory
import helloworld.{Request, Service, ServiceClient}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2020-08
  */
object Client extends App {
  val conf = ConfigFactory.load()
  implicit val system: ActorSystem = ActorSystem("helloworld", conf)
  implicit val ec: ExecutionContext = system.dispatcher

  val clientSettings = GrpcClientSettings.fromConfig("helloworld.Service")
  val client: Service = ServiceClient(clientSettings)

  system.log.info("Performing request")
  val response = client.message(Request("Client requests"))
  response.onComplete {
    case Success(msg) =>
      system.log.info(s"Server: ${msg.message}")
    case Failure(e) =>
      system.log.warning(s"Server: $e")
  }
}
