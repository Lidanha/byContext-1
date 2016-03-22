package rules

import byContext.model.{Probe, ValueRelevancy}
import ValueRelevancy._
import byContext.api.QueryBuilder
import byContext.rules.TextMatch
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, _}

class ExactTextMatchTests extends FunSuite with Matchers with MockFactory with RulesTestsHelper with ContextHelper{
  test("for a context with one entry with matching subject and value the result should be Relevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Relevant).once()

    TextMatch("sub"->"val")
      .evaluate(new QueryBuilder{item("sub" -> "val")},p)
  }
  test("for a context with two entries and one with matching subject and value the result should be Relevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Relevant).once()

    TextMatch("sub"->"val")
      .evaluate(new QueryBuilder{
        item("sub" -> "val")
        item("sub2" -> "val")
      }, p)
  }
  test("for a context with one entry with matching subject and none matching value the result should be NotRelevant"){
    val p = mock[Probe]
    (p setRelevancy _).expects(NotRelevant).once()

    TextMatch("sub"->"val")
      .evaluate(new QueryBuilder{item("sub" -> "val1")},p)
  }
  test("for a context with one entry with none matching subject the result should be Neutral"){
    val p = mock[Probe]
    (p setRelevancy _).expects(Neutral).once()

    TextMatch("sub"->"val")
      .evaluate(new QueryBuilder{item("sub1" -> "val")}, p)
  }
}