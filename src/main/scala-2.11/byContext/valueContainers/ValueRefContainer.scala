package byContext.valueContainers

import byContext.QueryContext

trait ValueRefContainer {
  def get(queryContext: QueryContext):Any
}
