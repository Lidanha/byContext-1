package rules

import byContext.rules.TextMatch
import byContext.{QueryContext, ValueRelevancy}
import org.scalatest.{FunSuite, _}

class ExactTextMatchTests extends FunSuite with Matchers{
  test("for a context with one entry with matching subject and value the result should be Relevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val")) should be (ValueRelevancy.Relevant)
  }
  test("for a context with two entries and one with matching subject and value the result should be Relevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val", "sub2" -> "val")) should be (ValueRelevancy.Relevant)
  }
  test("for a context with one entry with matching subject and none matching value the result should be NotRelevant"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub" -> "val1")) should be (ValueRelevancy.NotRelevant)
  }
  test("for a context with one entry with none matching subject the result should be Neutral"){
    TextMatch("sub"->"val")
      .evaluate(QueryContext("sub1" -> "val")) should be (ValueRelevancy.Neutral)
  }
}