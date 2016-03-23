package byContext.api

import byContext.dataSetHandler.DataSetHandler

import scala.concurrent.{ExecutionContext, Future}

class SyncInMemoryAPI(dataSetHandler: DataSetHandler) extends ByContextAPI {
  override def get(path: String, ctx: QueryBuilder)(implicit ec:ExecutionContext): Future[Map[String,Any]] =
  {
    try {
      val value = dataSetHandler.get(path, ctx)

      Future.successful(value)
    }
    catch {
      case t:Throwable => Future.failed(t)
    }
  }
}
