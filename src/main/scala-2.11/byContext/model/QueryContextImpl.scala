package byContext.model

class QueryContextImpl(queryParams:Map[String,Any], extensions:Seq[QueryExtension]) extends QueryContext{
  override def getQueryParamAs[T](key:String):Option[T] = queryParams.get(key).map(_.asInstanceOf[T])

  override def notify[E<:Event](e:E): Unit = extensions.foreach(_.handle.lift(e))

  override def toString() : String = {
    def formatItems = {
      queryParams.map{
        x=>s"${x._1}->${x._2}"
      }.mkString(" - ")
    }
    s"{QueryContext: ${formatItems}"
  }
}
