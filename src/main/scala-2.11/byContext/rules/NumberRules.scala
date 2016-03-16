package byContext.rules

import byContext.{FilterRule, Probe, QueryContext, ValueRelevancy}

abstract class NumberRule(val subj:String, val v:Double) extends FilterRule {
  override def evaluate(ctx: QueryContext, probe: Probe): Unit = {
    val valueRelevancy = ctx.getAs[Double](subj)
      .fold(ValueRelevancy.Neutral){
        contextValue =>
          if (operator(contextValue, v)) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant
      }
    probe setRelevancy valueRelevancy
  }
  protected def operator(contextValue:Double, value:Double):Boolean
}

case class NumberEquals(private val subject:String, private val value:Double) extends NumberRule(subject, value) {
  override protected def operator(contextValue:Double, value: Double): Boolean = contextValue == value
}

case class NumberGreaterThan(private val subject:String, private val value:Double) extends NumberRule(subject, value) {
  override protected def operator(contextValue:Double, value: Double): Boolean = {
    contextValue > value
  }
}

case class NumberGreaterThanOrEquals(private val subject:String, private val value:Double) extends NumberRule(subject, value) {
  override protected def operator(contextValue:Double, value: Double): Boolean = contextValue >= value
}

case class NumberSmallerThan(private val subject:String, private val value:Double) extends NumberRule(subject, value) {
  override protected def operator(contextValue:Double, value: Double): Boolean = contextValue < value
}

case class NumberSmallerThanOrEquals(private val subject:String, private val value:Double) extends NumberRule(subject, value) {
  override protected def operator(contextValue:Double, value: Double): Boolean = contextValue <= value
}