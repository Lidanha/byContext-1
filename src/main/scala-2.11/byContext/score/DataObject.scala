package byContext.score

import scala.collection.mutable

trait Node{}
case class Leaf(name: String, path:String, value:ValueProvider) extends Node
case class DataObject(nodes:Map[String, Node]) extends Node

object AddValueToMap{
  trait AddArrayTo{
    def add(value:AnyRef)
  }
  case class AddArrayToMap(val name:String, targetMap:mutable.Map[String, AnyRef]) extends AddArrayTo {
    override def add(value: AnyRef): Unit = targetMap += name -> value
  }
  case class AddArrayToList(targetList:mutable.ListBuffer[AnyRef]) extends AddArrayTo {
    override def add(value: AnyRef): Unit = targetList += value
  }

  def apply(name:String, value:Value, target :mutable.Map[String,AnyRef]):Unit = {
    addValue(name, value, target)
  }

  private def addValue(name:String, value: Value, target:mutable.Map[String,AnyRef]) : Unit = {
    value match {
      case v:SingleValue =>
        addSingleValue(name, v, target)
      case v:ObjectValue =>
        val childTarget = mutable.Map[String, AnyRef]()
        target += name -> childTarget
        addKeyValue(name, v, childTarget)
      case v:ArrayValue =>
        addArrayValue(name, AddArrayToMap(name, target), v, target)
    }
  }

  private def addArrayValue(name:String, addTo: AddArrayTo, value:ArrayValue, target:mutable.Map[String, AnyRef]) : Unit= {
    val list = mutable.ListBuffer[AnyRef]()
    addTo add list

    value.actualValue.foreach{
      case v:SingleValue =>
        list += v.actualValue
      case v:ObjectValue =>
        val childTarget = mutable.Map[String, AnyRef]()
        list += childTarget
        addKeyValue(name, v, childTarget)
      case v:ArrayValue =>
        addArrayValue(name, AddArrayToList(list), v, target)
    }
  }

  private def addSingleValue(name:String, value:SingleValue, target:mutable.Map[String, AnyRef]) : Unit =
    target += name -> value.actualValue

  private def addKeyValue(name:String, value:ObjectValue, target:mutable.Map[String,AnyRef]) : Unit =
    value.actualValue.foreach{kv=>
      val (childName,childValue) = kv
      addValue(childName, childValue, target)
    }
}

class QueryHandler{
  def query(ctx:QueryContext, node: Node) : Map[String, AnyRef] = {
  import collection._
    val resultMap = mutable.Map[String, AnyRef]()
    val indexablePathNames = mutable.Map[String, mutable.Map[String, AnyRef]]()

    processNode(ctx, node, resultMap)

    resultMap.toMap
  }

  private def processNode(ctx:QueryContext, node:Node, target:mutable.Map[String, AnyRef]) : Unit = {
    def processLeaf(leaf: Leaf, leafTarget:mutable.Map[String,AnyRef]): Unit = {
      leaf.value.get(ctx) fold(error => ???, {
        value => AddValueToMap(leaf.name, value, leafTarget)
      })
    }

    node match {
      case leaf : Leaf =>
        processLeaf(leaf, target)

      case obj : DataObject =>

        obj.nodes.foreach{
          _ match {
            case (name, leaf : Leaf) => processLeaf(leaf, target)
            case (name, child : DataObject) =>
              val childTargetMap = mutable.Map[String, AnyRef]()
              target += name -> childTargetMap
              processNode(ctx, child, childTargetMap)
          }
        }
    }
  }
}
