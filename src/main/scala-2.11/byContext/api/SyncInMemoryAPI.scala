package byContext.api

import byContext.valueContainers.ValueContainer
import byContext.writers.map._
import byContext.{IndexItem, DataIndex, QueryContext, QueryHandler}

import scala.concurrent.Future

class SyncInMemoryAPI(index:DataIndex, queryHandler: QueryHandler) extends ByContextAPI{

  override def get(path: String, ctx: QueryContext): Future[Any] = {
    index.getItem(path) match {
      case None => Future.failed(new RuntimeException(s"path: $path not found"))
      case Some(IndexItem(nodeName, container:ValueContainer)) => filter(nodeName, container, ctx)
      case Some(IndexItem(nodeName, raw)) => Future.successful(raw)
    }
  }

  private def filter(nodeName:String, container:ValueContainer, ctx: QueryContext) = {
    val rootWriter = new MapRootWriterFactory()
    queryHandler.query(ctx, container,rootWriter)
    Future.successful(rootWriter.getValue)
  }
}
