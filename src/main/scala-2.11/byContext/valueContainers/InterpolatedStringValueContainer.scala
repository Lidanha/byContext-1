package byContext.valueContainers

import byContext._
import byContext.score.valueContainers.SingleValueContainer
import com.typesafe.scalalogging.StrictLogging
import scala.util.{Failure, Success, Try}

case class Substitute(stringToReplace:String, container:SingleValueContainer)
class InterpolatedStringValueMarkerConverter(data:Map[String,Any]) extends DataSetItemConverter with MapExtensions with StrictLogging{
  val re = """<<.*>>""".r

  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,marker:InterpolatedStringValueMarker) =>

      val subs = buildSubsitutes(marker)
      new InterpolatedStringValueContainer(marker.value, subs)
  }

  private def buildSubsitutes(marker: InterpolatedStringValueMarker): Seq[Substitute] = {
    re
      .findAllMatchIn(marker.value)
      .map { m =>
        val path = m.source.subSequence(m.start + 2, m.end - 2).toString
        val stringToReplace = m.source.subSequence(m.start, m.end).toString
        Substitute(stringToReplace, findContainer(path))
      }
      .toSeq
  }
  private def findContainer(path:String):SingleValueContainer = {
    data.findByPath(path) match {
      case container:SingleValueContainer=>container
      case value:String => new SingleValueContainer {
        override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
      }
      case notsupported =>
        throw new RuntimeException(s"unsupported ref for string interpolation: ${notsupported} only ${classOf[SingleValueContainer].getName} is supported")
    }
  }
}
trait InterpolatedStringValueMarker{
  val value:String
}
class InterpolatedStringValueContainer(value:String, substitutes:Seq[Substitute])
  extends SingleValueContainer with StrictLogging{

  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    Try{
      interpolate(ctx)
    } match {
      case Success(interpolated)=> Right(interpolated)
      case Failure(t) => Left(StringInterpolationFailed(t.getMessage))
    }
  }

  def interpolate(ctx: QueryContext): String = {
    logger.debug(s"iterating substitutes for value: $value")

    substitutes
      .map { sub =>
        sub.container.get(ctx) match {
          case Right(v: String) =>
            (sub, v)
          case unsupported =>
            logger.warn(s"found unsupported value for interpolation: ${unsupported.toString}, throwing exception")
            throw new scala.RuntimeException(s"un supported interpolated value: ${unsupported.toString}")
        }
      }
      .foldLeft(value) {
        (current, kv) =>
          val (sub, newValue) = kv
          logger.debug(s"replacing ${sub.stringToReplace} with $newValue")
          current.replace(s"${sub.stringToReplace}", newValue)
      }
  }
}
