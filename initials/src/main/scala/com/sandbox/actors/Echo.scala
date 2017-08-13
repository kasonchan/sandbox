package com.sandbox.actors

import akka.actor.{ Actor, ActorLogging }

/**
 * @author kasonchan
 * @since Jan-2017
 */
class Echo extends Actor with ActorLogging {

  def receive: PartialFunction[Any, Unit] = {
    case s: String =>
      log.info(s)
      sender() ! self.path.name + " " + s
      sender() ! self.path.name + " " + s
      sender() ! self.path.name + " " + s
      context.stop(self)
    case x =>
      log.info(x.toString)
      sender() ! self.path.name + " " + x
  }

}
