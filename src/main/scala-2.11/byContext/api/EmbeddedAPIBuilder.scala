package byContext.api

import byContext.{DataIndex, RecursiveQueryHandler}

object EmbeddedAPIBuilder {
  def apply(index:DataIndex):ByContextAPI = {
    new SyncInMemoryAPI(index,new RecursiveQueryHandler())
  }
}
