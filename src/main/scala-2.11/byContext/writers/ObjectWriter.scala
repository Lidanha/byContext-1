package byContext.writers

class ObjectWriter(val map:collection.mutable.Map[String,Any]) extends Writer{
  override def write(value:Any): Unit = ???

  override def getCollectionWriter(): Writer = ???

  override def getObjectWriter(): Writer = ???

  override def getPropertyWriter(propertyName: String): Writer = new PropertyWriter(map, propertyName)
}
