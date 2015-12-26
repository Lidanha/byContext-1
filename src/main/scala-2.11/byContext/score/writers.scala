package byContext.score

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

trait ObjectWriter{
  def createArrayWriter(name: String): ArrayWriter

  def createObjectWriter(name: String) : ObjectWriter

  def property(name:String, value:AnyRef)
}

class MapObjectWriter(val targetMap:collection.mutable.Map[String,AnyRef] = collection.mutable.Map()) extends ObjectWriter{
  override def property(name: String, value: AnyRef): Unit = {
    targetMap += name -> value
  }

  override def createObjectWriter(name: String): ObjectWriter = {
    val childTargetMap = mutable.Map[String, AnyRef]()
    targetMap += name -> childTargetMap
    new MapObjectWriter(childTargetMap)
  }

  override def createArrayWriter(name: String): ArrayWriter = {
    val childTarget = ListBuffer[AnyRef]()
    targetMap += name -> childTarget
    new ListBufferArrayWriter(childTarget)
  }
}

trait ArrayWriter{
  def append(value:AnyRef) : Unit
}

class ListBufferArrayWriter(target:ListBuffer[AnyRef]) extends ArrayWriter {
  override def append(value: AnyRef): Unit = target += value
}
