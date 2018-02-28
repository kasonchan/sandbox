import com.typesafe.sbt.packager.docker._

name := "sandbox-docker"

version := "0.0.1"

scalaVersion := "2.12.4"

enablePlugins(DockerPlugin, JavaAppPackaging)

mainClass in Compile := Some("HelloWorld")

dockerCommands := Seq(
  Cmd("FROM", "openjdk:latest"),
  Cmd("WORKDIR", "/workspace"),
  Cmd("ADD", ".", "/workspace"),
  ExecCmd("RUN", "ls", "-laRth", "."),
  ExecCmd("CMD", "java", "-cp", "opt/docker/lib/sandbox-docker.sandbox-docker-0.0.1.jar:opt/docker/lib/org.scala-lang.scala-library-2.12.4.jar", "HelloWorld")
)
