package eu.joaocosta.coffee.model

import eu.joaocosta.coffee.components.*

final case class Model(
    checkIns: CheckInModal.Model = CheckInModal.init,
    settings: SettingsModal.Model = SettingsModal.init,
    checkInModal: Boolean = false
)
