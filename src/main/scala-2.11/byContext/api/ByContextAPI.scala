package byContext.api

import byContext.QueryContext

import scala.concurrent.Future

trait ByContextAPI {
  def get(path:String, ctx:QueryContext) : Future[Any]
}
