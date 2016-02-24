package byContext.rules

import byContext.ValueRelevancy.ValueRelevancy
import byContext.{FilterRule, QueryContext, ValueRelevancy}
import scala.reflect.runtime.universe._

abstract class NumberRule[T  <% Double](val subj:String, val v:T)(implicit tag:TypeTag[T]) extends FilterRule with VerifyType {
  override def evaluate(ctx: QueryContext): ValueRelevancy = {
    ctx.get(subj)
      .fold(ValueRelevancy.Neutral){
        verify[T](_, contextValue => if (operator(contextValue, v)) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
      }
  }
  protected def operator(contextValue:T, value:T):Boolean
}

class NumberEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue == value
}

class NumberGreaterThan[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue > value
}

class NumberGreaterThanOrEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue >= value
}

class NumberSmallerThan[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue < value
}

class NumberSmallerThanOrEquals[T  <% Double](private val subject:String, private val value:T)(implicit tag:TypeTag[T]) extends NumberRule[T](subject, value) {
  override protected def operator(contextValue: T, value: T): Boolean = contextValue <= value
}