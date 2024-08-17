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
    case Msg.ModifyCheckInModal(msg) =>
      (
        model.copy(
          settings = model.settings.closeModal,
          checkIns = CheckInModal.update(msg, model.checkIns)
        ),
        Cmd.None
      )
    case Msg.ModifySettingsModal(msg) =>
      (
        model.copy(
          settings = SettingsModal.update(msg, model.settings),
          checkIns = model.checkIns.closeModal
        ),
        Cmd.None
      )
    case Msg.NoOp =>
      (model, Cmd.None)

  def view(model: Model): Html[Msg] =
    Material.layout()(
      Material.topAppBar()(
        Material.topAppBarTitle()("Coffee Tracker"),
        div(style(Style("flex-grow" -> "1")))(),
        Material.buttonIcon(
          attribute("icon", "settings"),
          onClick(
            Msg.ModifySettingsModal(SettingsModal.Msg.Open(model.settings.data))
          )
        )()
      ),
      Material.layoutMain()(
        StatsCard.render(model.checkIns.data, model.settings.data),
        CheckInHistory.render(model.checkIns.data),
        CheckInModal
          .view(model.checkIns)
          .map(msg => Msg.ModifyCheckInModal(msg)),
        SettingsModal
          .view(model.settings)
          .map(msg => Msg.ModifySettingsModal(msg))
      ),
      Material.fab(
        attribute("icon", "add"),
        onClick(
          Msg.ModifyCheckInModal(
            CheckInModal.Msg.Open(model.checkIns.data.withNewCheckIn())
          )
        ),
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
