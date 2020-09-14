import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{Behaviors, TimerScheduler}

/**
  * @author kasonchan
  * @since 2020-09
  */
case object Worker {
  def apply: Behavior[Buzz] = Behaviors.withTimers(timers => ready(timers, 0))

  private def ready(timers: TimerScheduler[Buzz],
                    workCount: Int): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      if (workCount >= 2) {
        idle(timers, workCount)
      } else {
        Behaviors.receiveMessage {
          case n: New =>
            context.log.info("[READY] {} {}", workCount, n)
            n.replyTo ! Processed(n.j, context.self, n.replyTo)
            ready(timers, workCount + 1)
          case u: Unprocessed =>
            context.log.info("[READY] {} {}", workCount, u)
            u.replyTo ! Processed(u.j, context.self, u.replyTo)
            ready(timers, workCount + 1)
          case buzz: Buzz =>
            context.log.warn("[READY] {} {}", workCount, buzz.toString)
            ready(timers, workCount)
        }
      }
    }

  private def idle(timers: TimerScheduler[Buzz],
                   workCount: Int): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      if (workCount >= 4) {
        context.log.error("[IDLE]->[FATAL] {}", workCount)
        fatal(timers, workCount)
      } else {
        Behaviors.receiveMessage {
          case Fatal =>
            context.log.error("[IDLE]->[FATAL] {}", workCount)
            fatal(timers, workCount)
          case n: New =>
            context.log.warn("[IDLE] {} {}", workCount, "No new job please!")
            n.replyTo ! Unprocessed(n.j, n.replyTo)
            idle(timers, workCount + 1)
          case u: Unprocessed =>
            context.log.warn("[IDLE] {} {}", workCount, "No new job please!")
            u.replyTo ! Unprocessed(u.j, u.replyTo)
            idle(timers, workCount + 1)
          case buzz: Buzz =>
            context.log.warn("[IDLE] {}", workCount)
            idle(timers, workCount)
        }
      }
    }

  private def fatal(timers: TimerScheduler[Buzz],
                    workCount: Int): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      Behaviors.receiveMessage {
        case n: New =>
          n.replyTo ! Unprocessed(n.j, n.replyTo)
          throw Fatal
          Behaviors.stopped
        case u: Unprocessed =>
          u.replyTo ! Unprocessed(u.j, u.replyTo)
          throw Fatal
          Behaviors.stopped
        case buzz: Buzz =>
          throw Fatal
          Behaviors.stopped
      }
    }
}
