package rules

import byContext.QueryContext
import byContext.ValueRelevancy._
import byContext.rules.TextMatch
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, _}

class ExactTextMatchTests extends FunSuite with Matchers with MockFactory with RulesTestsHelper{
  test("for a context with one entry with matching subject and value the result should be Relevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val"),probeThatExpects(Relevant))
  }
  test("for a context with two entries and one with matching subject and value the result should be Relevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val", "sub2" -> "val"), probeThatExpects(Relevant))
  }
  test("for a context with one entry with matching subject and none matching value the result should be NotRelevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val1"),probeThatExpects(NotRelevant))
  }
  test("for a context with one entry with none matching subject the result should be Neutral"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub1" -> "val"), probeThatExpects(Neutral))
  }
}