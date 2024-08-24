package eu.joaocosta.coffee.model

import java.time.{Instant, LocalDate}

import scala.collection.immutable.SortedMap
import scala.util.Try

import io.circe.*

/** A history of check-ins.
  *
  * @param currentCheckInTime
  *   instant for the check in being currently edited
  * @param checkIns
  *   ordered sequence of check ins
  */
final case class History(
    currentCheckInTime: Instant = Instant.now(),
    checkIns: SortedMap[LocalDate, List[CheckIn]] = SortedMap.empty
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
    checkIns.valuesIterator
      .flatMap(_.iterator)
      .map(_.caffeineAt(time, settings))
      .sum

  /** The total caffeine consumed in a day.
    *
    * @param day
    *   day to consider
    * @return
    *   caffeine consumed (in mg)
    */
  def dailyCaffeine(day: LocalDate): Double =
    checkIns.get(day).iterator.flatMap(_.iterator).map(_.totalCaffeine).sum

  /** Adds a check-in to the history.
    */
  def addCheckIn(checkIn: CheckIn): History =
    copy(checkIns =
      checkIns.updatedWith(checkIn.localDate)(previous =>
        Some((checkIn :: previous.toList.flatten).sortBy(_.dateTime))
      )
    )

  /** Removes a check-in from the history.
    */
  def removeCheckIn(checkIn: CheckIn): History =
    copy(checkIns =
      checkIns.updatedWith(checkIn.localDate)(_.map(_.filterNot(_ == checkIn)))
    )

  /** Updates the current check-in time for a new check-in.
    */
  def withNewCheckIn(): History = copy(currentCheckInTime = Instant.now())

object History:
  given Codec[SortedMap[LocalDate, List[CheckIn]]] =
    val decoder: Decoder[SortedMap[LocalDate, List[CheckIn]]] =
      given KeyDecoder[LocalDate] =
        KeyDecoder.instance(str => Try(LocalDate.parse(str)).toOption)
      Decoder.decodeMap[LocalDate, List[CheckIn]].map(SortedMap.from)
    val encoder: Encoder[SortedMap[LocalDate, List[CheckIn]]] =
      given KeyEncoder[LocalDate] = KeyEncoder.instance[LocalDate](_.toString)
      Encoder
        .encodeMap[LocalDate, List[CheckIn]]
        .contramap[SortedMap[LocalDate, List[CheckIn]]](_.toMap)
    Codec.from(decoder, encoder)
