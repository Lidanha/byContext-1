package byContext.rawInputHandling

trait DataSetInspector {
  def inspect : PartialFunction[DataSetItem,Unit]
}
