package eu.joaocosta.coffee.model

import scala.concurrent.duration.*
import io.circe.Codec

final case class Settings(
  caffeineHalfLifeHours: Int = 5,
  caffeineIngestionMinutes: Int = 30,
  minCaffeine: Int = 100,
  maxCaffeine: Int = 400
) derives Codec:
  val caffeineHalfLife = caffeineHalfLifeHours.hours
  val caffeineIngestion = caffeineIngestionMinutes.minutes

