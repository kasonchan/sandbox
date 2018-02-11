import NativePackagerHelper._

lazy val root = (project in file("."))
  .settings(
    name := "sandbox-configs",
    version := "0.0.1",
    scalaVersion := "2.12.4",
    maintainer in Linux := "Kason Chan <kasonl.chan@gmail.com>",
    packageSummary in Linux := "Sandbox Configs",
    packageDescription in Rpm := "Sandbox Configs",
    rpmBrpJavaRepackJars := true,
    rpmRelease := "1",
    rpmVendor := "kasonchan",
    rpmGroup := Some("sandbox-configs"),
    rpmUrl := None,
    rpmLicense := Some("Apache v2"),
    rpmChangelogFile := None,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"
    ),
    mappings in Universal ++= directory("scripts")
  )
  .enablePlugins(JavaAppPackaging, SystemdPlugin, RpmPlugin, UniversalPlugin)
