package byContext.writers.map

import byContext.writers.{WriterUnsupportedOperationException, Writer}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MapCollectionWriter(val targetList:ListBuffer[Any]) extends Writer {
  override def write(value:Any): Unit = targetList += value

  override def getCollectionWriter(): Writer = {
    val list = ListBuffer[Any]()
    targetList += list
    new MapCollectionWriter(list)
  }

  override def getObjectWriter(): Writer = {
    val map = mutable.Map[String,Any]()
    targetList += map
    new MapObjectWriter(map)
  }

  override def getPropertyWriter(propertyName: String): Writer =
    throw new WriterUnsupportedOperationException("cannot call getPropertyWriter on MapCollectionWriter")
}
