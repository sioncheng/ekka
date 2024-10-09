name := "ekka-srv"

version := "1.0"
scalaVersion := "2.13.14"

val akkaVersion = "2.9.3"
lazy val akkaGrpcVersion = sys.props.getOrElse("akka-grpc.version", "2.4.3")

resolvers += "Akka library repository".at("https://repo.akka.io/maven")
enablePlugins(AkkaGrpcPlugin)

// This is the default - both client and server
akkaGrpcGeneratedSources := Seq(AkkaGrpc.Client, AkkaGrpc.Server)

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "com.typesafe.akka" %% "akka-pki" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion,


  "ch.qos.logback" % "logback-classic" % "1.2.13",

  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)