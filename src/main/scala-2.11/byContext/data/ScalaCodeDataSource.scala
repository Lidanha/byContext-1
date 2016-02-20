package byContext.data

import byContext.rules.ExactTextMatchRule
import byContext.score.ScoreCalculator
import byContext.score.valueContainers._
import byContext.{HighestScoreDefaultValueSelector, FilterRule, PossibleValue}
trait ScalaCodeDataSource {
  def array(values:Any*)(minItemsCount:Int=1)(implicit calc:ScoreCalculator):ArrayValueContainer =
    new DefaultArrayValueContainer(calc,values.map(PossibleValue(_,Array.empty[FilterRule])).toArray,minItemsCount)
  def obj(values:(String,Any)*)(minItemsCount:Int=1)(implicit calc:ScoreCalculator):ObjectValueContainer =
    new DefaultObjectValueContainer(calc,values.map(PossibleValue(_,Array.empty[FilterRule])).toArray, minItemsCount)
  def single(values:PossibleValue*)(isRequired:Boolean=true)(implicit calc:ScoreCalculator) : SingleValueContainer =
    new DefaultSingleValueContainer(calc,values.toArray, new HighestScoreDefaultValueSelector(),isRequired)

  def v(value:Any, filterRules: FilterRule*):PossibleValue = PossibleValue(value, filterRules)

  implicit def textEquals(subject:String) = new {
    def is(value:String) = new ExactTextMatchRule(subject, value)
  }
}
