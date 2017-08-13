package actors

import akka.actor.Actor
import messages._

/**
  * @author kasonchan
  * @since May-2017
  */
class Slave extends Actor with akka.actor.ActorLogging {

  private val sleepTime = 500

  override def preStart(): Unit = {
    log.info("Prestart")
  }

  override def postStop(): Unit = {
    log.info("Poststop")
  }

  def receive: PartialFunction[Any, Unit] = {
    case Ping =>
      log.info(s"Ping")
      Thread.sleep(sleepTime)
    case Msg(info: String) =>
      log.info(s"$info")
      sender ! Msg(s"Received msg: $info")
    case RestartException =>
      log.error(s"Throw Restart Exception")
      throw new RestartException
      Thread.sleep(sleepTime)
    case ResumeException =>
      log.error(s"Throw Resume Exception")
      throw new ResumeException
      Thread.sleep(sleepTime)
    case StopException =>
      log.error(s"Throw Stop Exception")
      throw new StopException
      Thread.sleep(sleepTime)
    case msg: String =>
      log.info(s"${sender().path}: $msg")
      Thread.sleep(sleepTime)
    case any @ _ =>
      log.info(any.toString)
      sender ! Msg(s"I don't understand")
  }

}
