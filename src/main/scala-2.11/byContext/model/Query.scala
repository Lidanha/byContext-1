package byContext.model

trait Query {
  def queryParams : Map[String,Any]
  val enabledExtesions : Seq[String]
}
