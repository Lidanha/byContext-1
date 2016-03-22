package byContext.model

trait QueryContext {
  def getAs[T](key:String):Option[T]
}
