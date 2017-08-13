name := "gatling"

version := "0.0.1"

scalaVersion := "2.12.3"

lazy val gatlingVersion = "2.2.5"

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle
  .in(Compile)
  .toTask("")
  .value

(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value
