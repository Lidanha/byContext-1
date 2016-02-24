package byContext.rules

import byContext.ValueRelevancy
import byContext.ValueRelevancy.ValueRelevancy
import com.typesafe.scalalogging.StrictLogging
import scala.reflect.runtime.universe._

trait VerifyType extends StrictLogging{
  protected def verify[T](rawValue:Any, ifVerified:T => ValueRelevancy)(implicit tag:TypeTag[T]) : ValueRelevancy = {

    if(!rawValue.isInstanceOf[T]){
      logger.debug(s"got ${rawValue.getClass.getSimpleName} - $rawValue only long values are supported")
      ValueRelevancy.NotRelevant
    }else{
      ifVerified(rawValue.asInstanceOf[T])
    }
  }
}
