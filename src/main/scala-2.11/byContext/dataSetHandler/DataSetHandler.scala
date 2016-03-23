package byContext.dataSetHandler

import byContext.model.Query

trait DataSetHandler {
  def get(path:String, query: Query) : Any
}

