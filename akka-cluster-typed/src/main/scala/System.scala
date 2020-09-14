import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

/**
  * @author kasonchan
  * @since 2020-09
  */
case object System {
  def apply(): Behavior[Buzz] = init()

  private def init(): Behavior[Buzz] =
    Behaviors.setup[Buzz] { context =>
      // Do nothing
      Behaviors.empty
    }
}
