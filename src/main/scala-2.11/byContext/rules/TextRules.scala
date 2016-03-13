package byContext.rules

import byContext.{FilterRule, Probe, QueryContext, ValueRelevancy}
object TextMatch{
  def apply(subjectAndValue:(String,String), caseSensitive:Boolean = false) : TextMatch =
    new TextMatch(subjectAndValue._1, subjectAndValue._2, caseSensitive)
}
class TextMatch(val subject:String, val value:String, caseSensitive:Boolean) extends FilterRule with VerifyType{
  override def evaluate(ctx: QueryContext, probe: Probe): Unit = {
    val valueRelevancy = ctx.get(subject)
      .fold(ValueRelevancy.Neutral){
        rawValue=>
          if(caseSensitive){
            if(rawValue == value) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant
          }else{
            verify[String](rawValue, v=>if(v.toLowerCase == value.toLowerCase) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
          }
      }
    probe setRelevancy valueRelevancy
  }
}