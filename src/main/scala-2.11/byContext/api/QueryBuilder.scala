package byContext.api

import byContext.model.Query

import scala.collection.mutable

class QueryBuilder extends Query{
  val map = mutable.Map[String,Any]()
  var enabledExtensions:Option[Seq[String]] = None

  def item[T](i:(String,T))(implicit ev:QueryItemValue[T]) = map += i._1 -> ev.getValue(i._2)
  def withExtensions(extensions:Seq[String]) : QueryBuilder = {
    enabledExtensions = Some(extensions)
    this
  }
  def getEnabledExtensions : Seq[String] = enabledExtensions.getOrElse(Seq.empty)
  override def queryParams: Map[String, Any] = map.toMap
}

object QueryBuilder{
  def apply() = new QueryBuilder()
}