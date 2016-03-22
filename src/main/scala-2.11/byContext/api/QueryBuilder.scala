package byContext.api

import byContext.model.QueryContext

import scala.collection.mutable

class QueryBuilder extends QueryContext{
  val map = mutable.Map[String,Any]()
  def item[T](i:(String,T))(implicit ev:QueryItemValue[T]) = map += i._1 -> ev.getValue(i._2)

  def getAs[T](key:String):Option[T] = map.get(key).map(_.asInstanceOf[T])

  override def toString() : String = {
    def formatItems = {
      map.map{
        x=>s"${x._1}->${x._2}"
      }.mkString(" - ")
    }
    s"{QueryContext: ${formatItems}"
  }
}

object QueryBuilder{
  def apply() = new QueryBuilder()
}