package byContext.api

import byContext._

object EmbeddedAPIBuilder {
  def apply(data:Map[String,Any]):ByContextAPI = {
    /*extract the iterator and reuse is to build the indej and inspect nodes in the same time*/

    val indexBuilder = new IndexBuilderInspector()
    val dataSetHandler = new InMemoryDataSetHandler(new RecursiveQueryHandler())
    val dataSetVisitor = new DataSetVisitor()
    dataSetVisitor.visit(data, Seq(indexBuilder, new ExtensionsInitializerExtension(dataSetHandler)))
    dataSetHandler.loadIndex(new MapDataIndex(indexBuilder.getIndex))
    new SyncInMemoryAPI(dataSetHandler)
  }
}
