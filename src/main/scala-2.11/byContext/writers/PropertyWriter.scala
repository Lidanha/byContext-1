package byContext.writers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PropertyWriter(val map:collection.mutable.Map[String,Any], propertyName:String) extends Writer{
  override def write(value:Any): Unit = {
    validateDuplicatePropName()
    map += propertyName -> value
  }

  override def getCollectionWriter(): Writer = {
    validateDuplicatePropName()

    val list = ListBuffer[Any]()
    map += propertyName -> list
    new CollectionWriter(list)
  }

  override def getObjectWriter(): Writer = {
    validateDuplicatePropName()

    val propMap = mutable.Map[String, Any]()
    map += propertyName -> propMap
    new ObjectWriter(propMap)
  }

  override def getPropertyWriter(propertyName: String): Writer = ???

  private def validateDuplicatePropName(): Unit = {
    //TODO: implement
  }
}
