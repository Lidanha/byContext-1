package byContext.writers

trait Writer{
  def write(value:Any)
  def getPropertyWriter(propertyName:String) : Writer
  def getObjectWriter() : Writer
  def getCollectionWriter() : Writer
}
object Writer{
  val NoOp = new Writer {
    override def getCollectionWriter(): Writer = this

    override def getObjectWriter(): Writer = this

    override def write(value: Any): Unit = {}

    override def getPropertyWriter(propertyName: String): Writer = this
  }
}



