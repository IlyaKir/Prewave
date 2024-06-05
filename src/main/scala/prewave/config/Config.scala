package prewave.config

import prewave.logger.Logger
import pureconfig._
import pureconfig.generic.ProductHint
import pureconfig.generic.auto._

object Config {
  // https://pureconfig.github.io/docs/overriding-behavior-for-case-classes.html#field-mappings
  implicit private def hint[A]: ProductHint[A] = ProductHint[A](ConfigFieldMapping(CamelCase, CamelCase))

  case class AppConfig(name: String,
                       graphqlServer: GraphQLServerConfig,
                       prewaveServer: PrewaveServerConfig)

  case class GraphQLServerConfig(host: String,
                                 port: Int)

  case class PrewaveServerConfig(key: String,
                                 testQueryTermAddress: String,
                                 testAlertsAddress: String)

  val config: AppConfig = ConfigSource.default.load[AppConfig] match {
    case Right(config) => config
    case Left(error) =>
      Logger.logger.error(s"Failed to load configuration: ${error.toList.map(_.description).mkString("\n")}")
      throw new Exception()
  }
}