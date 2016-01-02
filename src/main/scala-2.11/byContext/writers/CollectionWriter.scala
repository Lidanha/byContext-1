package byContext.writers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

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
