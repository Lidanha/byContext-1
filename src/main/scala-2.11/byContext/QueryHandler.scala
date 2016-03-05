package byContext

import byContext.score.valueContainers.{ArrayValueContainer, ObjectValueContainer, SingleValueContainer}
import byContext.writers.Writer
import com.typesafe.scalalogging.StrictLogging

trait QueryHandler{
  def query(ctx:QueryContext, data:Any, writer: Writer) : Unit
}
class RecursiveQueryHandler extends QueryHandler with StrictLogging{
  def query(ctx:QueryContext, data:Any, writer: Writer) : Unit = process(data, writer)(ctx)

  private def process(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] =>
        val objectWriter = writer.getObjectWriter()
        obj.foreach{x=>
          val (name, value) = x
          process(value, objectWriter.getPropertyWriter(name))
        }
      case collection : Array[Any] =>
        val collectionWriter = writer.getCollectionWriter()
        collection.foreach(value => process(value, collectionWriter))
      case container:SingleValueContainer =>
        container.get(ctx).fold(err=>{
          logger.error("error",err)
          throw err
        },
          value => process(value, writer))
      case container:ArrayValueContainer =>
        container.get(ctx).fold(err => {
          logger.error("error",err)
          throw err
        }, values => {
          val collectionWriter = writer.getCollectionWriter()
          values.foreach(value => process(value, collectionWriter))
      })
      case container:ObjectValueContainer => container.get(ctx).fold(err => ???, values => {
        val objectWriter = writer.getObjectWriter()
        values.foreach{x=>
          val (name, value) = x
          process(value, objectWriter.getPropertyWriter(name))
        }
      })

      case None => writer.write(None)
      case raw => writer.write(raw)
    }
  }
}
