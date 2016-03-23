package byContext.valueContainers.references

import byContext.index.{IndexItem, DataIndex}
import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
import byContext.utils.MapExtensions
import byContext.valueContainers.ValueRefMarker

class ValueRefContainerConverter(globals:DataIndex) extends DataSetItemConverter with MapExtensions{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,container:ValueRefMarker) =>
      globals
        .getItem(container.path) match {
        case Some(IndexItem(_,v)) => v
        case None => throw new RuntimeException(s"path ${container.path} not found")
      }
  }
}
