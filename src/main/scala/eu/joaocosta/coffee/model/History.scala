package eu.joaocosta.coffee.model

import java.time.Instant
import io.circe.Codec

final case class History(
    currentCheckInTime: Instant = Instant.now(),
    checkIns: List[CheckIn] = Nil
) derives Codec:
  def caffeineAt(time: Instant, settings: Settings): Double =
    checkIns.iterator.map(_.caffeineAt(time, settings)).sum

  def addCheckIn(checkIn: CheckIn): History =
    copy(checkIns = (checkIn :: checkIns).sortBy(_.dateTime))

  def removeCheckIn(checkIn: CheckIn): History =
    copy(checkIns = checkIns.filterNot(_ == checkIn))

  def withNewCheckIn(): History = copy(currentCheckInTime = Instant.now())
