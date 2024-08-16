package eu.joaocosta.coffee.model

import scala.concurrent.duration.*

final case class Settings(
  caffeineHalfLife: FiniteDuration = 5.hours,
  caffeineIngestion: FiniteDuration = 30.minutes,
  minCaffeine: Int = 100,
  maxCaffeine: Int = 400
)
