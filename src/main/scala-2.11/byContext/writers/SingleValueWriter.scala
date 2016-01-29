package byContext.writers

class SingleValueWriter extends Writer{
  private var v:Any = _
  def getValue = v
  override def write(value: Any): Unit = v = value

  override def getCollectionWriter(): Writer =
    throw new WriterUnsupportedOperationException("cannot call getCollectionWriter on SingleValueWriter")

  override def getObjectWriter(): Writer =
    throw new WriterUnsupportedOperationException("cannot call getObjectWriter on SingleValueWriter")

  override def getPropertyWriter(propertyName: String): Writer =
    throw new WriterUnsupportedOperationException("cannot call getPropertyWriter on SingleValueWriter")
}
