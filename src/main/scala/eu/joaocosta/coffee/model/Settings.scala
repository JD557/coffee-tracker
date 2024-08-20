package eu.joaocosta.coffee.model

import scala.concurrent.duration.*

import io.circe.Codec

/** Caffeine computation settings
  *
  * @param caffeineHalfLifeHours
  *   caffeine half-life, in hours
  * @param caffeineIngestionMinutes
  *   caffeine ingestion time, in minutes
  * @param minCaffeine
  *   maximum level of caffeine to have before sleep
  * @param maxCaffeine
  *   maximum recommended caffeine level
  */
final case class Settings(
    caffeineHalfLifeHours: Int = 5,
    caffeineIngestionMinutes: Int = 30,
    minCaffeine: Int = 100,
    maxCaffeine: Int = 400
) derives Codec:
  val caffeineHalfLife  = caffeineHalfLifeHours.hours
  val caffeineIngestion = caffeineIngestionMinutes.minutes
