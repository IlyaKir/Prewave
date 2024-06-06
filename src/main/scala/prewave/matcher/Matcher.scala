package prewave.matcher

import prewave.model.AlertsMatchesResult

// TODO Result type must depend on `T`, `V`
trait Matcher[T, V] {
  def `match`(terms: Seq[T], alerts: Seq[V]): AlertsMatchesResult
}

