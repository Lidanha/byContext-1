package byContext.index


trait DataIndex{
  def getItem(path:String):Option[IndexItem]
}



