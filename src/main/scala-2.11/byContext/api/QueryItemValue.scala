package byContext.api

trait QueryItemValue[T] {
  def getValue(v:T) : Any
}
object QueryItemValue{
  implicit object DoubleQueryItemValue extends QueryItemValue[Double]{
    override def getValue(v:Double): Any = v
  }
  implicit object IntQueryItemValue extends QueryItemValue[Int]{
    override def getValue(v:Int): Any = v.toDouble
  }
  implicit object StringQueryItemValue extends QueryItemValue[String]{
    override def getValue(v:String): Any = v
  }
}
