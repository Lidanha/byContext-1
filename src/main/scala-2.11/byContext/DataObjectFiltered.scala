package byContext

import byContext.score.QueryContext

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait Filtered{}
case class Single(valueProvider: ValueProvider) extends Filtered
case class DataObjectFiltered(valueProvider: ValueProvider) extends Filtered
case class ArrayFiltered(valueProvider: ValueProvider) extends Filtered {
}
case class Raw(value:Any)

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

class QueryHandler{
  def query(ctx:QueryContext, data:Map[String,Any], writer: ObjectWriter) : Unit = process(data, writer)(ctx)

  private def process(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] => obj.foreach{x=>
        val (name, value) = x
        process(value, writer.getPropertyWriter(name))
      }
      case collection : Array[Any] =>
        val collectionWriter = writer.getCollectionWriter()
        collection.foreach(value => process(value, collectionWriter))
      case Single(provider) =>
        provider.get(ctx).fold(err => ???,value=>process(value, writer))
      case ArrayFiltered(provider) => provider.get(ctx).fold(err => ???, value => {
        val filteredArray = value.asInstanceOf[Array[Any]]
        val collectionWriter = writer.getCollectionWriter()
        filteredArray.foreach(value => process(value, collectionWriter))
      })
      case DataObjectFiltered(provider) => provider.get(ctx).fold(err => ???, value => {
        val filteredArray = value.asInstanceOf[Array[(String,Any)]]
        val objectWriter = writer.getObjectWriter()
        filteredArray.foreach{x=>
          val (name, value) = x
          process(value, objectWriter.getPropertyWriter(name))
        }
      })

      case None => writer.write(None)
      case Raw(value) => writer.write(value)
      case value : String => writer.write(value)
    }
  }
}
