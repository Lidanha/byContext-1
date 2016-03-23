package byContext.model

import byContext.writers.Writer

trait QueryExtensionFactory{def create(writer:Writer) : QueryExtension}
trait QueryExtension{ def handle : PartialFunction[Event,Unit]}
trait Event
case class ValueSelected(path: String, metadata: Map[String, Any]) extends Event