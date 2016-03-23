package byContext.api

import byContext.dataSetHandler.DefaultDataSetHandler
import byContext.defaultValueSelection.{CompositeDefaultValueSelector, DefaultMarkedDefaultValueSelector, HighestScoreDefaultValueSelector}
import byContext.index.{DataIndex, IndexBuilderInspector, MapDataIndex}
import byContext.model.QueryExtensionFactory
import byContext.queryHandler.RecursiveQueryHandler
import byContext.rawInputHandling.{DataSetItemConverter, DataSetVisitor}
import byContext.score.DefaultScoreCalculator
import byContext.score.valueContainers.{ArrayValueConverter, SingleValueConverter}
import byContext.valueContainers.references.{ValueRefContainerConverter, VerifyNoRefMarkersConfigured}
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarkerConverter

object EmbeddedAPIBuilder {
  def apply(dataSet:Map[String,Any],
            globals:Option[Map[String,Any]]=None,
            queryExtensionFactories: Map[String,QueryExtensionFactory]=Map.empty):ByContextAPI = {

    val dataSetHandler = new DefaultDataSetHandler(new RecursiveQueryHandler(), queryExtensionFactories)
    val scoreCalculator = new DefaultScoreCalculator()
    val defaultValueSelector = new CompositeDefaultValueSelector(Seq(
      new HighestScoreDefaultValueSelector(),new DefaultMarkedDefaultValueSelector()
    ))

    val converters = Seq(
      new SingleValueConverter(scoreCalculator, defaultValueSelector),
      new ArrayValueConverter(scoreCalculator)
    )

    val refConverters: Seq[DataSetItemConverter] =
      globals
        .map{input =>

          val globalsIndex = buildIndex(input,converters ++ Seq(VerifyNoRefMarkersConfigured))

          Seq(
            new ValueRefContainerConverter(globalsIndex),
            new InterpolatedStringValueMarkerConverter(globalsIndex)
          )
        }
        .getOrElse(Seq(VerifyNoRefMarkersConfigured))

    val dataIndex = buildIndex(dataSet,converters ++ refConverters)

    dataSetHandler.loadIndex(dataIndex)
    new SyncInMemoryAPI(dataSetHandler)
  }
  def buildIndex(input:Map[String,Any], converters:Seq[DataSetItemConverter]):DataIndex = {
    val indexBuilder = new IndexBuilderInspector()
    new DataSetVisitor().visit(input,
      inspectors = Seq(indexBuilder),
      converters = converters
    )
    new MapDataIndex(indexBuilder.getIndex)
  }
}
