import akka.actor.{ActorSystem, Props}
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.{ActorMaterializer, Materializer, ThrottleMode}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-03
  */
case class Message(x: Int)

object Demo {

  def filterEven(x: Int) = x % 2 == 0

  def bulkExecution(s: Seq[Int]) = Future(s.foreach(e => print(s"$e ")))

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    val source = Source(1 to 30).map(e => Message(e))

    val gb = system.actorOf(Props(classOf[GatewayBridge]), "gb")

    val ws = Flow[Message]
      .collect {
        case m: Message =>
          m.x
      }
      .throttle(1, 300.milliseconds, 1, ThrottleMode.shaping)
      .groupedWithin(10, 1.second)
      .mapAsync(10)(bulkExecution)

    source.via(ws).runForeach(e => print(s"$e "))

    StdIn.readLine()
    system.terminate()
  }

}
