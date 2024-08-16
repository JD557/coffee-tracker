package eu.joaocosta.coffee

import cats.effect.IO
import tyrian.Html.*
import tyrian.*

import scala.scalajs.js.annotation.*

import eu.joaocosta.coffee.model.*
import eu.joaocosta.coffee.components.*

@JSExportTopLevel("TyrianApp")
object CoffeeTracker extends TyrianIOApp[Msg, Model]:

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model(), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.AddCheckIn(checkIn) =>
      (
        model.copy(
          history = model.history.addCheckIn(checkIn),
          checkInModal = false
        ),
        Cmd.None
      )
    case Msg.RemoveCheckIn(checkIn) =>
      (model.copy(history = model.history.removeCheckIn(checkIn)), Cmd.None)
    case Msg.OpenCheckInModal =>
      (model.copy(checkInModal = true, settingsModal = false), Cmd.None)
    case Msg.CloseCheckInModal =>
      (model.copy(checkInModal = false), Cmd.None)
    case Msg.OpenSettingsModal =>
      (model.copy(settingsScratch = model.settings, settingsModal = true, checkInModal = false), Cmd.None)
    case Msg.CloseSettingsModal =>
      (model.copy(settingsScratch = model.settings, settingsModal = false), Cmd.None)
    case Msg.SaveSettings =>
      (model.copy(settings = model.settingsScratch, settingsModal = false), Cmd.None)
    case Msg.UpdateSettingsScratch(settings) =>
      (model.copy(settingsScratch = settings), Cmd.None)
    case Msg.NoOp =>
      (model, Cmd.None)


  def view(model: Model): Html[Msg] =
    Material.layout()(
      Material.topAppBar()(
        Material.topAppBarTitle()("Coffee Tracker"),
        div(style(Style("flex-grow" -> "1")))(),
        Material.buttonIcon(
          attribute("icon", "settings"),
          onClick(Msg.OpenSettingsModal)
        )()
      ),
      Material.layoutMain()(
        StatsCard.render(model.history, model.settings),
        CheckInHistory.render(model.history),
        CheckInModal.render(model.checkInModal),
        SettingsModal.render(model.settingsModal, model.settingsScratch)
      ),
      Material.fab(
        attribute("icon", "add"),
        onClick(Msg.OpenCheckInModal),
        style(
          Style(
            "position" -> "fixed",
            "bottom"   -> "1em",
            "right"    -> "1em"
          )
        )
      )()
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

