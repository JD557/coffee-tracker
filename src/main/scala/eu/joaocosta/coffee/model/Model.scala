package eu.joaocosta.coffee.model

final case class Model(
    history: History = History(),
    settings: Settings = Settings(),
    checkInModal: Boolean = false,
    settingsModal: Boolean = false
)
