package byContext

trait MapExtensions {
  implicit def extendMap(src:Map[String,Any]) = new {
    def findByPath(path:String):Any = {
      path.split('.').foldLeft(src:Any){
        (currentNode,nodeName)=>
          currentNode match {
            case map: Map[String, Any] =>
              map.get(nodeName).fold(throw new RuntimeException(s"path not found ${path}"))(x=>x)
            case notMap => throw new RuntimeException(s"invalid path, node at level ${nodeName} is not a map object")
          }
      }
    }
  }
}
