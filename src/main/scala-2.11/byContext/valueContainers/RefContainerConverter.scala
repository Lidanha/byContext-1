package byContext.valueContainers

import byContext.rawInputHandling.{DataSetItemConverter, DataSetItem}
import byContext.utils.MapExtensions

class RefContainerConverter(data:Map[String,Any]) extends DataSetItemConverter with MapExtensions{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,container:ValueRefMarker) =>
      data.findByPath(container.path)
  }
}
