package byContext.api

import byContext.model.Query

import scala.collection.mutable

class QueryBuilder extends Query{
  val map = mutable.Map[String,Any]()
  def item[T](i:(String,T))(implicit ev:QueryItemValue[T]) = map += i._1 -> ev.getValue(i._2)

  override def queryParams: Map[String, Any] = map.toMap

  override val enabledExtesions: Seq[String] = Seq.empty[String]
}

object QueryBuilder{
  def apply() = new QueryBuilder()
}