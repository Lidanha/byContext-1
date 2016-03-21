package byContext.valueContainers

import byContext.{DataSetHandler, DataSetHandlerExtension, QueryContext}

trait ValueRefContainer {
  def get(queryContext: QueryContext):Any
}

class UnsafeValueRefContainer(path:String) extends ValueRefContainer with DataSetHandlerExtension{
  var dataSetHandler: DataSetHandler = _

  override def init(handler: DataSetHandler): Unit = this.dataSetHandler = handler
  override def get(queryContext: QueryContext): Any ={
    dataSetHandler.get(path,queryContext)
  }
}
