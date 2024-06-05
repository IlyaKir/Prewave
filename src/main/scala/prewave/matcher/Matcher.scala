package prewave.matcher

import prewave.model.AlertsMatchesResult

trait Matcher[T, V] {
  def `match`(terms: Seq[T], alerts: Seq[V]): AlertsMatchesResult
}

