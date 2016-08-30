name := """scalagram"""

organization := "io.rockneurotiko"
version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/")

libraryDependencies ++= {
  val akkaVersion = "2.4.2"
  val sprayVersion = "1.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "io.spray" %% "spray-json" % sprayVersion,
    "io.spray" %% "spray-client" % sprayVersion,
    "org.scalatest" %% "scalatest" % "2.2.4" % "test")
}

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-encoding", "utf8",
  "-feature",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-language:existentials")

fork in run := true
