package byContext.dataSetHandler

import byContext.model.QueryContext

trait DataSetHandler {
  def get(path:String, queryContext: QueryContext) : Any
}

