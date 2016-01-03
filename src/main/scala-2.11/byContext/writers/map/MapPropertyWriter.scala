package byContext.writers.map

import byContext.writers.{WriterUnsupportedOperationException, Writer}
import com.typesafe.scalalogging.StrictLogging

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MapPropertyWriter(val map:collection.mutable.Map[String,Any], propertyName:String) extends Writer with StrictLogging{
  override def write(value:Any): Unit = {
    validateDuplicatePropName()
    map += propertyName -> value
    logger.debug(s"wrote value $value for property $propertyName")
  }

  override def getCollectionWriter(): Writer = {
    validateDuplicatePropName()

    val list = ListBuffer[Any]()
    map += propertyName -> list

    logger.debug(s"added a collection for property $propertyName")

    new MapCollectionWriter(list)
  }

  override def getObjectWriter(): Writer = {
    validateDuplicatePropName()

    val propMap = mutable.Map[String, Any]()
    map += propertyName -> propMap

    logger.debug(s"added an object for property $propertyName")

    new MapObjectWriter(propMap)
  }

  override def getPropertyWriter(propertyName: String): Writer =
    throw new WriterUnsupportedOperationException("cannot call getPropertyWriter on PropertyWriter")

  private def validateDuplicatePropName(): Unit =
    if(map.contains(propertyName)) throw new WriterUnsupportedOperationException(s"attempt to write a property that was already written - $propertyName")
}
