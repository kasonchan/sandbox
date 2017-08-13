package actors

import akka.actor.SupervisorStrategy.{Restart, Resume, Stop}
import akka.actor.{Actor, OneForOneStrategy, Props}
import akka.routing.{ActorRefRoutee, RandomRoutingLogic, Router}
import messages.{RestartException, ResumeException, StopException}

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since May-2017
  */
class SuperSlave extends Actor with akka.actor.ActorLogging {

  private val routeesNumber = 10

  private val maxNrOfRetries = 10

  override def preStart(): Unit = {
    log.info("Prestart")
  }

  override def postStop(): Unit = {
    log.info("Poststop")
  }

  //noinspection ScalaStyle
  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = maxNrOfRetries, withinTimeRange = 1.minute) {
      case _: RestartException => Restart
      case _: ResumeException  => Resume
      case _: StopException    => Stop
    }

  private val router: Router = {
    val routees = Vector.fill(routeesNumber) {
      val r = context.actorOf(Props[Slave])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RandomRoutingLogic(), routees)
  }

  def receive: PartialFunction[Any, Unit] = {
    case any @ _ =>
      log.info(s"${sender().path} ${any.toString}")
      router.route(any, self)
  }

}
