package byContext

import byContext.score.ValueWithScore

import scala.collection.mutable.ListBuffer

trait DefaultValueSelector {
  def select(valuesWithScore:Iterable[ValueWithScore]) : Either[DefaultValueSelectorError,ValueWithScore]
}

class CompositeDefaultValueSelector(selectors:Iterable[DefaultValueSelector]) extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    val errors = ListBuffer[DefaultValueSelectorError]()
    var res: Option[ValueWithScore] = None

    selectors.toStream.takeWhile(_ => res.isEmpty).foreach {
      selector => selector.select(valuesWithScore).fold(errors += _, v => res = Some(v))
    }
    if (res.isDefined) Right(res.get) else Left(DefaultValueSelectorAggregateErrors(errors.toArray))
  }
}

class HighestScoreDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    if(valuesWithScore.isEmpty) {
      Left(EmptyValuesWithScoreProvidedError())
    }else{
      val valuesWithMaxScore = extractValuesWithMacScore(valuesWithScore)
      valuesWithMaxScore.size match {
        case 1 => Right(valuesWithMaxScore.head)
        case size if size > 1 => Left(new MultipleValuesWithSameScoreError(valuesWithScore.toArray))
      }
    }
  }

  def extractValuesWithMacScore(valuesWithScore: Iterable[ValueWithScore]): Iterable[ValueWithScore] = {
    val (_, valuesWithMaxScore) = valuesWithScore.foldLeft((-1, List[ValueWithScore]())) {
      (tuple, current) =>
        val (maxScore, values) = tuple
        if (current.score > maxScore) {
          (current.score, List(current))
        } else if (current.score == maxScore) {
          (current.score, current :: values)
        } else {
          tuple
        }
    }
    valuesWithMaxScore
  }
}

class DefaultMarkedDefaultValueSelector extends DefaultValueSelector {
  override def select(valuesWithScore: Iterable[ValueWithScore]): Either[DefaultValueSelectorError, ValueWithScore] = {
    val defaultValues = valuesWithScore.collect {
      case v@ValueWithScore(_, _, PossibleValueSettings(true)) => v
    }.toArray

    defaultValues.size match {
      case 0 => Left(NoValuesMarkedAsDefaultError(defaultValues))
      case 1 => Right(defaultValues.head)
      case size if size > 1 => Left(MultipleValuesMarkedAsDefaultError(defaultValues))
    }
  }
}