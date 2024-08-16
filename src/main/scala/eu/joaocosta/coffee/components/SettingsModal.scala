package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*

object SettingsModal:
  def render(open: Boolean, settings: Settings): Html[Msg] =
    Material.dialog(
      attribute("open", open.toString),
      attribute("headline", "Settings"),
      onEvent("overlay-click", _ => Msg.CloseSettingsModal)
    )(
      span()(settings.toString),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "text"),
        onClick(Msg.CloseSettingsModal)
      )("Cancel"),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "tonal"),
        onClick(Msg.CloseSettingsModal)
      )("Save")
    )
