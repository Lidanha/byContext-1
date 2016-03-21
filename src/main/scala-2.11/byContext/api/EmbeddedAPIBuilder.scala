package byContext.api

import byContext._

object EmbeddedAPIBuilder {
  def apply(data:Map[String,Any]):ByContextAPI = {
    /*extract the iterator and reuse is to build the indej and inspect nodes in the same time*/

    val indexBuilder = new IndexBuilderInspector()
    val dataSetHandler = new InMemoryDataSetHandler(new RecursiveQueryHandler())

    new DataSetVisitor().visit(data, inspectors = Seq(indexBuilder, new DataSetHandlerExtensionsInitializerExtension(dataSetHandler)))

    dataSetHandler.loadIndex(new MapDataIndex(indexBuilder.getIndex))
    new SyncInMemoryAPI(dataSetHandler)
  }
}
