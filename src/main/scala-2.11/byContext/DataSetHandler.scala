package byContext

import byContext.writers.map.MapRootWriterFactory
import com.typesafe.scalalogging.StrictLogging

trait DataSetHandler {
  def get(path:String, queryContext: QueryContext) : Any
}
class InMemoryDataSetHandler(queryHandler: QueryHandler) extends DataSetHandler with StrictLogging{
  var index:DataIndex = _
  def loadIndex(idx:DataIndex) = index = idx

  override def get(path: String, queryContext: QueryContext): Any = {
    index.getItem(path) match {
        //TODO:change to return either[] instead of throwing an exception
      case None => new RuntimeException(s"path: $path not found")
      case Some(IndexItem(nodeName, value)) => filter(nodeName, value, queryContext)
    }
  }
  private def filter(nodeName:String, value:Any, ctx: QueryContext) : Any = {
    val rootWriter = new MapRootWriterFactory()
    queryHandler.query(ctx, value,rootWriter)
    rootWriter.getValue
  }
}
