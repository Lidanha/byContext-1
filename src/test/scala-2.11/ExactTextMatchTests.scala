import byContext.score.{ValueRelevancy, QueryContext}
import byContext.score.rules.ExactTextMatchRule
import org.scalatest.{FunSuite, _}

class ExactTextMatchTests extends FunSuite with Matchers{
  test("for a context with one entry with matching subject and value the result should be Relevant"){
    new ExactTextMatchRule("sub", "val")
      .evaluate(QueryContext(Map("sub" -> "val"))) should be (ValueRelevancy.Relevant)
  }
  test("for a context with two entries and one with matching subject and value the result should be Relevant"){
    new ExactTextMatchRule("sub", "val")
      .evaluate(QueryContext(Map("sub" -> "val", "sub2" -> "val"))) should be (ValueRelevancy.Relevant)
  }
  test("for a context with one entry with matching subject and none matching value the result should be NotRelevant"){
    new ExactTextMatchRule("sub", "val")
      .evaluate(QueryContext(Map("sub" -> "val1"))) should be (ValueRelevancy.NotRelevant)
  }
  test("for a context with one entry with none matching subject the result should be Neutral"){
    new ExactTextMatchRule("sub", "val")
      .evaluate(QueryContext(Map("sub1" -> "val"))) should be (ValueRelevancy.Neutral)
  }
}