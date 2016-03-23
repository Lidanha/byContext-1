package rules

import byContext.model.Probe
import byContext.model.ValueRelevancy._
import byContext.rules.TextMatch
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, _}

class ExactTextMatchTests extends FunSuite with Matchers with MockFactory with RulesTestsHelper 
  with Creators{
  test("for a context with one entry with matching subject and value the result should be Relevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Relevant).once()

    TextMatch("sub"->"val")
      .evaluate(ctx("sub" -> "val"),p)
  }
  test("for a context with two entries and one with matching subject and value the result should be Relevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Relevant).once()

    TextMatch("sub"->"val")
      .evaluate(ctx("sub" -> "val","sub2" -> "val"), p)
  }
  test("for a context with one entry with matching subject and none matching value the result should be NotRelevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(NotRelevant).once()

    TextMatch("sub"->"val")
      .evaluate(ctx("sub" -> "val1"),p)
  }
  test("for a context with one entry with none matching subject the result should be Neutral"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Neutral).once()

    TextMatch("sub"->"val")
      .evaluate(ctx("sub1" -> "val"), p)
  }
}