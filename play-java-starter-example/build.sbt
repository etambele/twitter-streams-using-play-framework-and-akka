name := """play-java-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.196"

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-deprecation", "-Xlint")

libraryDependencies += jcache

libraryDependencies += "org.jsr107.ri" % "cache-annotations-ri-guice" % "1.0.0"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test

libraryDependencies += ws

libraryDependencies += "org.webjars" %% "webjars-play" % "2.6.2"
libraryDependencies += "org.webjars" % "bootstrap" % "2.3.2"
libraryDependencies += "org.webjars" % "flot" % "0.8.3"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.8.0" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "3.0.0" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "1.8.5" % Test

