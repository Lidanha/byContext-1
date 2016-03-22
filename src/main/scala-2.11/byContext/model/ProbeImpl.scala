package byContext.model

private[byContext] class ProbeImpl() extends Probe {
  private[this] var relevantCount : Int = 0
  private[this] var notRelevantCount : Int = 0
  private[this] var neutralCount : Int = 0

  def getRelevantCount = relevantCount
  def getNotRelevantCount = notRelevantCount
  def getNeutralCount = neutralCount

  import ValueRelevancy._
  override def setRelevancy(r: ValueRelevancy): Unit = r match {
    case Relevant => relevantCount += 1
    case NotRelevant => notRelevantCount += 1
    case Neutral => neutralCount += 1
  }
}
