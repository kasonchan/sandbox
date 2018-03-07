import akka.{Done, NotUsed}
import akka.actor.{ActorSystem, Props}
import akka.stream.scaladsl.{Flow, Sink, Source, SourceQueueWithComplete}
import akka.stream.{
  ActorMaterializer,
  Materializer,
  OverflowStrategy,
  ThrottleMode
}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-03
  */
case class Message(x: Int)

case object Init

case object Ack

case object Complete

object Demo {

  def filterEven(x: Int) = x % 2 == 0

  def bulkExecution(s: Seq[Int]): Future[Unit] =
    Future(s.foreach(e => print(s"$e ")))

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: Materializer = ActorMaterializer()

    implicit val timout = Timeout(2.minutes)

    val source = Source(1 to 30).map(e => Message(e))

    val gb = system.actorOf(Props(classOf[GatewayBridge]), "gb")

    val gbs = Sink.actorRefWithAck[Seq[Int]](gb, Init, Ack, Complete)

    val ws = Flow[Message]
      .collect {
        case m: Message => m.x
      }
      .throttle(1, 300.milliseconds, 1, ThrottleMode.shaping)
      .mapAsync(1) { i =>
        (gb ? Message(i)).mapTo[Done]
        Future(i)
      }
      .groupedWithin(10, 1.second)
      .alsoTo(gbs)
      .mapAsync(10)(bulkExecution)

    source.via(ws).runForeach(e => print(s"$e "))

    val queue: SourceQueueWithComplete[Int] = Source
      .queue[Int](1, OverflowStrategy.backpressure)
      .to(Sink.foreach(println))
      .run

    source.mapAsync(1)(m => queue.offer(m.x)).runWith(Sink.ignore)

    StdIn.readLine()
    system.terminate()
  }

}
