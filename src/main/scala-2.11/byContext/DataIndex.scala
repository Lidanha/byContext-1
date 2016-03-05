package byContext

case class IndexItem(nodeName:String, value:Any)
trait DataIndex{
  def getItem(path:String):Option[IndexItem]
}
class SimpleMapDataIndex(private val data:Map[String,Any]) extends DataIndex{
  val index = build(data)
  private def build(dataToIndex:Map[String,Any]) : Map[String,IndexItem]={
    val index = collection.mutable.Map[String,IndexItem]()

    def processMapNode(parentPath:Option[String], node:Map[String,Any]) :Unit = {
      val currentPath = parentPath.fold("")(p=>s"$p.")
      node.foreach(n=>processSimpleNode(currentPath,n))
    }
    def processSimpleNode(parentPath:String, node:(String,Any)) :Unit= {
      val (nodeName,value) = node
      val currentPath = s"$parentPath$nodeName"
      index += currentPath -> IndexItem(nodeName, value)
      if(value.isInstanceOf[Map[String,Any]]){
        processMapNode(Some(currentPath), value.asInstanceOf[Map[String,Any]])
      }
    }

    processMapNode(None,dataToIndex)
    index.toMap
  }

  override def getItem(path:String):Option[IndexItem] = index.get(path)
}
