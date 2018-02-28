name := "sandbox-pem"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on
  "org.bouncycastle" % "bcprov-jdk15on" % "1.59",
  "com.github.pathikrit" %% "better-files" % "3.4.0"
)
