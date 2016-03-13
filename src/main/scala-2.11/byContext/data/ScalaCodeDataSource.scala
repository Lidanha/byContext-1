package byContext.data

import byContext.rules._
import byContext.score.ScoreCalculator
import byContext.score.valueContainers._
import byContext.{PossibleValueSettings, FilterRule, HighestScoreDefaultValueSelector, PossibleValue}

trait ScalaCodeDataSource {
  def array(values: PossibleValue*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ArrayValueContainer =
    new DefaultArrayValueContainer(calc, values.toArray, minItemsCount)

  /*def obj(values: (String, Any)*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ObjectValueContainer =
    new DefaultObjectValueContainer(calc, values.map(PossibleValue(_, Array.empty[FilterRule])).toArray, minItemsCount)*/

  def single(values: PossibleValue*)(isRequired: Boolean = true)(implicit calc: ScoreCalculator): SingleValueContainer =
    new DefaultSingleValueContainer(calc, values.toArray, new HighestScoreDefaultValueSelector(), isRequired)

  /*def v(value: Any, rule:FilterRule): PossibleValue = PossibleValue(value, rule)*/

  implicit def possibleValue(value: Any) = new {
    def relevantWhen(rule:FilterRule) : PossibleValue = PossibleValue(value, Some(rule))
  }

  implicit def tupleToRule(tuple: Tuple1[FilterRule]) = tuple._1

  implicit def textEquals(subject: String) = new {
    def is(value: String) = TextMatch(subject -> value)
    def isNot(value: String) = new NotRuleContainer(TextMatch(subject -> value))
  }

  implicit def rulesConnector(leftRule:FilterRule) = new {
    def and(rightRule:FilterRule) : FilterRule = new AndRuleContainer(leftRule,rightRule)
    def or(rightRule:FilterRule) : FilterRule = new OrRuleContainer(leftRule,rightRule)
  }
  implicit def numberEquals(subject: String) = new {
    def is(value: Double) = new NumberEquals(subject, value)
    def isNot(value: Double) = new NotRuleContainer(new NumberEquals(subject, value))
  }

  implicit def possibleValueBuilder(subject:String) = new PossibleValueBuilder(subject)

  trait PossibleValueSettingsBuilder extends WithRules{
    def defaultValue : PossibleValueSettingsBuilder
  }
  trait WithRules{
    def withRules(rule:FilterRule) : PossibleValue
  }
  class PossibleValueBuilder(val value:String) extends PossibleValueSettingsBuilder {
    var isDefault = false
    def setAs : PossibleValueSettingsBuilder = this

    override def defaultValue: PossibleValueSettingsBuilder = {
      isDefault = true
      this
    }
    override def withRules(rule:FilterRule) : PossibleValue = PossibleValue(value,Some(rule), PossibleValueSettings(isDefault = this.isDefault))
  }
}