package byContext.score

trait Node{}
case class LeafNode(name: String, path:String, value:ValueProvider) extends Node
case class DataObjectNode(nodes:Map[String, Node]) extends Node
case class ArrayNode(nodes:Array[AnyRef]) extends Node

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

class QueryHandler{
  def query(ctx:QueryContext, node: Node, writer: ObjectWriter) : Unit = processNode(ctx, node, writer)

  private def processNode(ctx:QueryContext, node:Node, writer: ObjectWriter) : Unit = {
    def processLeaf(leaf: LeafNode, leafWriter:ObjectWriter): Unit = {
      leaf.value.get(ctx) fold(error => ???, {
        value => leafWriter.property(leaf.name, value.asInstanceOf[SingleValue].actualValue)
      })
    }

    def processArray(arrayNode: ArrayNode, arrayWriter:ArrayWriter) : Unit = {
      arrayNode.nodes.foreach(arrayWriter.append)
    }

    node match {
      case leaf : LeafNode =>
        processLeaf(leaf, writer)

      case obj : DataObjectNode =>

        obj.nodes.foreach{
          _ match {
            case (name, leaf : LeafNode) => processLeaf(leaf, writer)
            case (name, child : DataObjectNode) =>
              val childWriter = writer.createObjectWriter(name)
              processNode(ctx, child, childWriter)
            case (name, arrayChild : ArrayNode) => processArray(arrayChild, writer.createArrayWriter(name))
          }
        }
    }
  }
}
