import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

/**
  * @author kasonchan
  * @since 2018-03
  */
case object Meh extends Exception("Meh")

object Demo {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("system")

    val supervisor = system.actorOf(Props[Supervisor], "gateway")

    supervisor ! "Before PoisonPill"

    supervisor ! "p"

    Thread.sleep(200)
    supervisor ! "After PoisonPill"

    Thread.sleep(200)
    supervisor ! "Before Kill"

    supervisor ! "k"

    Thread.sleep(200)
    supervisor ! "After Kill"

    supervisor ! "Before Stop"
    supervisor ! "s"

    Thread.sleep(200)
    supervisor ! "After Stop"

    supervisor ! "Before PoisonPill"

    supervisor ! "p"

    Thread.sleep(200)
    supervisor ! "After PoisonPill"

    supervisor ! "Before Meh"

    supervisor ! Meh

    Thread.sleep(200)
    supervisor ! "After Meh"

    StdIn.readLine()
    system.terminate()

  }

}
