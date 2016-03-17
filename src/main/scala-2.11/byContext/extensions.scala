package byContext

trait Extenstion{
  def init(dataSetHandler: DataSetHandler)
}

class ExtensionsInitializerExtension(dataSetHandler: DataSetHandler) extends DataSetInspector{
  override def inspect = {
    case DataSetItem(_,_, ext: Extenstion) => ext.init(dataSetHandler)
  }
}