import akka.actor.SupervisorStrategy.{Restart, Stop}
import akka.actor.{
  Actor,
  ActorLogging,
  ActorRef,
  Kill,
  OneForOneStrategy,
  PoisonPill,
  Props
}
import akka.pattern.{Backoff, BackoffSupervisor}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since 2018-03-02
  */
class Supervisor extends Actor with ActorLogging {

  val maxNrOfRetries = 5

  val backOffOnStopSupervisor: Props = BackoffSupervisor.props(
    Backoff
      .onStop(
        Props(classOf[Minion]),
        childName = "minion",
        minBackoff = 1.millisecond,
        maxBackoff = 1.milliseconds,
        randomFactor = 0
      )
      .withSupervisorStrategy(
        OneForOneStrategy(maxNrOfRetries = maxNrOfRetries,
                          withinTimeRange = 2.millisecond) {
          case _ => Restart
        }
      )
      .withReplyWhileStopped(log.warning("stopping...")))

  val minion = context.system.actorOf(backOffOnStopSupervisor, "supervisor")
  context.watch(minion)

  override def preStart(): Unit = {
    log.warning(s"preStart")
  }

  override def postStop(): Unit = {
    log.warning(s"postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.warning(s"preRestart with $reason $message")
  }

  override def postRestart(reason: Throwable): Unit = {
    log.warning(s"postRestart with $reason")
  }

  override def receive: Receive = {
    case k @ "k" =>
      log.info(s"received Kill")
      context
        .actorSelection("../supervisor/minion") ! Kill
    case p @ "p" =>
      log.info(s"received PoisonPill")
      context
        .actorSelection("../supervisor/minion") ! PoisonPill
    case s @ "s" =>
      log.info(s"received Stop")
      context
        .actorSelection("../supervisor/minion")
        .resolveOne(1 minute)
        .onComplete {
          case Success(a: ActorRef) =>
            context.stop(a)
          case Failure(ex) =>
        }
    case Meh =>
      log.info(s"received Exception")
      context
        .actorSelection("../supervisor/minion") ! Meh
    case m =>
      log.info(s"received $m")
      minion ! m

  }

}
