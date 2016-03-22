import byContext.model.{FilterRule, Probe, QueryContext}
import byContext.data.ScalaCodeDataSource
import byContext.rules._
import org.scalatest.{Matchers, WordSpecLike}

class ScalaCodeDataSourceTests extends WordSpecLike with Matchers with ScalaCodeDataSource{
  case class TestRule(id:Any) extends FilterRule {
    override def evaluate(ctx: QueryContext, probe: Probe): Unit = ???
  }

  "with ScalaCodeDataSource" must {
    "simple text match rules" in {
      "subject" is "ss" should be (TextMatch("subject","ss",false))
    }
    "one and rule" in {
      val dslRule = TestRule(1) and TestRule(2)
      dslRule should be (AndRuleContainer(TestRule(1),TestRule(2)))
    }
    "two and rules" in {
      val dslRule = TestRule(1) and TestRule(2) and TestRule(3)
      dslRule should be (AndRuleContainer(AndRuleContainer(TestRule(1),TestRule(2)),TestRule(3)))
    }
    "or -> and -> and -> not -> simple" in {
      val actualRule =
        OrRuleContainer(
          AndRuleContainer(
            AndRuleContainer(TextMatch("subj1","value1",false),
              NotRuleContainer(TextMatch("subj2","oo",false))),
            NumberGreaterThan("subj3",33)),
          NumberEquals("ss",22))

      val dslRule = ("subj1" is "value1" and "subj2".isNot("oo") and "subj3".greaterThan(33)) or("ss" is 22)

      dslRule should be (actualRule)
    }
  }
}
