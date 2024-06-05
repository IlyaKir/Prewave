package prewave.graphql

import io.circe.{Decoder, Json}
import io.circe.generic.semiauto.deriveDecoder

case class GraphQLBody(query: String = "",
                       operationName: Option[String] = None,
                       variables: Option[Json] = None,
                       extensions: Option[Json] = None)

object GraphQLBody {
  implicit val decoder: Decoder[GraphQLBody] = deriveDecoder[GraphQLBody]
}