package byContext.index

class IndexBuilderInspector extends TreeNodeHandler{
  private val index = collection.mutable.Map[String,IndexItem]()

  def getIndex = index.toMap

  val handle: PartialFunction[(VisitContext, Any), Unit] = {
    case (ctx,v)=>index += ctx.absolutePath -> IndexItem(ctx.nodeName, v)
  }
}
