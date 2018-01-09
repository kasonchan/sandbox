import com.typesafe.sbt.packager.docker._

name := "sandbox-docker"

version := "0.0.1"

scalaVersion := "2.12.4"

enablePlugins(DockerPlugin, JavaAppPackaging)

dockerCommands := Seq(
  Cmd("FROM", "openjdk:latest"),
  Cmd("WORKDIR", "/workspace"),
  Cmd("ADD", ".", "/workspace"),
  ExecCmd("CMD", "ls", "-laRth", "."),
  ExecCmd("CMD", "java", "-jar", "./opt/docker/lib/sandbox-docker.sandbox-docker-0.0.1.jar")
)
