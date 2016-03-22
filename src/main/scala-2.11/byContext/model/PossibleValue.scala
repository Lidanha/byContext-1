package byContext.model

case class PossibleValue(value:Any, rule:Option[FilterRule], settings:PossibleValueSettings = PossibleValueSettings())
