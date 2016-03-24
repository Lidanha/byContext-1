package byContext.index

import byContext.writers.Writer
import com.typesafe.scalalogging.StrictLogging

trait TreeNodeConverter{
  val convert : PartialFunction[(VisitContext,Any),Any]
}
trait TreeNodeHandler{
  val handle: PartialFunction[(VisitContext, Any), Unit]
}
case class VisitContext(nodeName:String, absolutePath:String)
case class ConversionContext(v:Any, visitContext: VisitContext)

class TreeVisitor extends StrictLogging{
  def handle(data:Any, writer: Writer,
             converters:Seq[TreeNodeConverter]=Seq.empty,
             handlers:Seq[TreeNodeHandler] = Seq.empty) : Unit =
    process(data,VisitContext("",""), writer)(converters, handlers)

  private def process(v:Any,ctx:VisitContext, writer: Writer)
                     (implicit converters:Seq[TreeNodeConverter],
                      handlers:Seq[TreeNodeHandler]) : Unit = {
    v match {
      case obj:Iterable[(String,Any)] => handleRawObject(obj,ctx,writer)
      case collection : Array[Any] => handleArray(collection,ctx,writer)
      case raw => handleRaw(v,ctx,writer)
    }
  }

  private def handleRaw(v:Any, ctx:VisitContext, writer: Writer)
                       (implicit converters:Seq[TreeNodeConverter],
                        handlers:Seq[TreeNodeHandler]) : Unit = {
    val newValue = converters.foldLeft((ctx,v)){
      (current, converter)=>
        converter.convert.lift(current).map(newVal => (current._1,newVal)).getOrElse(current)
    }._2
    if(newValue != v){
      process(newValue,ctx,writer)
    }else{
      handlers.foreach(f=>f.handle.lift((ctx,newValue)))
      writer.write(v)
    }
  }

  private def handleRawObject(obj: Iterable[(String,Any)], ctx:VisitContext, writer: Writer)
                             (implicit converters:Seq[TreeNodeConverter],
                              handlers:Seq[TreeNodeHandler]) : Unit = {
    handlers.foreach(f=>f.handle.lift((ctx,obj)))
    val objectWriter = writer.getObjectWriter()
    obj.foreach { x =>
      val (name, value) = x
      val childPath = updatePath(ctx.absolutePath,name)
      process(value, ctx.copy(name,childPath), objectWriter.getPropertyWriter(name))
    }
  }

  private def updatePath(parent:String, child:String) = if(parent.isEmpty) child else s"${parent}.$child"

  private def handleArray(collection: Array[Any],ctx:VisitContext, writer: Writer)
                         (implicit converters:Seq[TreeNodeConverter],
                          handlers:Seq[TreeNodeHandler]) : Unit = {
    handlers.foreach(f=>f.handle.lift((ctx,collection)))
    val collectionWriter = writer.getCollectionWriter()
    collection.view.zipWithIndex.foreach{x=>
      val (v,index) = x
      val newNodeName = s"${ctx.nodeName}[$index]"
      val childPath = updatePath(ctx.absolutePath, newNodeName)
      process(v, ctx.copy(nodeName = newNodeName, absolutePath = childPath), collectionWriter)
    }
  }
}