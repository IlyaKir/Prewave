package prewave.matcher

import prewave.model.{Alert, AlertsMatchesResult, QueryTerm, Term2AlertsMatches}

object MatcherViaRegex extends Matcher[QueryTerm, Alert] {

  def `match`(terms: Seq[QueryTerm],
              alerts: Seq[Alert]): AlertsMatchesResult = {
    // try to find alert matches for each query term
    val matches = terms.map { term =>
      if (term.keepOrder) Term2AlertsMatches(term, matchForKeepOrdered(term, alerts))
      else Term2AlertsMatches(term, matchForNotKeepOrdered(term, alerts))
    }
    AlertsMatchesResult(matches.filter(_.alerts.nonEmpty), terms, alerts)
  }

  private def matchForKeepOrdered(term: QueryTerm, alerts: Seq[Alert]): Seq[Alert] = {
    // Split by spaces and create regex where these words can be separated by any number of spaces.
    // We need this, for example, for the case when two words in term are separated by 1 space
    //  and in alert they are separated by 2 spaces.
    val regexForWords = term.text.split("\\s+").mkString("\\s+")
    val pattern = getRegex(regexForWords)

    // Check that pattern occurs in at least one alert content
    // Consider only alert contents with the same language as in term
    alerts.flatMap { alert =>
      val texts = getTextsFilteredByLanguage(term, alert)
      val isExist = texts.exists(text => pattern.findFirstIn(text).isDefined)
      if (isExist) Some(alert) else None
    }
  }

  private def matchForNotKeepOrdered(term: QueryTerm, alerts: Seq[Alert]): Seq[Alert] = {
    // Create Regex for every word from term.text
    val patterns = term.text.split("\\s+").map(getRegex).toList

    // Check that all words (all `patterns`) occur in any order in at least one alert content
    // Consider only alert contents with the same language as in term
    alerts.flatMap { alert =>
      val texts = getTextsFilteredByLanguage(term, alert)
      val isExist = texts.exists { text =>
        patterns.forall(_.findFirstIn(text).isDefined)
      }
      if (isExist) Some(alert) else None
    }
  }

  private def getTextsFilteredByLanguage(term: QueryTerm, alert: Alert) = alert.contents.collect {
    case content if term.language == content.language => content.text
  }

  // `\b` word boundary
  // `(?i)` case insensitive
  private def getRegex(str: String) = s"\\b(?i)$str\\b".r
}

