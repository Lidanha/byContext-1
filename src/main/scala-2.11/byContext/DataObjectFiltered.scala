package byContext

import java.util

import byContext.score.{ObjectWriter, QueryContext}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/*object AddValueToMap{
  trait AddArrayTo{
    def add(value:AnyRef)
  }
  case class AddArrayToMap(val name:String, targetMap:mutable.Map[String, AnyRef]) extends AddArrayTo {
    override def add(value: AnyRef): Unit = targetMap += name -> value
  }
  case class AddArrayToList(targetList:mutable.ListBuffer[AnyRef]) extends AddArrayTo {
    override def add(value: AnyRef): Unit = targetList += value
  }

  def apply(name:String, value:Value, writer: ObjectWriter):Unit = {
    addValue(name, value, writer)
  }

  private def addValue(name:String, value: Value, writer: ObjectWriter) : Unit = {
    value match {
      case v:SingleValue =>
        addSingleValue(name, v, writer)
      case v:ObjectValue =>
        addObjectValue(name, v, writer.createObjectWriter(name))
      case v:ArrayValue =>
        addArrayValue(name, AddArrayToMap(name, target), v, target)
    }
  }

  private def addArrayValue(name:String, addTo: AddArrayTo, value:ArrayValue, writer: ObjectWriter) : Unit= {
    val list = mutable.ListBuffer[AnyRef]()
    addTo add list

    value.actualValue.foreach{
      case v:SingleValue =>
        list += v.actualValue
      case v:ObjectValue =>
        val childTarget = mutable.Map[String, AnyRef]()
        list += childTarget
        addObjectValue(name, v, childTarget)
      case v:ArrayValue =>
        addArrayValue(name, AddArrayToList(list), v, writer)
    }
  }

  private def addSingleValue(name:String, value:SingleValue, writer: ObjectWriter) : Unit =
    writer.property(name, value.actualValue)

  private def addObjectValue(name:String, value:ObjectValue, writer: ObjectWriter) : Unit =
    value.actualValue.foreach{kv=>
      val (childName,childValue) = kv
      addValue(childName, childValue, writer)
    }
}*/

trait Filtered{}
case class Single(private val valueProvider: ValueProvider) extends Filtered {
  def getValue(ctx:QueryContext) : Any = {
    valueProvider.get(ctx).
  }
}
case class DataObjectFiltered(properties:Array[AnyRef]) extends Filtered
case class ArrayFiltered(nodes:Array[AnyRef]) extends Filtered
case class RawFiltered(value:AnyRef) extends Filtered

trait Writer{
  def write(value:Any)
  def getPropertyWriter(propertyName:String) : Writer
  def getObjectWriter() : Writer
  def getCollectionWriter() : Writer
}
class ObjectWriter(private val map:collection.mutable.Map[String,Any]) extends Writer{
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

  private def validateDuplicatePropName(): Unit = ???
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
  def query(ctx:QueryContext, data:Map[String,Any], writer: ObjectWriter) : Unit = {
    data
      .foreach{ x =>
        val (name, value) = x
          val propWriter = writer.getPropertyWriter(name)
          value match {
            case ArrayFiltered(values) => propWriter.
          }

      }
  }
  def write(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] => obj.foreach{x=>
        val (name, value) = x
        write(value, writer.getPropertyWriter(name))
      }
      case collection : Array[Any] =>
        val collectionWriter = writer.getCollectionWriter()
        collection.foreach(value => write(value, collectionWriter))
      case value if value.getClass.isPrimitive => writer.write(value)
    }
  }
  def write()
}

/*class QueryHandler{
  def query(ctx:QueryContext, node: Node, writer: ObjectWriter) : Unit = processNode(ctx, node, writer)

  private def processNode(ctx:QueryContext, node:Any, writer: ObjectWriter) : Unit = {
    node match {
      case raw:RawNode =>
        ???
      case single : Single =>
        processSingle(ctx, single, writer)
      case array : ArrayNode =>

      case obj : DataObjectNode =>
        obj.properties.foreach{
          _ match {
            case (name, raw:RawNode) => writer.property(name, raw.value)
            case (name, single : Single) => processSingle(ctx, name, single, writer)
            case (name, child : DataObjectNode) => processNode(ctx, child, writer.createObjectWriter(name))
            case (name, arrayChild : ArrayNode) => processArray(arrayChild, writer.createArrayWriter(name))
          }
        }
    }
  }

  private def processSingle(ctx:QueryContext, name:String, single: Single, writer:ObjectWriter): Unit = {
    single.value.get(ctx) fold(error => ???, {
      value => writer.property(name, value.asInstanceOf[SingleValue].actualValue)
    })
  }

  private def processArray(arrayNode: ArrayNode, arrayWriter:ArrayWriter) : Unit = {
    arrayNode.nodes.foreach(arrayWriter.append)
  }

}*/
