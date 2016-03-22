package byContext.valueContainers.stringInterpolation

import byContext.exceptions.ByContextError
import byContext.model.QueryContext
import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
import byContext.score.valueContainers.SingleValueContainer
import byContext.utils.MapExtensions
import com.typesafe.scalalogging.StrictLogging

class InterpolatedStringValueMarkerConverter(globals:Map[String,Any]) extends DataSetItemConverter with MapExtensions with StrictLogging{
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
    globals.findByPath(path) match {
      case container:SingleValueContainer=>container
      case value:String => new SingleValueContainer {
        override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(value)
      }
      case notsupported =>
        throw new RuntimeException(s"unsupported ref for string interpolation: ${notsupported} only ${classOf[SingleValueContainer].getName} is supported")
    }
  }
}
