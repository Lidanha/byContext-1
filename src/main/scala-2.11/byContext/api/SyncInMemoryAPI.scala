package byContext.api

import byContext.writers.SingleValueWriter
import byContext.writers.map.MapObjectWriter
import byContext.{DataIndex, QueryContext, QueryHandler}

import scala.concurrent.Future

class SyncInMemoryAPI(index:DataIndex, queryHandler: QueryHandler) extends ByContextAPI{

  override def get(path: String, ctx: QueryContext): Future[Any] = {
    index.getItem(path) match {
      case None => Future.failed(new RuntimeException(s"path: $path not found"))
      case Some(mapItem:Map[String,Any]) => filterMap(mapItem, ctx)
      case Some(mapItem) => filterSingle(mapItem, ctx)
    }
  }
  private def filterMap(mapItem:Map[String,Any], ctx: QueryContext) = {
    val map = collection.mutable.Map[String,Any]()
    queryHandler.query(ctx, mapItem,new MapObjectWriter(map))
    Future.successful(map.toMap)
  }
  private def filterSingle(mapItem:Any, ctx:QueryContext) = {
    val writer = new SingleValueWriter()
    queryHandler.query(ctx, mapItem,writer)
    Future.successful(writer getValue)
  }
}
