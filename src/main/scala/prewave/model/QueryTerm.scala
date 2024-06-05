package prewave.model

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class QueryTerm(id: Long,
                     target: Long,
                     text: String,
                     language: String, // TODO Enum ?
                     keepOrder: Boolean)

object QueryTerm {
  implicit val queryTermDecoder: Decoder[QueryTerm] = deriveDecoder[QueryTerm]
  implicit val QueryTermType: ObjectType[Unit, QueryTerm] = deriveObjectType[Unit, QueryTerm]()
}
