package com.sandbox.initials

import scala.sys.process.{Process, ProcessLogger}
import scala.util.{Failure, Success, Try}

/**
  * @author kasonchan
  * @since Dec-2016
  */
case class Command(command: String, option: String, file: String) {
  val processLogger: ProcessLogger = ProcessLogger(
    (o: String) => println(o)
  )

  def run(): String = {
    Try {
      // Does not support multiple commands
      Process(s"$command -$option $file",
              new java.io.File(Process("pwd").!!.trim())) !! processLogger
    } match {
      case Success(v) => v
      case Failure(e) => e.getMessage
    }
  }

  def runOn(server: String): String = {
    Try {
      // Multiple commands need to be separate by ;
      Process(s"ssh $server $command -$option $file") !! processLogger
    } match {
      case Success(v) => v
      case Failure(e) => e.getMessage
    }
  }
}

object Processes {

  def main(args: Array[String]): Unit = {
    println(l)
    println(Command("ls", "lath", "").run())
    println(Command("ls", "lath", "build.sbt").run())
    println(Command("ls", "lath", "test").run())
    println(Command("w", "", "").run())
    println(Command("ls", "9", "").run())
    println(Process(Seq("bash", "-c", "ls ; pwd")).!!)
  }

  def exec(command: String,
           currentDirectory: String = Process("pwd").!!.trim()): String = {
    Process(command, new java.io.File(currentDirectory)).!!.trim()
  }

  def w(): String = {
    exec("w")
  }

  def pwd(): String = {
    exec("pwd")
  }

  def l(): String = {
    exec("ls -lath")
  }

}
