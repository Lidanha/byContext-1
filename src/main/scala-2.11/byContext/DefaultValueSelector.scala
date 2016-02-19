package byContext

import byContext.score.ValueWithScore

trait DefaultValueSelector {
  def select(valuesWithScore:Iterable[ValueWithScore]) : Either[CouldNotSelectDefaultValueError,ValueWithScore]
}
class HighestScoreDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[CouldNotSelectDefaultValueError, ValueWithScore] = {
    val (maxScore, values) = valuesWithScore.foldLeft((-1,List[ValueWithScore]())){
      (tuple,current) =>
      val (maxScore, values) = tuple
        if(current.score > maxScore){
          (current.score, List(current))
        } else if(current.score == maxScore){
          (current.score, current :: values)
        }else{
          tuple
        }
    }
    if(values.size == 1) Right(values.head) else Left(new CouldNotSelectDefaultValueError())
  }
}
