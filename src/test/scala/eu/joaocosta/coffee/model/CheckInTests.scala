package eu.joaocosta.coffee.model

import java.time.OffsetDateTime

class CheckInTests extends munit.FunSuite {

  test("computes caffeine based on the quantity") {
    val now = OffsetDateTime.now()
    val settings = Settings(
      caffeineHalfLifeHours = 1,
      caffeineIngestionMinutes = 0
    )
    val drink   = Drink("test", 100, Nil)
    val checkIn = CheckIn(drink, now, 2.0)

    assert(checkIn.totalCaffeine == 200)
    assert(checkIn.caffeineAt(now.toInstant(), settings) == 200)
  }

  test("computes caffeine at a point in time based on the half-life") {
    val now = OffsetDateTime.now()
    val settings = Settings(
      caffeineHalfLifeHours = 1,
      caffeineIngestionMinutes = 0
    )
    val drink   = Drink("test", 100, Nil)
    val checkIn = CheckIn(drink, now, 1.0)

    assert(checkIn.caffeineAt(now.toInstant(), settings) == 100)
    assert(checkIn.caffeineAt(now.plusHours(1).toInstant(), settings) == 50)
  }

  test("computes caffeine at a point in time based on the ingestion") {
    val now = OffsetDateTime.now()
    val settings = Settings(
      caffeineHalfLifeHours = 1,
      caffeineIngestionMinutes = 120
    )
    val drink   = Drink("test", 100, Nil)
    val checkIn = CheckIn(drink, now, 1.0)

    assert(checkIn.caffeineAt(now.toInstant(), settings) == 0)
    assert(checkIn.caffeineAt(now.plusHours(1).toInstant(), settings) < 50)
    assert(checkIn.caffeineAt(now.plusHours(2).toInstant(), settings) == 25)
  }

  test("ignore caffeine levels in the past and in the far future") {
    val now = OffsetDateTime.now()
    val settings = Settings(
      caffeineHalfLifeHours = 1,
      caffeineIngestionMinutes = 120
    )
    val drink   = Drink("test", 100000, Nil)
    val checkIn = CheckIn(drink, now, 1.0)

    assert(checkIn.caffeineAt(now.minusHours(1).toInstant(), settings) == 0)
    assert(checkIn.caffeineAt(now.plusHours(25).toInstant(), settings) == 0)
  }

}
