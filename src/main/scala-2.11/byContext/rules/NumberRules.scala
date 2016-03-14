package byContext.rules

import byContext.{FilterRule, Probe, QueryContext, ValueRelevancy}

import scala.reflect.runtime.universe._

abstract class NumberRule[T  <% Double](val subj:String, val v:T)(implicit tag:TypeTag[T]) extends FilterRule with VerifyType {
  override def evaluate(ctx: QueryContext, probe: Probe): Unit = {
    val valueRelevancy = ctx.get(subj)
      .fold(ValueRelevancy.Neutral){
        verify[T](_, contextValue => if (operator(contextValue, v)) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
      }
    probe setRelevancy valueRelevancy
  }
  protected def operator(contextValue:T, value:T):Boolean
}

case class NumberEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue == value
}

case class NumberGreaterThan[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = {
    contextValue > value
  }
}

case class NumberGreaterThanOrEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue >= value
}

case class NumberSmallerThan[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue < value
}

case class NumberSmallerThanOrEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue <= value
}