name := "recommendation_service"

organization := "com.recommendation_service"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-agent" % "2.3.6",
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-remote" % "2.3.6",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test",
  "org.reactivemongo" %% "reactivemongo" % "0.12.5",
  "org.slf4j" % "slf4j-log4j12" % "1.7.10",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.pauldijou" %% "jwt-json4s-native" % "0.14.0",
  "org.json4s" %% "json4s-native" % "3.5.2",
  "org.json4s" %% "json4s-core" % "3.5.2",
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.2.0",
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  "org.reactivemongo" %% "reactivemongo" % "0.12.5",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

mappings in (Compile, packageBin) ~= { _.filterNot { case (_, name) =>
  Seq("application.conf").contains(name)
}}