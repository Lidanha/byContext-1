package byContext.api

import scala.concurrent.{ExecutionContext, Future}

trait ByContextAPI {
  def get(path:String, query: QueryBuilder)(implicit ec:ExecutionContext) : Future[Any]
}
