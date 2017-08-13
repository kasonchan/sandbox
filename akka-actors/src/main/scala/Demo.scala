/**
  * @author kasonchan
  * @since Aug-2017
  */
import actors.{Slave, SuperSlave}
import akka.actor.{ActorSystem, Inbox, Props}
import messages._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since May-2017
  */
object Demo {

  val system = ActorSystem("akka")

  val slave = system.actorOf(Props[Slave], "slave")

  val superSlave = system.actorOf(Props[SuperSlave], "superslave")

  // Create an "actor-in-a-box"
  val inbox = Inbox.create(system)

  def main(args: Array[String]): Unit = {
    slave ! Ping

    inbox.send(slave, Msg("Hello World!"))
    val slaveReply = inbox.receive(5.seconds)
    print(s"$slaveReply\n")

    superSlave ! "Msg1"

    superSlave ! ResumeException

    superSlave ! "Msg after resuming"

    superSlave ! "Msg3"

    superSlave ! "Msg4"

    superSlave ! RestartException

    superSlave ! "Msg after restarting"

    superSlave ! "Msg6"

    superSlave ! StopException

    superSlave ! "Msg after stopping"

    superSlave ! "Msg8"

    system.terminate()
    Await.result(system.terminate, Duration.Inf)
  }

}
