package eu.joaocosta.coffee.model

import java.time.*
import io.circe.Codec

final case class CheckIn(
    drink: Drink,
    dateTime: OffsetDateTime,
    quantity: Double
) derives Codec:
  def totalCaffeine: Double = drink.caffeinePerMl * quantity

  def caffeineAt(time: Instant, settings: Settings): Double =
    val t = (time.getEpochSecond() - dateTime.toEpochSecond()).toDouble
    if (t <= 0 || t >= 24 * 60 * 60) 0.0
    else
      val absorbedCaffeine =
        math.min(t / settings.caffeineIngestion.toSeconds, 1.0) * totalCaffeine
      absorbedCaffeine * math.pow(2.0, -t / settings.caffeineHalfLife.toSeconds)
