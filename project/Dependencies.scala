import sbt._

object Dependencies {
  private val circeVersion = "0.14.7"
  private val sttpVersion = "4.0.0-M16"
  private val logbackVersion = "1.5.6"
  private val sangriaVersion = "4.1.0"
  private val pekkoVersion = "1.0.1"
  private val pureConfigVersion = "0.17.6"
  private val scalaTestVersion = "3.2.18"

  private lazy val circeDeps = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )

  private lazy val sttpDeps = Seq(
    "com.softwaremill.sttp.client4" %% "core" % sttpVersion,
    "com.softwaremill.sttp.client4" %% "async-http-client-backend" % sttpVersion,
    "com.softwaremill.sttp.client4" %% "json-common" % sttpVersion
  )

  private lazy val sangriaDeps = Seq(
    "org.sangria-graphql" %% "sangria" % sangriaVersion,
    "org.sangria-graphql" %% "sangria-circe" % "1.3.2"
  )

  private lazy val pekkoDeps = Seq(
    "org.apache.pekko" %% "pekko-http" % pekkoVersion,
    "org.apache.pekko" %% "pekko-actor" % pekkoVersion,
    "org.apache.pekko" %% "pekko-stream" % pekkoVersion
  )

  private lazy val loggerDeps = Seq(
    // depends on slf4j-api
    "ch.qos.logback" % "logback-classic" % logbackVersion
  )

  private lazy val configDeps = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
  )

  private lazy val testDeps = Seq(
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  )

  lazy val dependencies: Seq[ModuleID] = circeDeps ++ sttpDeps ++ loggerDeps ++
    sangriaDeps ++ pekkoDeps ++ configDeps ++ testDeps
}