package byContext.api

import byContext.{InMemoryDataSetHandler, DataIndex, RecursiveQueryHandler}

object EmbeddedAPIBuilder {
  def apply(index:DataIndex):ByContextAPI = {
    val dataSetHandler = new InMemoryDataSetHandler(index,new RecursiveQueryHandler())
    new SyncInMemoryAPI(dataSetHandler)
  }
}
