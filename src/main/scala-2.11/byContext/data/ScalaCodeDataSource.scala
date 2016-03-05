package byContext.data

import byContext.rules.{NumberEquals, TextMatch}
import byContext.score.ScoreCalculator
import byContext.score.valueContainers._
import byContext.{FilterRule, HighestScoreDefaultValueSelector, PossibleValue}
trait ScalaCodeDataSource {
  def array(values:PossibleValue*)(minItemsCount:Int=1)(implicit calc:ScoreCalculator):ArrayValueContainer =
    new DefaultArrayValueContainer(calc,values.toArray,minItemsCount)
  def obj(values:(String,Any)*)(minItemsCount:Int=1)(implicit calc:ScoreCalculator):ObjectValueContainer =
    new DefaultObjectValueContainer(calc,values.map(PossibleValue(_,Array.empty[FilterRule])).toArray, minItemsCount)
  def single(values:PossibleValue*)(isRequired:Boolean=true)(implicit calc:ScoreCalculator) : SingleValueContainer =
    new DefaultSingleValueContainer(calc,values.toArray, new HighestScoreDefaultValueSelector(),isRequired)

  def v(value:Any, filterRules: FilterRule*):PossibleValue = PossibleValue(value, filterRules)

  implicit def possibleValue(value:Any) = new {
    def relevantWhen(filterRules: FilterRule*) :PossibleValue = PossibleValue(value, filterRules)
  }

  implicit def textEquals(subject:String) = new {
    def is(value:String) = new TextMatch(subject, value)
  }
  implicit def numberEquals(subject:String) = new {
    def is(value:Double) = new NumberEquals(subject, value)
  }
}

