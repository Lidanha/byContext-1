package byContext.valueContainers.references

import byContext.index.{DataIndex, TreeNodeConverter, VisitContext}
import byContext.utils.MapExtensions
import byContext.valueContainers.ValueRefMarker

class ValueRefContainerConverter(globals:DataIndex) extends MapExtensions with TreeNodeConverter{
  override val convert: PartialFunction[(VisitContext, Any), Any] = {
    case (ctx, marker:ValueRefMarker) =>
      globals
        .getItem(marker.path)
        .fold(throw new RuntimeException(s"path ${marker.path} not found")) (_.value)
  }
}
