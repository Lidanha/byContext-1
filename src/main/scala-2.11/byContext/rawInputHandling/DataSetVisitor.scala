package byContext.rawInputHandling

class DataSetVisitor {
  def visit(input:Map[String,Any], converters:Seq[DataSetItemConverter] = Seq.empty, inspectors:Seq[DataSetInspector]= Seq.empty) : Unit ={
    processMapNode(None,input)

    def processMapNode(parentPath:Option[String], node:Map[String,Any]) :Unit = {
      val currentPath = parentPath.fold("")(p=>s"$p.")
      node.foreach(n=>processSimpleNode(currentPath,n))
    }
    def processSimpleNode(parentPath:String, node:(String,Any)) :Unit= {
      val (nodeName,value) = node
      val currentPath = s"$parentPath$nodeName"

      val convertedDataSetItem =
        converters.foldLeft(DataSetItem(currentPath, nodeName, value)){
          (item, converter) => converter.convert.lift(item) match{
            case Some(newValue)=>
              item.copy(value = newValue)
            case None => item
          }
        }

      inspectors.foreach(inspector=> inspector.inspect.lift(convertedDataSetItem))

      if(value.isInstanceOf[Map[String,Any]]){
        processMapNode(Some(currentPath), value.asInstanceOf[Map[String,Any]])
      }
    }
  }
}