name := "ssl-config"
version := "0.1"
scalaVersion := "2.13.2"

lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion    = "2.6.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "ch.qos.logback"    % "logback-classic" % "1.2.3"
)
