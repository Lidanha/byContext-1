package byContext.valueContainers

import byContext._
import byContext.score.valueContainers.SingleValueContainer

import scala.util.{Failure, Success, Try}

case class Substitute(key:String, path:String)

class InterpolatedStringValueContainer(value:String, substitutes:Seq[Substitute])
  extends SingleValueContainer with Extenstion{

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
    substitutes
      .map { sub =>
        dataSetHandler.get(sub.path, ctx) match {
          case v: String => (sub.key, v)
          case unsupported => throw new scala.RuntimeException(s"un supported interpolated value: ${unsupported.toString}")
        }
      }
      .foldLeft(value) {
        (current, kv) =>
          val (key, newValue) = kv
          current.replace(s"[[${key}]]", newValue)
      }
  }

  override def init(handler: DataSetHandler): Unit = dataSetHandler = handler
}
