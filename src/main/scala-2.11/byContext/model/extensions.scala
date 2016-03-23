package byContext.model

trait QueryExtension{ def handle : PartialFunction[Event,Unit]}
trait Event
case class ValueSelected(path: String, metadata: Map[String, Any]) extends Event