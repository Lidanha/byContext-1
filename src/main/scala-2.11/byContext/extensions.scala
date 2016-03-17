package byContext

trait Extenstion{
  def init(dataSetHandler: DataSetHandler)
}

trait DataSetInspector{
  def inspect : PartialFunction[(String, String, Any),Unit]
}

class ExtensionsInitializerExtension(dataSetHandler: DataSetHandler) extends DataSetInspector{
  override def inspect = {
    case (_,_, ext: Extenstion) => ext.init(dataSetHandler)
  }
}