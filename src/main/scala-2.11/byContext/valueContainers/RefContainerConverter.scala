package byContext.valueContainers

import byContext.{MapExtensions, DataSetItem, DataSetItemConverter}

class RefContainerConverter(data:Map[String,Any]) extends DataSetItemConverter with MapExtensions{
  override def convert: PartialFunction[DataSetItem, Any] = {
    case DataSetItem(_,_,container:ValueRefMarker) =>
      data.findByPath(container.path)
  }
}
