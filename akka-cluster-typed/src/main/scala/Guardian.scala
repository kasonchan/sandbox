import akka.actor.typed._
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.scaladsl.{
  Behaviors,
  PoolRouter,
  Routers,
  TimerScheduler
}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since 2020-09
  */
case object Guardian {
  private case object WorkTimerKey
  private case object NotificationTimerKey

  def apply: Behavior[Buzz] = Behaviors.withTimers(timers => starting(timers))

  private def starting(timers: TimerScheduler[Buzz]): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      // Activate
      timers.startSingleTimer(Activate, 0.seconds)

      // Drone group
      val pool: PoolRouter[Buzz] = Routers
        .pool(1)(
          // Restart drones if they fail
          Behaviors
            .supervise(Drone.apply)
            .onFailure[Exception](SupervisorStrategy.restart))
        .withRouteeProps(routeeProps = DispatcherSelector.blocking)
      val router: ActorRef[Buzz] = context.spawn(pool, "drone-pool")

      // Notification
      val notification: ActorRef[Topic.Command[Buzz]] =
        context.spawnAnonymous(Topic[Buzz]("notification"))

      ConfigFactory
        .load()
        .getString("role") match {
        case "SUB" =>
          context.log.info(
            s"${context.system.address.system} subscribing to topic......")
          notification ! Topic.Subscribe(context.self)
      }

      Behaviors.receiveMessage[Buzz] {
        case Activate =>
          ready(router, 0, notification, timers)
        case buzz =>
          Behaviors.same
      }
    }

  private def ready(router: ActorRef[Buzz],
                    workCount: Int,
                    notification: ActorRef[Topic.Command[Buzz]],
                    timers: TimerScheduler[Buzz]): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      // Timers
      timers.startTimerWithFixedDelay(WorkTimerKey,
                                      New(workCount, context.self),
                                      1.second)

      // Simulate jobs
      if (workCount > 50) timers.cancel(WorkTimerKey)

      Behaviors
        .receiveMessage[Buzz] {
          case n: New =>
            router ! n
            context.log.debug("[READY] {} {}", workCount, n)

            ConfigFactory
              .load()
              .getString("role") match {
              case "PUB" =>
                context.log.info(
                  s"${context.system.address.system} publishing topic......")
                notification ! Topic.Publish(
                  Notification(context.self,
                               s"${context.system.address}: notification"))
            }

            ready(router, workCount + 1, notification, timers)
          case p: Processed =>
            context.log.info("[READY] {} {}", workCount, p)
            ready(router, workCount + 1, notification, timers)
          case u: Unprocessed =>
            context.log.debug("[READY] {} {}", workCount, u)
            router ! u
            ready(router, workCount, notification, timers)
          case buzz =>
            context.log.warn("[READY] {}", buzz)
            ready(router, workCount, notification, timers)
        }
        .receiveSignal {
          case (_, signal) if signal == PreRestart || signal == PostStop =>
            Behaviors.stopped
        }
    }
}
