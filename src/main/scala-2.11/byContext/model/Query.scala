package byContext.model

trait Query {
  def queryParams : Map[String,Any]
  def getEnabledExtensions : Seq[String]
}
