package byContext

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait Writer{
  def write(value:Any)
  def getPropertyWriter(propertyName:String) : Writer
  def getObjectWriter() : Writer
  def getCollectionWriter() : Writer
}
class ObjectWriter(val map:collection.mutable.Map[String,Any]) extends Writer{
  override def write(value:Any): Unit = ???

  override def getCollectionWriter(): Writer = ???

  override def getObjectWriter(): Writer = ???

  override def getPropertyWriter(propertyName: String): Writer = new PropertyWriter(map, propertyName)
}
class PropertyWriter(private val map:collection.mutable.Map[String,Any], propertyName:String) extends Writer{
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
class CollectionWriter(targetList:ListBuffer[Any]) extends Writer {
  override def write(value:Any): Unit = targetList += value

  override def getCollectionWriter(): Writer = {
    val list = ListBuffer[Any]()
    targetList += list
    new CollectionWriter(list)
  }

  override def getObjectWriter(): Writer = {
    val map = mutable.Map[String,Any]()
    targetList += map
    new ObjectWriter(map)
  }

  override def getPropertyWriter(propertyName: String): Writer = ???
}
