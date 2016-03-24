package byContext.writers.map

import byContext.writers.{WriterUnsupportedOperationException, Writer}
import com.typesafe.scalalogging.StrictLogging

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class MapRootWriter extends Writer with StrictLogging{
  var value : Option[Any] = None
  def getValue : Any = {
    value match {
      case Some(list:ListBuffer[Any]) => list.toArray
      case Some(map:mutable.Map[String, Any]) => map.toMap
      case Some(raw) => raw
      case None => None
    }
  }

  override def write(v: Any): Unit = value = Some(v)

  override def getCollectionWriter(): Writer = {
    val list = ListBuffer[Any]()
    logger.debug(s"collection writer was created")
    value = Some(list)
    new MapCollectionWriter(list)
  }

  override def getObjectWriter(): Writer = {
    val map = mutable.Map[String, Any]()
    logger.debug(s"object writer was created")
    value = Some(map)
    new MapObjectWriter(map)
  }

  override def getPropertyWriter(propertyName: String): Writer =
    throw new WriterUnsupportedOperationException("cannot call getPropertyWriter on RootWriterFactory")
}
