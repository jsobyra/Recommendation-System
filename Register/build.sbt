name := "first-sample"

version := "1.0-SNAPSHOT"

updateOptions := updateOptions.value.withCachedResolution(true)

libraryDependencies += "net.sf.barcode4j" % "barcode4j" % "2.0"
libraryDependencies ++= Seq(
  cache,
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3",
  "com.typesafe.akka" %% "akka-agent" % "2.3.6",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-remote" % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test"
)

scalaVersion := "2.11.8"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

