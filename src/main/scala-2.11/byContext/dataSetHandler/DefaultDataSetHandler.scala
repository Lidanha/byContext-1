package byContext.dataSetHandler

import byContext.index.{DataIndex, IndexItem}
import byContext.model.{Query, QueryContextImpl, QueryExtension}
import byContext.queryHandler.QueryHandler
import byContext.writers.map.MapObjectWriter
import com.typesafe.scalalogging.StrictLogging

import scala.collection.mutable

class DefaultDataSetHandler(queryHandler: QueryHandler) extends DataSetHandler with StrictLogging{
  var index:DataIndex = _
  def loadIndex(idx:DataIndex) = index = idx

  override def get(path: String, query: Query): Map[String,Any] = {

    index.getItem(path) match {
        //TODO:change to return either instead of throwing an exception
      case None => throw new RuntimeException(s"path: $path not found")
      case Some(IndexItem(nodeName, value)) => filter(nodeName, value, query)
    }
  }
  private def filter(nodeName:String, value:Any, q: Query) : Map[String,Any] = {
    val ctx = new QueryContextImpl(q.queryParams,Seq.empty[QueryExtension])

    val rootResult = mutable.Map[String, Any]()
    val rootWriter = new MapObjectWriter(rootResult)

    val dataWriter = rootWriter.getPropertyWriter("data")
    queryHandler.query(ctx, value,dataWriter)
    rootResult.toMap
  }
}
