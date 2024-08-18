package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*

object SettingsModal extends Modal[Settings]:

  val localStorageKey = "settings"
  val defaultValue = Settings()

  def view(model: Model): Html[Msg] =
    Material.dialog(
      attribute("open", model.open.toString),
      attribute("headline", "Settings"),
      onEvent("overlay-click", _ => Msg.Close)
    )(
      Material.list(id := "settings")(
        Material.listItem(attribute("nonclickable", "true"))(
        Material.textField(
          `type` := "number",
          label  := "Caffeine half-life (hours)",
          value  := model.scratch.caffeineHalfLifeHours.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(caffeineHalfLifeHours = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )()),
        Material.listItem(attribute("nonclickable", "true"))(
        Material.textField(
          `type` := "number",
          label  := "Caffeine ingestion (minutes)",
          value  := model.scratch.caffeineIngestionMinutes.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(caffeineIngestionMinutes = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )()),
        Material.listItem(attribute("nonclickable", "true"))(
        Material.textField(
          `type` := "number",
          label  := "Max caffeine before sleep (mg)",
          value  := model.scratch.minCaffeine.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(minCaffeine = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )()),
        Material.listItem(attribute("nonclickable", "true"))(
        Material.textField(
          `type` := "number",
          label  := "Max caffeine (mg)",
          value  := model.scratch.maxCaffeine.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(maxCaffeine = value)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )())
      ),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "text"),
        onClick(Msg.UpdateScratch(_ => Settings()))
      )("Restore defaults"),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "text"),
        onClick(Msg.Close)
      )("Cancel"),
      Material.button(
        attribute("slot", "action"),
        attribute("variant", "tonal"),
        onClick(Msg.Save)
      )("Save")
    )
