import sbt.enablePlugins

import scala.sys.process._
import scala.util.Try

name := "gatling"
version := "0.0.1"
scalaVersion := "2.12.4"

lazy val gatlingVersion = "2.3.0"

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-target:jvm-1.8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:postfixOps"
)

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test",
  "io.gatling"            % "gatling-test-framework"    % gatlingVersion % "test"
)

parallelExecution in Test := false

enablePlugins(GatlingPlugin)

/**
  * Set up ScalaStyle
  */
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")
compileScalastyle := scalastyle.in(Compile).toTask("").value
(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value

/**
  * Set up download Gatling bundle
  */
def dlGB: Try[Int] = Try {
  lazy val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/" +
    s"$gatlingVersion/gatling-charts-highcharts-bundle-$gatlingVersion-bundle.zip"
  s"curl -O $link".!
}

/**
  * Unzip Gatling bundle
  */
def unzipGB: Try[Int] = Try {
  s"unzip gatling-charts-highcharts-bundle-$gatlingVersion-bundle.zip".!
}

/**
  * List unzipped Gatling bundle
  */
def lsGB: Try[Seq[String]] = Try { s"ls gatling-charts-highcharts-bundle-$gatlingVersion".!!.split("\n") }

/**
  * Move Gatling bundle to current directory
  */
def mvGB: Try[Unit] = Try {
  lsGB.getOrElse(Seq())
    .foreach(dir => s"mv -fv gatling-charts-highcharts-bundle-$gatlingVersion/$dir .".!)

  s"ls user-files/simulations".!!.split("\n").foreach(dir => s"rm -rv user-files/simulations/$dir".!)
}

/**
  * Copy simulation from test to user-files
  */
def cpSims: Try[Unit] = {
  Try {
    s"ls src/test/resources/data".!!
      .split("\n")
      .foreach(dir => s"cp -frv src/test/resources/data/$dir user-files/data".!)
  }

  Try {
    s"ls src/test/resources/bodies".!!
      .split("\n")
      .foreach(dir => s"cp -frv src/test/resources/bodies/$dir user-files/bodies".!)
  }

  Try {
    s"ls src/test/scala".!!
      .split("\n")
      .foreach(dir => s"cp -frv src/test/scala/$dir user-files/simulations".!)
  }
}

/**
  * Clean Gatling bundle
  */
lazy val cleanGB = taskKey[Unit]("cleanGB")

cleanGB := {
  Try {
    List("bin", "conf", "lib", "results", "user-files", "LICENSE").foreach(dir => s"rm -rv $dir".!)
  }

  Try {
    s"rm -rv ../${name.value}.zip".!
  }

  Try {
    (s"ls ." #| s"grep gatling-charts-highcharts-bundle-$gatlingVersion").!!
      .split("\n")
      .foreach(file => s"rm -rv $file".!)
  }
}

/**
  * Zip the current directory to parent directory
  */
def zipMyself: Try[Int] = Try {
  val currentPath = System.getProperty("user.dir")
  lazy val basename: String = s"basename $currentPath".!!
  s"zip -rv ../$basename.zip ." !
}

/**
  * Package Gatling bundle
  */
lazy val packageGB = taskKey[Unit]("packageGB")

packageGB := {
  dlGB
  unzipGB
  mvGB
  cpSims
  zipMyself
}
