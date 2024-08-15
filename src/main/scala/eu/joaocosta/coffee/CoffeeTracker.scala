package eu.joaocosta.coffee

import cats.effect.IO
import tyrian.Html.*
import tyrian.*

import scala.scalajs.js.annotation.*
import java.time.ZonedDateTime
import java.time.ZoneId
import java.time.Instant

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
      (model.copy(settingsModal = true, checkInModal = false), Cmd.None)
    case Msg.CloseSettingsModal =>
      (model.copy(settingsModal = false), Cmd.None)
    case Msg.NoOp => (model, Cmd.None)

  def renderDrink(drink: Drink): Html[Msg] =
    Material.dropdown()(
      Material.listItem(
        attribute("slot", "trigger"),
        attribute("icon", "add")
      )(span()(drink.name)),
      Material.menu()(
        drink.commonSizes.map((size, volume) =>
          Material.menuItem(
            onClick(
              Msg.AddCheckIn(
                CheckIn(drink, ZonedDateTime.now(ZoneId.of("UTC")), volume)
              )
            )
          )(size)
        )*
      )
    )

  def renderDrinks: Html[Msg] =
    Material.list()(Drink.defaults.map(renderDrink)*)

  def renderCheckIn(checkIn: CheckIn): Html[Msg] =
    Material.listItem(
      attribute("description", checkIn.dateTime.toLocalTime().toString)
    )(
      span()(
        s"${checkIn.drink.name} (${checkIn.quantity}mL / ${checkIn.totalCaffeine.toString}mg)"
      ),
      Material.buttonIcon(
        attribute("variant", "tonal"),
        attribute("slot", "end-icon"),
        attribute("icon", "delete"),
        onClick(Msg.RemoveCheckIn(checkIn))
      )()
    )

  def renderHistory(history: History): Html[Msg] =
    div(style(Style("padding-bottom" -> "4em")))(
      h2(style(Material.Styles.titleLarge))("History") ::
        history.checkIns
          .groupBy(_.dateTime.toLocalDate())
          .toList
          .sortBy(_._1)
          .map((localDate, checkIns) =>
            Material.card()(
              div(style(Style("padding" -> "1rem")))(
                h3(localDate.toString),
                Material.list()(checkIns.map(renderCheckIn))
              )
            )
          )
    )

  def renderStats(history: History, settings: Settings): Html[Msg] =
    val plot = CaffeinePlot.getImage(history, settings, 1024, 512)
    Material.card()(
      img(src := plot.src, style(Style("max-width" -> "100%"))),
      div(style(Style("padding" -> "1rem")))(
        h2(style(Material.Styles.titleLarge))("Overview"),
        span(
          f"Current caffeine: ${history.caffeineAt(Instant.now(), settings)}%1.1f mg"
        )
      )
    )

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
        renderStats(model.history, model.settings),
        renderHistory(model.history),
        Material.dialog(
          attribute("open", model.checkInModal.toString),
          attribute("headline", "Add Drink"),
          onEvent("overlay-click", _ => Msg.CloseCheckInModal)
        )(
          renderDrinks
        ),
        Material.dialog(
          attribute("open", model.settingsModal.toString),
          attribute("headline", "Settings"),
          onEvent("overlay-click", _ => Msg.CloseSettingsModal)
        )(
          span()(model.settings.toString),
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

final case class Model(
    history: History = History(),
    settings: Settings = Settings(),
    checkInModal: Boolean = false,
    settingsModal: Boolean = false
)

enum Msg:
  case AddCheckIn(checkIn: CheckIn)
  case RemoveCheckIn(checkIn: CheckIn)
  case OpenCheckInModal
  case CloseCheckInModal
  case OpenSettingsModal
  case CloseSettingsModal
  case NoOp
