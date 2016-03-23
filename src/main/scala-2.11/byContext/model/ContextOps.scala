package byContext.model

trait ContextOps {
  def notify[E<:Event](e:E): Unit
}
