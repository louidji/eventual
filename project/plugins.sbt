// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// The Sonatype snapshots repository
resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.6")
// 2.3.6

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.7.0-SNAPSHOT")

// Use the Scalariform plugin to reformat the code
//addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

// Use sbt web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.0.0")

//addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.4")
addSbtPlugin("com.typesafe.sbt" % "sbt-uglify" % "1.0.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.4")
