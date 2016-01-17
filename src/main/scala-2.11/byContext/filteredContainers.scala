package byContext

trait Filtered{}

/*
case class SingleFiltered(valueProvider: ValueProvider) extends Filtered
case class ObjectFiltered(valueProvider: ValueProvider) extends Filtered
case class ArrayFiltered(valueProvider: ValueProvider) extends Filtered
*/
case class Raw(value:Any)
