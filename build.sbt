
lazy val akkaVersion = "2.4.9-RC2"

lazy val commonSettings = Seq(
  version := "0.1.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings", "-encoding", "UTF-8", "-target:jvm-1.8"),
  scalatest,
  akka,
  akka_fe,
  resolvers += Resolver.typesafeRepo("snapshots"),
  resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

lazy val akka = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion
)

lazy val akka_fe = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.8.0",
  "com.lihaoyi" %% "upickle" % "0.4.1"
)

lazy val scalatest = libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

lazy val root =
  project.in(file("."))
    .settings(commonSettings: _*)

