import play.PlayScala

scalaVersion := "2.11.2"


name := "eventual"

version := "1.0-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
	// ReactiveMongo dependencies
	"org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
	// ReactiveMongo Play plugin dependencies
	"org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
	//
	"com.mohiva" %% "play-silhouette" % "2.0-SNAPSHOT",
	//"com.mohiva" %% "play-silhouette" % "1.0",
	"net.codingwell" %% "scala-guice" % "4.0.0-beta4",
	//
	cache,
	//
	filters
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala)
