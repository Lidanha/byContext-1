package byContext.writers.map

import byContext.writers.{WriterUnsupportedOperationException, Writer}
import com.typesafe.scalalogging.StrictLogging

class MapObjectWriter(val map:collection.mutable.Map[String,Any]) extends Writer with StrictLogging{
  override def write(value:Any): Unit =
    throw new WriterUnsupportedOperationException("cannot call write on MapObjectWriter, only getPropertyWriter is supporter")

  override def getCollectionWriter(): Writer =
    throw new WriterUnsupportedOperationException("cannot call getCollectionWriter on MapObjectWriter, only getPropertyWriter is supporter")

  override def getObjectWriter(): Writer =
    throw new WriterUnsupportedOperationException("cannot call getObjectWriter on MapObjectWriter, only getPropertyWriter is supporter")

  override def getPropertyWriter(propertyName: String): Writer = {
    logger.debug(s"returns a property writer for property $propertyName")
    new MapPropertyWriter(map, propertyName)
  }
}
