package eu.joaocosta.coffee.model

enum Msg:
  case AddCheckIn(checkIn: CheckIn)
  case RemoveCheckIn(checkIn: CheckIn)
  case OpenCheckInModal
  case CloseCheckInModal
  case OpenSettingsModal
  case CloseSettingsModal
  case NoOp
