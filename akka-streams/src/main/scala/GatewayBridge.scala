import akka.actor.Actor

/**
  * @author kasonchan
  * @since 2018-03
  */
class GatewayBridge extends Actor {

  val db = ???

  override def receive = {
    case m => println(m)
  }

}
