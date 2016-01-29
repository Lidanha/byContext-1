package byContext.api

import byContext.writers.SingleValueWriter
import byContext.writers.map.MapObjectWriter
import byContext.{DataIndex, QueryContext, QueryHandler}

import scala.concurrent.Future

class SyncInMemoryAPI(index:DataIndex, queryHandler: QueryHandler) extends ByContextAPI{

  override def get(path: String, ctx: QueryContext): Future[Any] = {
    index.getItem(path) match {
      case None => Future.failed(new RuntimeException(s"path: $path not found"))
      case Some(mapItem:Map[String,Any]) =>
        val map = collection.mutable.Map[String,Any]()
        queryHandler.query(ctx, mapItem,new MapObjectWriter(map))
        Future.successful(map.toMap)
      case Some(mapItem) =>
        val writer = new SingleValueWriter()
        queryHandler.query(ctx, mapItem,writer)
        Future.successful(writer getValue)
    }
  }
}
