package byContext.data

import byContext._
import byContext.rules._
import byContext.score.ScoreCalculator
import byContext.score.valueContainers.{ArrayValueContainer, DefaultArrayValueContainer, DefaultSingleValueContainer, SingleValueContainer}
import byContext.valueContainers.{InterpolatedStringValueContainer, Substitute, UnsafeValueRefContainer, ValueRefContainer}

trait Filters{
  def filterArray(values: PossibleValue*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ArrayValueContainer =
    new DefaultArrayValueContainer(calc, values.toArray, minItemsCount)

  /*def obj(values: (String, Any)*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ObjectValueContainer =
    new DefaultObjectValueContainer(calc, values.map(PossibleValue(_, Array.empty[FilterRule])).toArray, minItemsCount)*/

  def filterSingle(values: PossibleValue*)(isRequired: Boolean = true)(implicit calc: ScoreCalculator): SingleValueContainer =
    new DefaultSingleValueContainer(calc, values.toArray,
      new CompositeDefaultValueSelector(Seq(new HighestScoreDefaultValueSelector(),new DefaultMarkedDefaultValueSelector())), isRequired)
  def valueRef(path:String):ValueRefContainer = new UnsafeValueRefContainer(path)
  def interpolated(value:String) : SingleValueContainer= {
    val re = """<<.*>>""".r
    val subs = re
      .findAllMatchIn(value)
      .map{m=>
        val path = m.source.subSequence(m.start+2,m.end-2).toString
        val stringToReplace = m.source.subSequence(m.start,m.end).toString
        Substitute(stringToReplace,path)
      }
      .toSeq

    new InterpolatedStringValueContainer(value,subs)
  }
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
    def smallerThanOrEquals(value: Double) = new NumberSmallerThanOrEquals(subject,value)
    def greaterThanOrEquals(value: Double) = new NumberGreaterThanOrEquals(subject,value)
  }
  implicit def rulesConnector(leftRule:FilterRule) = new {
    def and(rightRule:FilterRule) : FilterRule = new AndRuleContainer(leftRule,rightRule)
    def or(rightRule:FilterRule) : FilterRule = new OrRuleContainer(leftRule,rightRule)
  }
}
trait ScalaCodeDataSource extends Filters with RulesBuilders{

  implicit def possibleValueBuilder(value:Any) = new PossibleValueBuilder(value)

  trait PossibleValueSettingsBuilder extends WithRules{
    def defaultValue : PossibleValueSettingsBuilder
    private[data] def buildPossibleValue : PossibleValue
  }
  trait WithRules{
    def withRules(rule:FilterRule) : PossibleValueSettingsBuilder
  }

  implicit def possibleValueExtractor(builder:PossibleValueSettingsBuilder) : PossibleValue = builder.buildPossibleValue


  class PossibleValueBuilder(val value:Any) extends PossibleValueSettingsBuilder {
    var isDefault = false
    var rule:Option[FilterRule] = None
    def setAs : PossibleValueSettingsBuilder = this

    override def defaultValue: PossibleValueSettingsBuilder = {
      isDefault = true
      this
    }

    override def withRules(r:FilterRule) : PossibleValueSettingsBuilder = {
      rule = Some(r)
      this
    }

    /*override def withRules(rule:FilterRule) : PossibleValue =
      PossibleValue(value,Some(rule), PossibleValueSettings(isDefault = this.isDefault))*/
    override def buildPossibleValue: PossibleValue = PossibleValue(value,rule, PossibleValueSettings(isDefault = this.isDefault))
  }
}