import byContext.score._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, FunSuite, Matchers}

class MultiplePossibleValuesValueProviderTests extends FunSuite with Matchers with MockFactory with EitherValues{
  val emptyctx = QueryContext()
  val emptyValues = Array.empty[PossibleValue]

  test("for a single ValueWithScore, MultiplePossibleValuesValueProvider should return that result independent from all settings - 1"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",1)))

    new MultiplePossibleValuesValueProvider(calculator, emptyValues,true, true)
      .get(emptyctx) should be (Right("a"))

  }
  test("for a single ValueWithScore, MultiplePossibleValuesValueProvider should return that result independent from all settings - 2"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",1)))

    new MultiplePossibleValuesValueProvider(calculator, emptyValues,false, true)
      .get(emptyctx) should be (Right("a"))
  }
  test("for a single ValueWithScore, MultiplePossibleValuesValueProvider should return that result independent from all settings - 3"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",1)))

    new MultiplePossibleValuesValueProvider(calculator, emptyValues,false, false)
      .get(emptyctx) should be (Right("a"))
  }
  test("for a single ValueWithScore, MultiplePossibleValuesValueProvider should return that result independent from all settings - 4"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",4)))

    new MultiplePossibleValuesValueProvider(calculator, emptyValues,true, false)
      .get(emptyctx) should be (Right("a"))
  }
  test("for multiple ValueWithScore and allow multiple, MultiplePossibleValuesValueProvider should return an ArrayValue with all values"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",4), ValueWithScore("b",1)))

    val res = new MultiplePossibleValuesValueProvider(calculator, emptyValues,true, true).get(emptyctx)

    res.isRight should be (true)
    res.right.get.asInstanceOf[Array[Any]] should have size 2
    res.right.get.asInstanceOf[Array[Any]] should contain ("a")
    res.right.get.asInstanceOf[Array[Any]] should contain ("b")
  }
  test("for multiple ValueWithScore and not allow multiple, MultiplePossibleValuesValueProvider should return a failure[MultipleValuesNotAllowedException]"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array(ValueWithScore("a",4), ValueWithScore("b",1)))

    val res = new MultiplePossibleValuesValueProvider(calculator, emptyValues,false, true).get(emptyctx)

    res.left.get should be (MultipleValuesNotAllowedError())
  }
  test("for empty ValueWithScore and required = true, MultiplePossibleValuesValueProvider should return a failure[RequiredValueMissingException]"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array.empty[ValueWithScore])

    val res = new MultiplePossibleValuesValueProvider(calculator, emptyValues,false, true).get(emptyctx)

    res.left.get should be (RequiredValueMissingError())
  }
  test("for empty ValueWithScore and required = false, MultiplePossibleValuesValueProvider should return None"){
    val calculator = stub[ScoreCalculator]

    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx,emptyValues)
      .returns(Array.empty[ValueWithScore])

    val res = new MultiplePossibleValuesValueProvider(calculator, emptyValues,false, false).get(emptyctx)

    res shouldBe (Right(None))
  }
}
