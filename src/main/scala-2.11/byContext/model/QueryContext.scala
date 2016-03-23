package byContext.model

trait QueryContext extends ContextOps{
  def getAs[T](key:String):Option[T]
}
