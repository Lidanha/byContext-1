package byContext.api

import byContext.writers.map.MapObjectWriter
import byContext.{DataIndex, QueryContext, RecursiveQueryHandler}

import scala.concurrent.Future

class SyncInMemoryAPI(index:DataIndex, queryHandler: RecursiveQueryHandler) extends ByContextAPI{

  override def get(path: String, ctx: QueryContext): Future[Any] = {
    index.getItem(path) match {
      case None => Future.failed(new RuntimeException(s"path: $path not found"))
      case Some(item) =>
        val map = collection.mutable.Map[String,Any]()
        queryHandler.query(ctx, item,new MapObjectWriter(map))
        Future.successful(map.toMap)
    }
  }
}
