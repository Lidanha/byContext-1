package byContext

import byContext.score.{ObjectValueContainer, ArrayValueContainer, SingleValueContainer, QueryContext}
import byContext.writers.Writer
import byContext.writers.map.MapObjectWriter
import com.typesafe.scalalogging.StrictLogging

class QueryHandler extends StrictLogging{
  def query(ctx:QueryContext, data:Map[String,Any], writer: MapObjectWriter) : Unit = process(data, writer)(ctx)

  private def process(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] =>
        obj.foreach{x=>
          val (name, value) = x
          process(value, writer.getPropertyWriter(name))
        }
      case collection : Array[Any] =>
        val collectionWriter = writer.getCollectionWriter()
        collection.foreach(value => process(value, collectionWriter))
      case container:SingleValueContainer =>
        container.get(ctx).fold(err=>{
          logger.error("error",err)
          throw err
        },value => process(value, writer))
      case container:ArrayValueContainer =>
        container.get(ctx).fold(err => {
          logger.error("error",err)
          throw err
        }, values => {
        values.foreach(value => process(value, writer.getCollectionWriter()))
      })
      case container:ObjectValueContainer => container.get(ctx).fold(err => ???, values => {
        val objectWriter = writer.getObjectWriter()
        values.foreach{x=>
          val (name, value) = x
          process(value, objectWriter.getPropertyWriter(name))
        }
      })

      case None => writer.write(None)
      case Raw(value) => writer.write(value)
      case value : String => writer.write(value)
    }
  }
}
