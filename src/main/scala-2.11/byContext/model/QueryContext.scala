package byContext.model

trait QueryContext extends ContextOps{
  def getQueryParamAs[T](key:String):Option[T]
}
