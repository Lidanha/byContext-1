package byContext.score

trait Writer{
  def property(name:String, value:AnyRef)
}

class MapWriter(targetMap:collection.mutable.Map[String,AnyRef] = collection.mutable.Map()) extends Writer{
  override def property(name: String, value: AnyRef): Unit = {

  }
}