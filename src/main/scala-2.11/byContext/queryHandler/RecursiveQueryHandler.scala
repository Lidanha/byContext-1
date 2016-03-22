package byContext.queryHandler

import byContext.model.QueryContext
import byContext.score.valueContainers.{ObjectValueContainer, ArrayValueContainer, SingleValueContainer}
import byContext.writers.Writer
import com.typesafe.scalalogging.StrictLogging

class RecursiveQueryHandler extends QueryHandler with StrictLogging{
  def query(ctx:QueryContext, data:Any, writer: Writer) : Unit = process(data, writer)(ctx)

  private def process(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] => handleRawObject(writer, obj)
      case collection : Array[Any] => handleArray(writer, collection)
      case container:SingleValueContainer => handleSingleValueContainer(writer, container)
      case container:ArrayValueContainer => handleArrayValueContainer(container,writer)
      case container:ObjectValueContainer => handleObjectValueContainer(container,writer)
      case raw => writer.write(raw)
    }
  }

  private def handleRawObject(writer: Writer, obj: Map[String, Any])(implicit ctx:QueryContext) : Unit = {
    val objectWriter = writer.getObjectWriter()
    obj.foreach { x =>
      val (name, value) = x
      process(value, objectWriter.getPropertyWriter(name))
    }
  }

  private def handleArray(writer: Writer, collection: Array[Any])(implicit ctx:QueryContext) : Unit = {
    val collectionWriter = writer.getCollectionWriter()
    collection.foreach(value => process(value, collectionWriter))
  }

  private def handleSingleValueContainer(writer: Writer, container: SingleValueContainer)(implicit ctx:QueryContext) : Unit = {
    container.get(ctx).fold(err => {
      logger.error(s"failed processing query with context: ${ctx.toString()}, ${err.toString}", err)
      throw err
    },
      value => process(value, writer))
  }

  private def handleObjectValueContainer(container: ObjectValueContainer, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    container.get(ctx).fold(err => ???, values => {
      val objectWriter = writer.getObjectWriter()
      values.foreach { x =>
        val (name, value) = x
        process(value, objectWriter.getPropertyWriter(name))
      }
    })
  }

  private def handleArrayValueContainer(container: ArrayValueContainer, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    container.get(ctx).fold(err => {
      logger.error("error", err)
      throw err
    }, values => {
      val collectionWriter = writer.getCollectionWriter()
      values.foreach(value => process(value, collectionWriter))
    })
  }
}