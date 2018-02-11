import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

/**
  * @author kasonchan
  * @since 2018-02-11
  */
object Server {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system").
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val (host, port) = ("127.0.0.1", 19999)

    val bindingFuture =
      Http().bindAndHandle(routes(system), host, port)

    bindingFuture.failed.foreach { ex =>
      system.log.error(s"Failed to bind to $host:$port.")
    }

    system.log.info(s"Server online at $host:$port.")
  }

  final def routes(system: ActorSystem): Route =
    path("") {
      get {
        system.log.info("OK")
        complete(StatusCodes.OK.intValue, "Config")
      }
    }

}
