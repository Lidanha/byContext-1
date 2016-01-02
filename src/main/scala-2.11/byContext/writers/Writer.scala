package byContext.writers

trait Writer{
  def write(value:Any)
  def getPropertyWriter(propertyName:String) : Writer
  def getObjectWriter() : Writer
  def getCollectionWriter() : Writer
}



