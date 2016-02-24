package byContext.rules

import byContext.{FilterRule, QueryContext, ValueRelevancy}
import ValueRelevancy._

class TextMatch(val subject:String, val value:String, caseSensitive:Boolean = false) extends FilterRule with VerifyType{
  override def evaluate(ctx: QueryContext): ValueRelevancy =
    ctx.get(subject)
      .fold(ValueRelevancy.Neutral){
        rawValue=>
          if(caseSensitive){
            if(rawValue == value) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant
          }else{
            verify[String](rawValue, v=>if(v.toLowerCase == value.toLowerCase) ValueRelevancy.Relevant else ValueRelevancy.NotRelevant)
          }
      }
}