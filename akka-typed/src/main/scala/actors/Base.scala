package actors

import actions.Action
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

/**
  * @author kasonchan
  * @since 2018-09
  */
object Base {
  final def NEW: Behavior[Action] = Behaviors.setup { ctx ⇒
    val state = ctx.spawn(Status.NEW, "state")
    Behaviors.receiveMessage { action ⇒
      state ! action
      Behaviors.same
    }
  }
}
