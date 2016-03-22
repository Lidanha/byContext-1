package byContext.model

import byContext.model.ValueRelevancy.ValueRelevancy

trait Probe {
  def setRelevancy(r:ValueRelevancy) : Unit
}
