package byContext.defaultValueSelection

import byContext.exceptions.{DefaultValueSelectorAggregateErrors, DefaultValueSelectorError}
import byContext.score.ValueWithScore

import scala.collection.mutable.ListBuffer

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
