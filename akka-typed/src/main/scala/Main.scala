import actions.{Create, Start, State, Stop}
import actors.Base
import akka.actor.typed.ActorSystem

import scala.io.StdIn.readLine

/**
  * @author kasonchan
  * @since 2018-09
  */
object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Base.NEW, "system")

    system ! State
    system ! Create
    system ! State
    system ! Start
    system ! State
    system ! State
    system ! Stop
    system ! State
    system ! State

    readLine()
    system.terminate()
  }

}
