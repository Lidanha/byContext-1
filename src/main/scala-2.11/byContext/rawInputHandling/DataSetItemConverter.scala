package byContext.rawInputHandling

trait DataSetItemConverter {
  def convert : PartialFunction[DataSetItem,Any]
}
