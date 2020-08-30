name := "akka-grpc-server"
version := "0.1"
scalaVersion := "2.13.3"
val AkkaVersion = "2.5.26"
val AkkaHttpVersion = "10.1.12"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % "Test",
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % "Test"
)
enablePlugins(AkkaGrpcPlugin)
