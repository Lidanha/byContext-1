package byContext

class DataSetVisitor() {
  def visit(dataToIndex:Map[String,Any], inspectors:Seq[DataSetInspector]) : Unit ={
    processMapNode(None,dataToIndex)

    def processMapNode(parentPath:Option[String], node:Map[String,Any]) :Unit = {
      val currentPath = parentPath.fold("")(p=>s"$p.")
      node.foreach(n=>processSimpleNode(currentPath,n))
    }
    def processSimpleNode(parentPath:String, node:(String,Any)) :Unit= {
      val (nodeName,value) = node
      val currentPath = s"$parentPath$nodeName"

      val input = (currentPath, nodeName, value)

      inspectors.foreach{
        f=>
          if(f.inspect.isDefinedAt(input)) f.inspect.apply(input)
      }

      if(value.isInstanceOf[Map[String,Any]]){
        processMapNode(Some(currentPath), value.asInstanceOf[Map[String,Any]])
      }
    }
  }
}