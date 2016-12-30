name := "scala-chess-scalafx"

version := "1.0"

scalaVersion := "2.11.8"

lazy val scalaChessApi = RootProject(file("../scala-chess-api"))

lazy val `scala_chess_scalafx` = (project in file(".")).dependsOn(scalaChessApi)

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.4.12"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true
    