lazy val commonSettings = Seq (
  organization  := "io.rockneurotiko",
  version       := "0.1-SNAPSHOT",
  scalaVersion  := "2.11.7",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:higherKinds",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials")
    // licenses      := ("Apache2", new java.net.URL("http://www.apache.org/licenses/LICENSE-2.0.txt")) :: Nil
)

lazy val scalagram = project.in(file(".")).
  settings(commonSettings: _*).
  settings(name := "scalagram",
          resolvers ++= Seq(
            "spray repo" at "http://repo.spray.io/"),
          libraryDependencies ++= {
            val akkaVersion = "2.4.2"
            val sprayVersion = "1.3.2"
            Seq(
              "com.typesafe.akka" %% "akka-actor" % akkaVersion,
              "io.spray" %% "spray-can" % sprayVersion,
              "io.spray" %% "spray-routing" % sprayVersion,
              "io.spray" %% "spray-json" % sprayVersion,
              "io.spray" %% "spray-client" % sprayVersion,
              "org.scalatest" %% "scalatest" % "3.0.0" % "test")
          }).
  aggregate(scalagrammacros)

lazy val examples = project.in(file("examples/simple")).
  settings(commonSettings: _*).
  settings(libraryDependencies ++= Seq(
    "io.rockneurotiko" %% "scalagram" % "0.1-SNAPSHOT",
    "io.rockneurotiko" %% "scalagram-macros" % "0.1-SNAPSHOT",
    "org.scalatest" %% "scalatest" % "2.2.4" % "test")).
  aggregate(scalagrammacros, scalagram)

lazy val scalagrammacros = project.in(file("macros")).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.11.7",
      "org.scala-lang" % "scala-compiler" % "2.11.7" % "provided"))

// name := """scalagram"""

// organization := "io.rockneurotiko"
// version := "0.1-SNAPSHOT"

// scalaVersion := "2.11.7"

// resolvers ++= Seq(
//   "spray repo" at "http://repo.spray.io/")

// libraryDependencies ++= {
//   val akkaVersion = "2.4.2"
//   val sprayVersion = "1.3.2"
//   Seq(
//     "com.typesafe.akka" %% "akka-actor" % akkaVersion,
//     "io.spray" %% "spray-can" % sprayVersion,
//     "io.spray" %% "spray-routing" % sprayVersion,
//     "io.spray" %% "spray-json" % sprayVersion,
//     "io.spray" %% "spray-client" % sprayVersion,
//     "org.scalatest" %% "scalatest" % "2.2.4" % "test")
// }

// scalacOptions := Seq(
//   "-unchecked",
//   "-deprecation",
//   "-encoding", "utf8",
//   "-feature",
//   "-language:higherKinds",
//   "-language:postfixOps",
//   "-language:implicitConversions",
//   "-language:existentials")

fork in run := true
