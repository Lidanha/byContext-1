package byContext.index

class MapDataIndex(map:Map[String,IndexItem]) extends DataIndex {
  override def getItem(path:String):Option[IndexItem] = map.get(path)
}
