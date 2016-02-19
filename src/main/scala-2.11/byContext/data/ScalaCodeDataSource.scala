package byContext.data

import byContext.score.ScoreCalculator
import byContext.score.valueContainers.{ArrayValueContainer, ObjectValueContainer}
import byContext.valueContainers.RawValueContainer
// class is abstract temporarily
abstract class ScalaCodeDataSource {
  def raw(v:Any):RawValueContainer = new RawValueContainer(v)
  def array(values:Any*)(implicit calc:ScoreCalculator):ArrayValueContainer
  def obj(values:(String,Any)*):ObjectValueContainer
}
