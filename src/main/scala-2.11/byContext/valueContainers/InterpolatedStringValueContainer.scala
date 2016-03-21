package byContext.valueContainers

import byContext._
import byContext.score.valueContainers.SingleValueContainer
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}

case class Substitute(stringToReplace:String, path:String)

class InterpolatedStringValueContainer(value:String, substitutes:Seq[Substitute])
  extends SingleValueContainer with DataSetHandlerExtension with StrictLogging{

  var dataSetHandler: DataSetHandler = _

  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    Try{
      interpolate(ctx)
    } match {
      case Success(interpolated)=> Right(interpolated)
      case Failure(t) => Left(StringInterpolationFailed(t.getMessage))
    }
  }

  def interpolate(ctx: QueryContext): String = {
    logger.debug(s"iterating substitutes for value: $value")
    substitutes
      .map { sub =>
        logger.debug(s"finding path ${sub.path} to interpolate in value: ${value}")
        dataSetHandler.get(sub.path, ctx) match {
          case v: String =>
            logger.debug(s"found value ${v} for path ${sub.path} to interpolate in value: ${value}")
            (sub, v)
          case unsupported =>
            logger.warn(s"found unsupported value for interpolation: ${unsupported.toString}, throwing exception")
            throw new scala.RuntimeException(s"un supported interpolated value: ${unsupported.toString}")
        }
      }
      .foldLeft(value) {
        (current, kv) =>
          val (sub, newValue) = kv
          logger.debug(s"replacing ${sub.stringToReplace} with $newValue")
          current.replace(s"${sub.stringToReplace}", newValue)
      }
  }

  override def init(handler: DataSetHandler): Unit = dataSetHandler = handler
}
