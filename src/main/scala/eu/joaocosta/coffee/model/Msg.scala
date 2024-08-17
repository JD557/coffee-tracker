package eu.joaocosta.coffee.model

import eu.joaocosta.coffee.components.*

enum Msg:
  case RemoveCheckIn(checkIn: CheckIn)
  case ModifyCheckInModal(msg: CheckInModal.Msg)
  case ModifySettingsModal(msg: SettingsModal.Msg)
  case NoOp
