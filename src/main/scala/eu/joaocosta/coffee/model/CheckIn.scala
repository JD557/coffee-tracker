package eu.joaocosta.coffee.model

import java.time.*

import io.circe.Codec

/** A check-in represents a consumption of a drink.
  *
  * @param drink
  *   drink consumed
  * @param dateTime
  *   when was the drink consumed
  * @param quantity
  *   drink quantity (in mL)
  */
final case class CheckIn(
    drink: Drink,
    dateTime: OffsetDateTime,
    quantity: Double
) derives Codec:

  /** The local date of this check in */
  def localDate: LocalDate = dateTime.toLocalDate()

  /** The total caffeine ingested due to this check-in.
    */
  def totalCaffeine: Double = drink.caffeinePerMl * quantity

  /** The caffeine level from this check-in at a certain point in time.
    *
    * @param time
    *   time instant to consider
    * @param settings
    *   caffeine level computation settings
    * @return
    *   current caffeine level (in mg)
    */
  def caffeineAt(time: Instant, settings: Settings): Double =
    val t = (time.getEpochSecond() - dateTime.toEpochSecond()).toDouble
    if (t < 0 || t >= 24 * 60 * 60) 0.0
    else
      val ingestion =
        if (settings.caffeineIngestion.toSeconds == 0) 1.0
        else math.min(t / settings.caffeineIngestion.toSeconds, 1.0)
      val absorbedCaffeine =
        ingestion * totalCaffeine
      absorbedCaffeine * math.pow(2.0, -t / settings.caffeineHalfLife.toSeconds)
