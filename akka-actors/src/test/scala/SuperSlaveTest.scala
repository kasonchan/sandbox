/**
  * @author kasonchan
  * @since Aug-2017
  */
import actors.SuperSlave
import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit}
import messages.Ping
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class SuperSlaveTest(_system: ActorSystem)
    extends TestKit(_system)
    with ImplicitSender
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("akkaTest"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A SuperSlave" should "be able to receive any messages" in {
    val superSlave = TestActorRef(Props[SuperSlave])

    EventFilter.info(source = Ping.toString) intercept {
      superSlave ! Ping
      expectNoMsg()
    }
  }

}
