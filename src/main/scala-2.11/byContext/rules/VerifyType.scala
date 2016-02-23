package byContext.rules

import byContext.ValueRelevancy
import byContext.ValueRelevancy.ValueRelevancy
import com.typesafe.scalalogging.StrictLogging

trait VerifyType[T] extends StrictLogging{
  protected def verify(rawValue:Any, ifVerified:T => ValueRelevancy) : ValueRelevancy = {

    if(!rawValue.isInstanceOf[T]){
      logger.debug(s"got ${rawValue.getClass.getSimpleName} - $rawValue only long values are supported")
      ValueRelevancy.NotRelevant
    }else{
      ifVerified(rawValue.asInstanceOf[T])
    }
  }
}
