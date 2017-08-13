/**
  * @author kasonchan
  * @since Aug-2017
  */
import actors.Slave
import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestActorRef, TestKit}
import messages._
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class SlaveTest(_system: ActorSystem)
    extends TestKit(_system)
    with ImplicitSender
    with Matchers
    with FlatSpecLike
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("akkaTest"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A slave" should "be able to receive Ping, log Ping and return nothing" in {
    val slave = TestActorRef(Props[Slave])

    EventFilter.info(source = Ping.toString) intercept {
      slave ! Ping
      expectNoMsg()
    }
  }

  it should "be able to receive Msg and return Msg with appended Received msg: " in {
    val slave = TestActorRef(Props[Slave])
    val info = "Hello World!"

    EventFilter.info(source = Msg(info).toString) intercept {
      slave ! Msg(info)
      expectMsg(Msg(s"Received msg: $info"))
    }
  }

  it should "be able to receive a string" in {
    val slave = TestActorRef(Props[Slave])

    EventFilter.info(source = "test") intercept {
      slave ! "test"
    }
  }

  it should "be able to receive a undefined message and return I don't understand" in {
    val slave = TestActorRef(Props[Slave])
    val weirdMsg = 1

    EventFilter.info(source = weirdMsg.toString) intercept {
      slave ! weirdMsg
      expectMsg(Msg(s"I don't understand"))
    }
  }

  it should "be able to restart" in {
    val slave = TestActorRef(Props[Slave])

    intercept[RestartException] {
      slave.receive(RestartException)
    }
    expectNoMsg
  }

  it should "be able to resume" in {
    val slave = TestActorRef(Props[Slave])

    intercept[ResumeException] {
      slave.receive(ResumeException)
    }
    EventFilter.info(source = "Resumed") intercept {
      slave ! "Resumed"
    }
    expectNoMsg
  }

  it should "be able to stop" in {
    val slave = TestActorRef(Props[Slave])

    intercept[StopException] {
      slave.receive(StopException)
    }
    EventFilter.info(source = "Stopped") intercept {
      slave ! "Stopped"
    }
    expectNoMsg()
  }

}
