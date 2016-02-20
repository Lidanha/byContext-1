package byContext

import byContext.score.ValueWithScore

trait DefaultValueSelector {
  def select(valuesWithScore:Iterable[ValueWithScore]) : Either[DefaultValueSelectorError,ValueWithScore]
}
class HighestScoreDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    if(valuesWithScore.isEmpty) {
      Left(EmptyValuesWithScoreProvidedError())
    }else{
      val (_, values) = valuesWithScore.foldLeft((-1,List[ValueWithScore]())){
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
      values.size match {
        case 1 => Right(values.head)
        case size if size > 1 => Left(new MultipleValuesWithSameScoreError())
      }
    }
  }
}
