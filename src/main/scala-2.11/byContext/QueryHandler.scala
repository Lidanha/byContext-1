package byContext

import byContext.score.QueryContext


class QueryHandler{
  def query(ctx:QueryContext, data:Map[String,Any], writer: ObjectWriter) : Unit = process(data, writer)(ctx)

  private def process(data:Any, writer: Writer)(implicit ctx:QueryContext) : Unit = {
    data match {
      case obj:Map[String,Any] => obj.foreach{x=>
        val (name, value) = x
        process(value, writer.getPropertyWriter(name))
      }
      case collection : Array[Any] =>
        val collectionWriter = writer.getCollectionWriter()
        collection.foreach(value => process(value, collectionWriter))
      case SingleFiltered(provider) =>
        provider.get(ctx).fold(err => ???,value=>process(value, writer))
      case ArrayFiltered(provider) => provider.get(ctx).fold(err => ???, value => {
        val filteredArray = value.asInstanceOf[Array[Any]]
        val collectionWriter = writer.getCollectionWriter()
        filteredArray.foreach(value => process(value, collectionWriter))
      })
      case ObjectFiltered(provider) => provider.get(ctx).fold(err => ???, value => {
        val filteredArray = value.asInstanceOf[Array[(String,Any)]]
        val objectWriter = writer.getObjectWriter()
        filteredArray.foreach{x=>
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
