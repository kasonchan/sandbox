name := "akka-actors"

version := "0.0.1"

scalaVersion := "2.12.3"

// http://alvinalexander.com/scala/sbt-how-to-control-managed-dependencies-versions

lazy val scalaTestVersion = "3.0.+"
lazy val akkaVersion = "2.5.+"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)

// https://github.com/scoverage/sbt-scoverage

coverageEnabled := true

coverageHighlighting := true

// http://www.scalastyle.org/sbt.html

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle
  .in(Compile)
  .toTask("")
  .value

(compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle
