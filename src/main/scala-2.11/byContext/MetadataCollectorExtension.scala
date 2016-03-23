package byContext

import byContext.model.{ValueSelected, Event, QueryExtension, QueryExtensionFactory}
import byContext.writers.Writer

class MetadataCollectorExtensionFactory(keyToCollect:String) extends QueryExtensionFactory{
  override def create(rootWriter: Writer): QueryExtension = new QueryExtension{
    val writer = rootWriter.getObjectWriter()
    override def handle: PartialFunction[Event, Unit] = {
      case ValueSelected(path,metadata) =>
        metadata.get(keyToCollect).foreach{writer.getPropertyWriter(path).write(_)}
    }
  }
}
