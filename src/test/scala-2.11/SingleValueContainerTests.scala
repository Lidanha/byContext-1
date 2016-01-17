import byContext.score._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{EitherValues, FlatSpec, Matchers}

class SingleValueContainerTests extends FlatSpec with Matchers with MockFactory with EitherValues {
  val emptyctx = QueryContext()
  val emptyValues = Array.empty[PossibleValue]

  def calc(values: Array[ValueWithScore]): ScoreCalculator = {
    val calculator = stub[ScoreCalculator]
    (calculator.calculateScoreForRelevantValues _)
      .when(emptyctx, emptyValues)
      .returns(values)
    calculator
  }

  def single(calculator: ScoreCalculator, required: Boolean): DefaultSingleValueContainer = {
    new DefaultSingleValueContainer(calculator, emptyValues, required)
  }

  "SingleValueContainer" should "return a right with a single value when score calculator returns a songle result" in {
    val calculator = calc(Array(ValueWithScore("a", 1)))

    single(calculator, true) get (emptyctx) should be(Right("a"))
  }
  it should "return a left with MultipleValuesNotAllowedError when score calculator returns multiple values" in {
    val calculator = calc(Array(ValueWithScore("a", 1),ValueWithScore("a", 1)))
    single(calculator, true) get emptyctx should be (Left( new MultipleValuesNotAllowedError()))
  }
  it should "return a left with RequiredValueMissingError when score calculator returns no values and a value is required" in {
    val calculator = calc(Array.empty[ValueWithScore])
    single(calculator, true) get emptyctx should be (Left( new RequiredValueMissingError()))
  }
  it should "return a right with None when score calculator returns no values and a value is not required" in {
    val calculator = calc(Array.empty[ValueWithScore])
    single(calculator, false) get emptyctx should be (Right(None))
  }
}
