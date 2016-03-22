package byContext.api

import byContext.dataSetHandler.DefaultDataSetHandler
import byContext.index.{IndexBuilderInspector, MapDataIndex}
import byContext.queryHandler.RecursiveQueryHandler
import byContext.rawInputHandling.DataSetVisitor
import byContext.valueContainers.RefContainerConverter
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarkerConverter

object EmbeddedAPIBuilder {
  def apply(data:Map[String,Any]):ByContextAPI = {

    val indexBuilder = new IndexBuilderInspector()
    val dataSetHandler = new DefaultDataSetHandler(new RecursiveQueryHandler())

    new DataSetVisitor().visit(data,
      inspectors = Seq(indexBuilder),
      converters = Seq(new RefContainerConverter(data), new InterpolatedStringValueMarkerConverter(data))
    )

    dataSetHandler.loadIndex(new MapDataIndex(indexBuilder.getIndex))
    new SyncInMemoryAPI(dataSetHandler)
  }
}
