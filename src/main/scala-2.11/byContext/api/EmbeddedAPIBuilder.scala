package byContext.api

import byContext.dataSetHandler.DefaultDataSetHandler
import byContext.index.{IndexBuilderInspector, MapDataIndex}
import byContext.queryHandler.RecursiveQueryHandler
import byContext.rawInputHandling.DataSetVisitor
import byContext.valueContainers.references.{VerifyNoRefMarkersConfigured, ValueRefContainerConverter}
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarkerConverter

object EmbeddedAPIBuilder {
  def apply(dataSet:Map[String,Any], globals:Option[Map[String,Any]]=None):ByContextAPI = {

    val indexBuilder = new IndexBuilderInspector()
    val dataSetHandler = new DefaultDataSetHandler(new RecursiveQueryHandler())

    val refConverters =
      globals
        .collect{case globals:Map[String,Any] =>
          Seq(new ValueRefContainerConverter(globals), new InterpolatedStringValueMarkerConverter(globals))
        }
        .getOrElse(Seq(VerifyNoRefMarkersConfigured))

    new DataSetVisitor().visit(dataSet,
      inspectors = Seq(indexBuilder),
      converters = refConverters
    )

    dataSetHandler.loadIndex(new MapDataIndex(indexBuilder.getIndex))
    new SyncInMemoryAPI(dataSetHandler)
  }
}
