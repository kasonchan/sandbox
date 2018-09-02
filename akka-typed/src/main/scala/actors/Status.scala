package actors

import actions._
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

/**
  * @author kasonchan
  * @since 2018-09-02
  */
object Status {
  final def NEW: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[NEW]")
        Behaviors.same
      case Create =>
        ctx.log.info("[NEW] -Create> [LOADING]")
        LOADING
      case _ =>
        ctx.log.info("[NEW]: {}", action.toString)
        Behaviors.same
    }
  }

  private final def LOADING: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[LOADING]")
        Behaviors.same
      case Start =>
        ctx.log.info("[LOADING] -Start> [READY]")
        READY
      case _ =>
        ctx.log.info("[LOADING]: {}", action.toString)
        Behaviors.same
    }
  }

  private final def READY: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[READY]")
      case _ =>
        ctx.log.info("[READY]: {}", action.toString)
    }
    RUNNING
  }

  private final def RUNNING: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[RUNNING]")
        Behaviors.same
      case Stop =>
        ctx.log.info("[RUNNING] -STOP> [WAITING]")
        WAITING
      case _ =>
        ctx.log.info("[RUNNING]: {}", action.toString)
        Behaviors.same
    }
  }

  private final def WAITING: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[WAITING]")
      case _ =>
        ctx.log.info("[WAITING]: {}", action.toString)
    }
    TERMINATED
  }

  private final def TERMINATED: Behavior[Action] = Behaviors.receive { (ctx, action) =>
    action match {
      case State =>
        ctx.log.info("[TERMINATED]")
      case _ =>
        ctx.log.info("[TERMINATED]: {}", action.toString)
    }
    Behaviors.stopped
  }
}
