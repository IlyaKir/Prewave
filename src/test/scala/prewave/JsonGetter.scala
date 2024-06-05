package prewave

import io.circe.Decoder
import io.circe.parser.decode
import prewave.logger.Logger.logger

import scala.io.Source
import scala.util.{Failure, Success, Using}

object JsonGetter {
  def get[T: Decoder](filePath: String): Seq[T] = {
    (for {
      jsonString <- Using(Source.fromFile(filePath))(_.mkString)
      decoded <- decode[Seq[T]](jsonString).toTry
    } yield decoded) match {
      case Failure(exception) =>
        logger.error(s"Failed to read from file ${filePath}\n${exception.getMessage}")
        Seq.empty
      case Success(seq) =>
        logger.info(s"Successfully read from $filePath")
        seq
    }
  }
}
