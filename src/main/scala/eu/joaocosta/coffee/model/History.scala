package eu.joaocosta.coffee.model

import java.time.{Instant, LocalDate}

import io.circe.Codec

/** A history of check-ins.
  *
  * @param currentCheckInTime
  *   instant for the check in being currently edited
  * @param checkIns
  *   ordered sequence of check ins
  */
final case class History(
    currentCheckInTime: Instant = Instant.now(),
    checkIns: List[CheckIn] = Nil
) derives Codec:

  /** The caffeine level at a certain point in time.
    *
    * @param time
    *   time instant to consider
    * @param settings
    *   caffeine level computation settings
    * @return
    *   current caffeine level (in mg)
    */
  def caffeineAt(time: Instant, settings: Settings): Double =
    checkIns.iterator.map(_.caffeineAt(time, settings)).sum

  /** The total caffeine consumed in a day.
    *
    * @param day
    *   day to consider
    * @return
    *   caffeine consumed (in mg)
    */
  def dailyCaffeine(day: LocalDate): Double =
    checkIns.filter(_.dateTime.toLocalDate == day).map(_.totalCaffeine).sum

  /** Adds a check-in to the history.
    */
  def addCheckIn(checkIn: CheckIn): History =
    copy(checkIns = (checkIn :: checkIns).sortBy(_.dateTime))

  /** Removes a check-in from the history.
    */
  def removeCheckIn(checkIn: CheckIn): History =
    copy(checkIns = checkIns.filterNot(_ == checkIn))

  /** Updates the current check-in time for a new check-in.
    */
  def withNewCheckIn(): History = copy(currentCheckInTime = Instant.now())
