package eu.joaocosta.coffee.model

import java.time.Instant

final case class History(checkIns: List[CheckIn] = Nil):
  def caffeineAt(time: Instant, settings: Settings): Double =
    checkIns.iterator.map(_.caffeineAt(time, settings)).sum

  def addCheckIn(checkIn: CheckIn): History =
    copy(checkIns = (checkIn :: checkIns).sortBy(_.dateTime))

  def removeCheckIn(checkIn: CheckIn): History =
    copy(checkIns = checkIns.filterNot(_ == checkIn))
