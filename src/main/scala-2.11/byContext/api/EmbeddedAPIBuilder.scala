package byContext.api

import byContext._
import byContext.valueContainers.{InterpolatedStringValueMarkerConverter, RefContainerConverter}

object EmbeddedAPIBuilder {
  def apply(data:Map[String,Any]):ByContextAPI = {

    val indexBuilder = new IndexBuilderInspector()
    val dataSetHandler = new InMemoryDataSetHandler(new RecursiveQueryHandler())

    new DataSetVisitor().visit(data,
      inspectors = Seq(indexBuilder),
      converters = Seq(new RefContainerConverter(data), new InterpolatedStringValueMarkerConverter(data))
    )

    dataSetHandler.loadIndex(new MapDataIndex(indexBuilder.getIndex))
    new SyncInMemoryAPI(dataSetHandler)
  }
}
