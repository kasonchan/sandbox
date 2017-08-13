import org.scalajs.sbtplugin.ScalaJSPlugin.AutoImport.persistLauncher

enablePlugins(ScalaJSPlugin)

lazy val name = "scalajs"

val baseSettings = Seq(
  scalacOptions in(Compile, console) := compilerOptions
)

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1",
  scalaVersion := "2.12.2"
)

lazy val compilerOptions = Seq(
  "-encoding", "UTF-8",
  "-feature"
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val allSettings = baseSettings ++ buildSettings ++ noPublishSettings

lazy val scalajs = project.in(file("."))
  .settings(moduleName := name)
  .settings(allSettings: _*)
  .settings(skip in packageJSDependencies := false)
  .settings(persistLauncher := true)
  .settings(
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
      "org.plotly-scala" %%% "plotly-render" % "0.3.1"
    ))
  .settings(
    jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",
    // Include Runtime DOM for rbt ~run to work
    jsDependencies += RuntimeDOM
  )
