import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{Behaviors, Routers, TimerScheduler}
import akka.actor.typed._

import scala.concurrent.duration._

import akka.actor.typed.pubsub.Topic

/**
  * @author kasonchan
  * @since 2020-09
  */
case object Drone {
  private val groupServiceKey = ServiceKey[Buzz]("buzzes")

  def apply: Behavior[Buzz] = Behaviors.withTimers(timers => starting(timers))

  private def starting(timers: TimerScheduler[Buzz]): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      // Activate
      timers.startSingleTimer(Activate, 0.seconds)

      // Worker group
      val group = Routers.group(groupServiceKey).withRandomRouting
      // Restart work if it fails
      val worker = context.spawnAnonymous(
        Behaviors
          .supervise(Worker.apply)
          .onFailure[Exception](SupervisorStrategy.restart))
      context.system.receptionist ! Receptionist.Register(groupServiceKey,
                                                          worker)
      val router: ActorRef[Buzz] = context.spawn(group, "worker-group")

      Behaviors.receiveMessage[Buzz] {
        case Activate =>
          context.log.info("[STARTING]")
          ready(router, timers)
        case buzz =>
          Behaviors.same
      }
    }

  private def ready(router: ActorRef[Buzz],
                    timers: TimerScheduler[Buzz]): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      Behaviors
        .receiveMessage[Buzz] {
          case n: New =>
            router ! New(n.j, context.self)
            context.log.debug("[READY] {}", n)
            ready(router, timers)
          case p: Processed =>
            context.log.info("[READY] {}", p)
            ready(router, timers)
          case u: Unprocessed =>
            context.log.debug("[READY] {}", u)
            router ! Unprocessed(u.j, context.self)
            ready(router, timers)
          case buzz =>
            context.log.warn("[READY] {}", buzz)
            ready(router, timers)
        }
        .receiveSignal {
          case (_, signal) if signal == PreRestart || signal == PostStop =>
            Behaviors.stopped
        }
    }
}
