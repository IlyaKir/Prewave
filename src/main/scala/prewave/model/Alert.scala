package prewave.model

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import sangria.macros.derive.deriveObjectType
import sangria.schema.ObjectType

case class Alert(id: String,
                 contents: Seq[Content],
                 date: String, // TODO Instant ?
                 inputType: String) // TODO Enum ?

object Alert {
  implicit val alertDecoder: Decoder[Alert] = deriveDecoder[Alert]
  implicit val AlertType: ObjectType[Unit, Alert] = deriveObjectType[Unit, Alert]()
}