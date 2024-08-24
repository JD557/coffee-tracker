package eu.joaocosta.coffee.model

import java.time.*

class HistoryTests extends munit.FunSuite {

  test("adds check ins grouped by day and ordered by time") {
    val drink = Drink("test", 100, Nil)
    val now   = OffsetDateTime.parse("2020-01-02T12:00:00+00:00")

    val checkIn1 = CheckIn(drink, now.minusDays(1), 1.0)
    val checkIn2 = CheckIn(drink, now, 2.0)
    val checkIn3 = CheckIn(drink, now.plusHours(1), 3.0)
    val checkIn4 = CheckIn(drink, now.plusDays(1), 4.0)

    val history =
      History()
        .addCheckIn(checkIn3)
        .addCheckIn(checkIn2)
        .addCheckIn(checkIn4)
        .addCheckIn(checkIn1)

    assert(
      history.checkIns ==
        Map(
          LocalDate.parse("2020-01-01") -> List(checkIn1),
          LocalDate.parse("2020-01-02") -> List(checkIn2, checkIn3),
          LocalDate.parse("2020-01-03") -> List(checkIn4)
        )
    )
  }

  test("removes check ins from the list") {
    val drink = Drink("test", 100, Nil)
    val now   = OffsetDateTime.parse("2020-01-02T12:00:00+00:00")

    val checkIn1 = CheckIn(drink, now.minusDays(1), 1.0)
    val checkIn2 = CheckIn(drink, now, 2.0)
    val checkIn3 = CheckIn(drink, now.plusHours(1), 3.0)
    val checkIn4 = CheckIn(drink, now.plusDays(1), 4.0)

    val history =
      History()
        .addCheckIn(checkIn1)
        .addCheckIn(checkIn2)
        .addCheckIn(checkIn3)

    assert(history == history.addCheckIn(checkIn4).removeCheckIn(checkIn4))
    assert(history == history.removeCheckIn(checkIn4))
  }

  test("removes duplicate check ins only once") {
    val drink = Drink("test", 100, Nil)
    val now   = OffsetDateTime.parse("2020-01-02T12:00:00+00:00")

    val checkIn1 = CheckIn(drink, now.minusDays(1), 1.0)

    val history =
      History()
        .addCheckIn(checkIn1)
        .addCheckIn(checkIn1)

    assert(History().addCheckIn(checkIn1) == history.removeCheckIn(checkIn1))
  }

  test("return the caffeine sum for a day") {
    val drink = Drink("test", 100, Nil)
    val now   = OffsetDateTime.parse("2020-01-02T12:00:00+00:00")

    val checkIn1 = CheckIn(drink, now.minusDays(1), 1.0)
    val checkIn2 = CheckIn(drink, now, 2.0)
    val checkIn3 = CheckIn(drink, now.plusHours(1), 3.0)
    val checkIn4 = CheckIn(drink, now.plusDays(1), 4.0)

    val history =
      History()
        .addCheckIn(checkIn1)
        .addCheckIn(checkIn2)
        .addCheckIn(checkIn3)
        .addCheckIn(checkIn4)

    assert(
      history.dailyCaffeine(LocalDate.parse("2020-01-01")) ==
        checkIn1.totalCaffeine
    )
    assert(
      history.dailyCaffeine(LocalDate.parse("2020-01-02")) ==
        checkIn2.totalCaffeine + checkIn3.totalCaffeine
    )
    assert(
      history.dailyCaffeine(LocalDate.parse("2020-01-03")) ==
        checkIn4.totalCaffeine
    )
    assert(
      history.dailyCaffeine(LocalDate.parse("2020-01-04")) == 0
    )
  }

}
