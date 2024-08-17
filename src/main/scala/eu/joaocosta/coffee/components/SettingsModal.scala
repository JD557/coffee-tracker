package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import scala.concurrent.duration._

object SettingsModal extends Modal[Settings]:

  val init: Model = Model(false, Settings(), Settings())

  def view(model: Model): Html[Msg] =
    Material.dialog(
      attribute("open", model.open.toString),
      attribute("headline", "Settings"),
      onEvent("overlay-click", _ => Msg.Close)
    )(
      form(id := "settings")(
        Material.textField(
          `type` := "number",
          label  := "Caffeine half-life (hours)",
          value  := model.scratch.caffeineHalfLife.toHours.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(caffeineHalfLife = value.hours)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )(),
        Material.textField(
          `type` := "number",
          label  := "Caffeine ingestion (minutes)",
          value  := model.scratch.caffeineIngestion.toMinutes.toString,
          onInput(
            _.toIntOption
              .map(value =>
                Msg.UpdateScratch(
                  _.copy(caffeineIngestion = value.minutes)
                )
              )
              .getOrElse(Msg.NoOp)
          )
        )(),
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
        )(),
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
        )()
      ),
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
