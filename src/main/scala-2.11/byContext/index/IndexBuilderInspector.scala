package byContext.index

import byContext.rawInputHandling.{DataSetItem, DataSetInspector}

class IndexBuilderInspector extends DataSetInspector{
  private val index = collection.mutable.Map[String,IndexItem]()

  def getIndex = index.toMap

  override def inspect: PartialFunction[DataSetItem, Unit] = {
    case DataSetItem(fullPath, nodeName, item: Any) =>
      index += fullPath -> IndexItem(nodeName, item)
  }
}
