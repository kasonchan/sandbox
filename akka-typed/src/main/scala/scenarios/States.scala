package scenarios

import actions._
import actors.Base
import akka.actor.typed.ActorSystem

import scala.io.StdIn.readLine

/**
 * @author kasonchan
 * @since 2020-05
 */
case object States {
  def init = {
    implicit val system: ActorSystem[Action] = ActorSystem(Base.NEW, "system")

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
