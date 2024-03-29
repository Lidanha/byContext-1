package byContext.valueContainers.stringInterpolation

import byContext.exceptions.ByContextError
import byContext.index.{VisitContext, TreeNodeConverter, IndexItem, DataIndex}
import byContext.model.QueryContext
import byContext.score.valueContainers.SingleValueContainer
import byContext.utils.MapExtensions
import com.typesafe.scalalogging.StrictLogging

class InterpolatedStringValueMarkerConverter(globalsIndex:DataIndex)
  extends MapExtensions with StrictLogging with TreeNodeConverter{
  val re = """<<.*>>""".r

  override val convert: PartialFunction[(VisitContext, Any), Any] = {
    case (ctx,marker:InterpolatedStringValueMarker) =>
      val subs = buildSubsitutes(marker, ctx.absolutePath)
      new InterpolatedStringValueContainer(marker.value, subs)
  }

  private def buildSubsitutes(marker: InterpolatedStringValueMarker, ownPath:String): Seq[Substitute] = {
    re
      .findAllMatchIn(marker.value)
      .map { m =>
        val pathToFind = m.source.subSequence(m.start + 2, m.end - 2).toString
        val stringToReplace = m.source.subSequence(m.start, m.end).toString
        Substitute(stringToReplace, findContainer(pathToFind,ownPath))
      }
      .toSeq
  }
  private def findContainer(pathToFind:String, ownPath:String):SingleValueContainer = {
    globalsIndex.getItem(pathToFind) match {
      case Some(IndexItem(_,container:SingleValueContainer))=>container
      case Some(IndexItem(_,v:String)) => new SingleValueContainer {
        override def get(ctx: QueryContext): Either[ByContextError, Any] = Right(v)
      }
      case None => throw new RuntimeException(s"path: $pathToFind not found")
      case notsupported =>
        throw new RuntimeException(s"item marked for interpolation: $ownPath with ownPath " +
          s"$pathToFind is not supported: ${notsupported} only ${classOf[SingleValueContainer].getName} is supported")
    }
  }
}
