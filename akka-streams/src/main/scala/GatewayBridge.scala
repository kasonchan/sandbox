import akka.Done
import akka.actor.{Actor, ActorLogging, ReceiveTimeout}

import scala.concurrent.duration._

/**
  * @author kasonchan
  * @since 2018-03
  */
class GatewayBridge extends Actor with ActorLogging {

  context.setReceiveTimeout(1.second)

  override def receive = {
    case Message(i) =>
      log.info(i.toString)
      sender ! Done
    case ReceiveTimeout =>
      log.warning(ReceiveTimeout.toString)
    case m => println(m)
  }

}
