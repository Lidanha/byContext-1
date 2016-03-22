package byContext.rules

import byContext.model.{FilterRule, Probe, ValueRelevancy, QueryContext}

object TextMatch{
  def apply(subjectAndValue:(String,String), caseSensitive:Boolean = false) : TextMatch =
    new TextMatch(subjectAndValue._1, subjectAndValue._2, caseSensitive)
}
case class TextMatch(val subject:String, val value:String, caseSensitive:Boolean) extends FilterRule {
  override def evaluate(ctx: QueryContext, probe: Probe): Unit = {
    val valueRelevancy = ctx.getAs[String](subject)
      .fold(ValueRelevancy.Neutral){
        contextValue =>
          if(caseSensitive){
            if(contextValue == value) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant
          }else{
            if(contextValue.toLowerCase == value.toLowerCase) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant
          }
      }
    probe setRelevancy valueRelevancy
  }
}