package byContext.data

import byContext.rules._
import byContext.score.ScoreCalculator
import byContext.score.valueContainers.{ArrayValueContainer, DefaultArrayValueContainer, DefaultSingleValueContainer, SingleValueContainer}
import byContext._

trait Filters{
  def filterArray(values: PossibleValue*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ArrayValueContainer =
    new DefaultArrayValueContainer(calc, values.toArray, minItemsCount)

  /*def obj(values: (String, Any)*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ObjectValueContainer =
    new DefaultObjectValueContainer(calc, values.map(PossibleValue(_, Array.empty[FilterRule])).toArray, minItemsCount)*/

  def filterSingle(values: PossibleValue*)(isRequired: Boolean = true)(implicit calc: ScoreCalculator): SingleValueContainer =
    new DefaultSingleValueContainer(calc, values.toArray,
      new CompositeDefaultValueSelector(Seq(new HighestScoreDefaultValueSelector(),new DefaultMarkedDefaultValueSelector())), isRequired)
}

trait RulesBuilders{
  implicit def possibleValue(value: Any) = new {
    def relevantWhen(rule:FilterRule) : PossibleValue = PossibleValue(value, Some(rule))
  }

  implicit def tupleToRule(tuple: Tuple1[FilterRule]) = tuple._1

  implicit def textRules(subject: String) = new {
    def is(value: String) = TextMatch(subject -> value)
    def isNot(value: String) = new NotRuleContainer(TextMatch(subject -> value))
  }
  implicit def numberRules(subject: String) = new {
    def is(value: Double) = new NumberEquals(subject, value)
    def isNot(value: Double) = new NotRuleContainer(new NumberEquals(subject, value))
    def greaterThan(value: Double) = new NumberGreaterThan(subject,value)
  }
  implicit def rulesConnector(leftRule:FilterRule) = new {
    def and(rightRule:FilterRule) : FilterRule = new AndRuleContainer(leftRule,rightRule)
    def or(rightRule:FilterRule) : FilterRule = new OrRuleContainer(leftRule,rightRule)
  }
}
trait ScalaCodeDataSource extends Filters with RulesBuilders{

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