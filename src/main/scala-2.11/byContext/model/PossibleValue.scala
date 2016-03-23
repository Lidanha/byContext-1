package byContext.model

case class PossibleValue(value:Any, rule:Option[FilterRule],
                         settings:PossibleValueSettings = PossibleValueSettings(),
                         metadata:Map[String,Any]=Map.empty[String,Any])
