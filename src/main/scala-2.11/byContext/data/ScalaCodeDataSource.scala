package byContext.data

import byContext.model.{FilterRule, PossibleValue, PossibleValueSettings}
import byContext.rules._
import byContext.score.ScoreCalculator
import byContext.score.valueContainers._
import byContext.valueContainers.ValueRefMarker
import byContext.valueContainers.stringInterpolation.InterpolatedStringValueMarker

trait Filters{
  def filterArray(values: PossibleValue*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator) =
    new ArrayValueMarker {
      override val minResultItemsCount: Int = minItemsCount
      override val possibleValues: Array[PossibleValue] = values.toArray
    }

  /*def obj(values: (String, Any)*)(minItemsCount: Int = 1)(implicit calc: ScoreCalculator): ObjectValueContainer =
    new DefaultObjectValueContainer(calc, values.map(PossibleValue(_, Array.empty[FilterRule])).toArray, minItemsCount)*/

  def filterSingle(values: PossibleValue*)(required: Boolean = true)(implicit calc: ScoreCalculator) =
    new SingleValueMarker {
      override val possibleValues: Array[PossibleValue] = values.toArray
      override val isRequired: Boolean = required
    }

  def valueRef(p:String):ValueRefMarker = new ValueRefMarker{
    override val path: String = p
  }
  def interpolated(v:String) = new InterpolatedStringValueMarker {
    override val value: String = v
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

  trait PossibleValueSettingsBuilder extends WithRules with WithMetadata{
    def defaultValue : PossibleValueSettingsBuilder
    private[data] def buildPossibleValue : PossibleValue
  }
  trait WithRules{
    def withRules(rule:FilterRule) : PossibleValueSettingsBuilder
  }
  trait WithMetadata{
    def withMetadata(metadata:(String,Any)*): PossibleValueSettingsBuilder
  }

  implicit def possibleValueExtractor(builder:PossibleValueSettingsBuilder) : PossibleValue = builder.buildPossibleValue


  class PossibleValueBuilder(val value:Any) extends PossibleValueSettingsBuilder {
    var isDefault = false
    var rule:Option[FilterRule] = None
    var metadata:Option[Map[String,Any]] = None

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

    override def withMetadata(items: (String, Any)*): PossibleValueSettingsBuilder = {
      metadata = Some(items.toMap)
      this
    }
  }
}