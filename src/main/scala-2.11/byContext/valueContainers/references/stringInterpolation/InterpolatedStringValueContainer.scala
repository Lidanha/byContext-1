package byContext.valueContainers.stringInterpolation

import byContext.exceptions.{ByContextError, StringInterpolationFailed}
import byContext.model.QueryContext
import byContext.score.valueContainers.SingleValueContainer
import com.typesafe.scalalogging.StrictLogging

import scala.util.{Failure, Success, Try}

class InterpolatedStringValueContainer(val value:String, val substitutes:Seq[Substitute])
  extends SingleValueContainer with StrictLogging{

  override def get(ctx: QueryContext): Either[ByContextError, Any] = {
    Try{
      interpolate(ctx)
    } match {
      case Success(interpolated)=> Right(interpolated)
      case Failure(t) =>
        Left(StringInterpolationFailed(t))
    }
  }

  def interpolate(ctx: QueryContext): String = {
    logger.debug(s"iterating substitutes for value: $value")

    substitutes
      .map { sub =>
        sub.container.get(ctx) match {
          case Right(v: String) =>
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
}
