import com.typesafe.sbt.web.SbtWeb
import play.PlayScala
import com.typesafe.sbt.web.SbtWeb.autoImport._
import com.typesafe.sbt.less.Import.LessKeys
import com.typesafe.sbt.web.Import.Assets
import sbt._

scalaVersion := "2.11.2"


name := "eventual"

version := "1.0-SNAPSHOT"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

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
	"org.webjars" %% "webjars-play" % "2.3.0-2",
	//
	"org.webjars" % "bootstrap" % "3.3.1",
	//
	"org.webjars" % "angularjs" % "1.3.4-1",
	//
	"org.webjars" % "jquery" % "2.1.1",
	//
	//	"org.webjars" % "less-node" % "2.1.0",
	//
	cache,
	//
	filters
)




LessKeys.compress in Assets := true

includeFilter in(Assets, LessKeys.less) := "*.less"

excludeFilter in(Assets, LessKeys.less) := "_*.less"


pipelineStages := Seq(uglify, digest, gzip)
//pipelineStages := Seq(rjs, digest, gzip)





