import akka.actor.ActorSystem
import akka.grpc.scaladsl.ServerReflection
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.{Http, HttpConnectionContext}
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.ConfigFactory
import helloworld.grpc.{ServiceHandler => HWServiceHandler, ServiceImpl}
import akka.grpc.scaladsl.ServiceHandler

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
    implicit val sys: ActorSystem = system
    implicit val mat: Materializer = ActorMaterializer()
    implicit val ec: ExecutionContext = sys.dispatcher

    val service: PartialFunction[HttpRequest, Future[HttpResponse]] =
      HWServiceHandler.partial(new ServiceImpl())

    val services: HttpRequest => Future[HttpResponse] =
      ServiceHandler.concatOrNotFound(service)

    // Bind service handler servers to localhost:8080
    val binding = Http().bindAndHandleAsync(services,
                                            interface = "127.0.0.1",
                                            port = 8080,
                                            connectionContext =
                                              HttpConnectionContext())

    // report successful binding
    binding.foreach { binding =>
      println(s"gRPC server bound to: ${binding.localAddress}")
    }

    binding
  }
}
