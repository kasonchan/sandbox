name := "akka-clustering"

version := "0.1"

scalaVersion := "2.12.4"

val akkaVersion = "2.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion
)

scalacOptions in Compile ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlog-reflective-calls",
    "-Xlint"
)

javacOptions in Compile ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation"
)

