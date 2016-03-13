package byContext.api

import byContext.writers.map._
import byContext.{DataIndex, IndexItem, QueryContext, QueryHandler}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

class SyncInMemoryAPI(index:DataIndex, queryHandler: QueryHandler) extends ByContextAPI with StrictLogging{

  override def get(path: String, ctx: QueryContext): Future[Any] = {
    val f = index.getItem(path) match {
      case None => Future.failed(new RuntimeException(s"path: $path not found"))
      case Some(IndexItem(nodeName, value)) => filter(nodeName, value, ctx)
    }

    implicit val exec = ExecutionContext.global
    f.onFailure{
      case t => logger.error(s"failed processing query with context: ${ctx.toString()}",t)
    }
    f
  }

  private def filter(nodeName:String, value:Any, ctx: QueryContext) = {
    try {
      val rootWriter = new MapRootWriterFactory()
      queryHandler.query(ctx, value,rootWriter)
      Future.successful(rootWriter.getValue)
    }
    catch {
      case t:Throwable => Future.failed(t)
    }
  }
}
