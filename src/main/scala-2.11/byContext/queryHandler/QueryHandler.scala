package byContext.queryHandler

import byContext.model.QueryContext
import byContext.writers.Writer

trait QueryHandler{
  def query(ctx:QueryContext, data:Any, writer: Writer) : Unit
}
