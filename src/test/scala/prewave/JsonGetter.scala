package prewave

import io.circe.Decoder
import io.circe.parser.decode

import scala.io.Source
import scala.util.Using

object JsonGetter {
  def get[T: Decoder](filePath: String): Seq[T] = {
    val maybeDecodedJson = for {
      jsonString <- Using(Source.fromFile(filePath))(_.mkString)
      decoded <- decode[Seq[T]](jsonString).toTry
    } yield decoded
    maybeDecodedJson.get
  }
}
