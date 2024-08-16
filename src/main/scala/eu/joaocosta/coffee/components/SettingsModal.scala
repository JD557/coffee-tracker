package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import scala.concurrent.duration._

object SettingsModal:
  def render(open: Boolean, settings: Settings): Html[Msg] =
    Material.dialog(
      attribute("open", open.toString),
      attribute("headline", "Settings"),
      onEvent("overlay-click", _ => Msg.CloseSettingsModal)
    )(
      form(id := "settings")(
        Material.textField(
          `type` := "number",
          label  := "Caffeine half-life (hours)",
          value  := settings.caffeineHalfLife.toHours.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateSettingsScratch(
                  settings.copy(caffeineHalfLife = value.hours)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )(),
        Material.textField(
          `type` := "number",
          label  := "Caffeine ingestion (minutes)",
          value  := settings.caffeineIngestion.toMinutes.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateSettingsScratch(
                  settings.copy(caffeineIngestion = value.minutes)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )(),
        Material.textField(
          `type` := "number",
          label  := "Max caffeine before sleep (mg)",
          value  := settings.minCaffeine.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateSettingsScratch(
                  settings.copy(minCaffeine = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )(),
        Material.textField(
          `type` := "number",
          label  := "Max caffeine (mg)",
          value  := settings.maxCaffeine.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateSettingsScratch(
                  settings.copy(maxCaffeine = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )()
      ),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "text"),
        onClick(Msg.CloseSettingsModal)
      )("Cancel"),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "tonal"),
        onClick(Msg.SaveSettings)
      )("Save")
    )
