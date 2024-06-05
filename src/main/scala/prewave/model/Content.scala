package prewave.model

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class Content(text: String,
                   `type`: String, // TODO Enum ?
                   language: String) // TODO Enum ?

object Content {
  implicit val decoder: Decoder[Content] = deriveDecoder[Content]
  implicit val ContentType: ObjectType[Unit, Content] = deriveObjectType[Unit, Content]()
}
