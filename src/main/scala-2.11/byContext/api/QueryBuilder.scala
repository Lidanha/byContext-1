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

  val versions = scala.collection.mutable.Map[String,Int]()
  override def valueSelected(path: String, metadata: Map[String, Any]): Unit = {
    metadata.get("version").foreach(v=>versions += path->v.toString.toInt)
  }
}

object QueryBuilder{
  def apply() = new QueryBuilder()
}