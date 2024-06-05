package prewave.matcher

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import prewave.JsonGetter
import prewave.model.{Alert, QueryTerm}

class MatcherViaRegexSpec extends AnyFlatSpec with Matchers {
  private val keepOrderedPath = "src/test/files/keepOrdered"
  private val notKeepOrderedPath = "src/test/files/notKeepOrdered"

  private lazy val koTerm = JsonGetter.get[QueryTerm](s"$keepOrderedPath/term.json")
  private lazy val nkoTerm = JsonGetter.get[QueryTerm](s"$notKeepOrderedPath/term.json")

  "keepOrdered term" should "match" in {
    val alerts = JsonGetter.get[Alert](s"$keepOrderedPath/alertsMatch.json")
    val result = MatcherViaRegex.`match`(koTerm, alerts)

    result.matches.head.alerts.length shouldBe alerts.length
  }

  it should "not match" in {
    val alerts = JsonGetter.get[Alert](s"$keepOrderedPath/alertsNotMatch.json")
    val result = MatcherViaRegex.`match`(koTerm, alerts)

    result.matches.length shouldBe 0
  }

  "not keepOrdered term" should "match" in {
    val alerts = JsonGetter.get[Alert](s"$notKeepOrderedPath/alertsMatch.json")
    val result = MatcherViaRegex.`match`(nkoTerm, alerts)

    result.matches.head.alerts.length shouldBe alerts.length
  }

  it should "not match" in {
    val alerts = JsonGetter.get[Alert](s"$notKeepOrderedPath/alertsNotMatch.json")
    val result = MatcherViaRegex.`match`(nkoTerm, alerts)

    result.matches.length shouldBe 0
  }
}
