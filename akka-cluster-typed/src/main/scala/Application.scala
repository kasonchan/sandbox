import akka.actor.typed._
import akka.cluster.Cluster

import scala.concurrent.ExecutionContextExecutor

/**
  * @author kasonchan
  * @since 2020-09
  */
sealed trait Buzz
case object Activate extends Buzz with CborSerializable
case object Timeout extends Buzz with CborSerializable
case object Fatal extends Exception("Exception") with Buzz with CborSerializable

sealed trait Job extends Buzz
case class New(j: Int, replyTo: ActorRef[Buzz])
    extends Job
    with CborSerializable
case class Unprocessed(j: Int, replyTo: ActorRef[Buzz])
    extends Job
    with CborSerializable
case class Processed(j: Int, by: ActorRef[Buzz], to: ActorRef[Buzz])
    extends Job
    with CborSerializable

case class Notification(startFrom: ActorRef[Buzz], message: String)
    extends Job
    with CborSerializable

case object Service {
  implicit private val system: ActorSystem[Buzz] =
    ActorSystem(System(), "service")
  val cluster: Cluster = Cluster(system)
  implicit val ec: ExecutionContextExecutor = system.executionContext

  def start: ActorSystem[Buzz] = system
  system.log.info("{}", cluster.getSelfRoles.toArray.mkString(", "))
}

sealed trait CborSerializable

object Application {
  def main(args: Array[String]): Unit = {
    val service = Service.start
    service.systemActorOf(Guardian.apply, "guardian")
  }
}
