package com.sandbox.initials

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.{ActorMaterializer, Materializer}
import com.sandbox.actors.Echo

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * @author kasonchan
  * @since Jan-2017
  */
object Actors {

  implicit val system: ActorSystem = ActorSystem("actorsystem")

  implicit val mat: Materializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    implicit val echo: ActorRef = system.actorOf(Props[Echo], "echo")

    echo ! "Hi"

    Await.ready(system.whenTerminated, Duration.Inf)

  }

}
