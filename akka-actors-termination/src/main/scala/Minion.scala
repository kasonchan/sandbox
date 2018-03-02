import akka.actor.{Actor, ActorLogging}

/**
  * @author kasonchan
  * @since 2018-03-02
  */
class Minion extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.warning(s"prestart")
    super.preStart()
  }

  override def postStop(): Unit = {
    log.warning(s"poststop")
    super.postStop()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.warning(s"preRestart called with $reason $message")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.warning(s"postRestart called with $reason")
    super.postRestart(reason)
  }

  override def receive: Receive = {
    case Meh =>
      log.warning("received Exception")
      throw Meh
    case m => log.info(s"received $m")
  }

}
