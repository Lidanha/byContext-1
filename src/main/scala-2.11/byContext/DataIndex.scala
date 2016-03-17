package byContext

case class IndexItem(nodeName:String, value:Any)
trait DataIndex{
  def getItem(path:String):Option[IndexItem]
}
class MapDataIndex(map:Map[String,IndexItem]) extends DataIndex {
  override def getItem(path:String):Option[IndexItem] = map.get(path)
}
class IndexBuilderInspector extends DataSetInspector{
  private val index = collection.mutable.Map[String,IndexItem]()

  def getIndex = index.toMap

  override def inspect: PartialFunction[DataSetItem, Unit] = {
    case DataSetItem(fullPath, nodeName, item: Any) => index += fullPath -> IndexItem(nodeName, item)
  }
}

