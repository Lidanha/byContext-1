package byContext.valueContainers.references

import byContext.rawInputHandling.{DataSetItem, DataSetItemConverter}
import byContext.utils.MapExtensions
import byContext.valueContainers.ValueRefMarker

class ValueRefContainerConverter(data:Map[String,Any]) extends DataSetItemConverter with MapExtensions{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,container:ValueRefMarker) =>
      data.findByPath(container.path)
  }
}
