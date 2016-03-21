package byContext

import byContext.writers.map.MapRootWriterFactory
import com.typesafe.scalalogging.StrictLogging

trait DataSetHandler {
  def get(path:String, queryContext: QueryContext) : Any
}

