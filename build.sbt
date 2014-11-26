import play.PlayScala

scalaVersion := "2.11.1"

name := "eventual"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	// ReactiveMongo dependencies
	"org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
	// ReactiveMongo Play plugin dependencies
	"org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
	cache,
	filters
)     

lazy val root = (project in file(".")).enablePlugins(PlayScala)
