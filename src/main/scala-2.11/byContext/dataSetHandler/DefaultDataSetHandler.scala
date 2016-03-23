package byContext.dataSetHandler

import byContext.index.{DataIndex, IndexItem}
import byContext.model.{Query, QueryContextImpl, QueryExtensionFactory}
import byContext.queryHandler.QueryHandler
import byContext.writers.map.MapObjectWriter
import com.typesafe.scalalogging.StrictLogging

import scala.collection.mutable

class DefaultDataSetHandler(queryHandler: QueryHandler, queryExtensionFactories:Map[String,QueryExtensionFactory]) extends DataSetHandler with StrictLogging{
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
    val rootResult = mutable.Map[String, Any]()
    val rootWriter = new MapObjectWriter(rootResult)

    val extensions = q.getEnabledExtensions.flatMap{ name=>
      queryExtensionFactories.get(name).map{ext=>
        val extWriter = rootWriter.getPropertyWriter(name)
        ext.create(extWriter)
      }
    }

    val ctx = new QueryContextImpl(q.queryParams,extensions)

    val dataWriter = rootWriter.getPropertyWriter("query-result")
    queryHandler.query(ctx, value,dataWriter)

    rootResult.toMap
  }
}
