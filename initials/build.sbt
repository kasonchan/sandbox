name := "initials"

version := "0.0.1"

scalaVersion := "2.12.3"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

lazy val akkaVersion = "2.5.+"
lazy val scalaLoggingVersion = "3.5.+"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.+",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
)
