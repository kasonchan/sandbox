package helloworld

import akka.stream.Materializer

import scala.concurrent.Future

/**
  * @author kasonchan
  * @since 2020-08
  */
class ServiceImpl(implicit mat: Materializer) extends Service {

  override def message(in: Request): Future[Response] = {
    println(s"Client: ${in.message}")
    val response: String = """Server responds"""
    Future.successful(Response(response))
  }

}
