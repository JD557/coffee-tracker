package eu.joaocosta.coffee

import cats.effect.IO
import tyrian.Html.*
import tyrian.*

import scala.scalajs.js.annotation.*
import java.time.ZonedDateTime
import java.time.ZoneId

@JSExportTopLevel("TyrianApp")
object CoffeeTracker extends TyrianIOApp[Msg, Model]:

  def router: Location => Msg =
    Routing.none(Msg.NoOp)

  def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    (Model(), Cmd.None)

  def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.AddCheckIn(checkIn) =>
      (model.copy(history = model.history.addCheckIn(checkIn)), Cmd.None)
    case Msg.RemoveCheckIn(checkIn) =>
      (model.copy(history = model.history.removeCheckIn(checkIn)), Cmd.None)
    case Msg.NoOp => (model, Cmd.None)

  def renderDrink(drink: Drink): Html[Msg] =
    tr(
      td(drink.name + " "),
      td(
        div(role := "group", `class` := "btn-group btn-group-block")(
          drink.commonSizes.map(size =>
            button(
              `class` := "btn",
              onClick(
                Msg.AddCheckIn(
                  CheckIn(drink, ZonedDateTime.now(ZoneId.of("UTC")), size)
                )
              )
            )(s"${size}mL")
          )*
        )
      )
    )

  def renderDrinks: Html[Msg] =
    table(`class` := "table")(
      (tr(
        th("Drink Name"),
        th("Sizes")
      ) :: Drink.defaults.map(renderDrink))*
    )

  def renderCheckIn(checkIn: CheckIn): Html[Msg] =
    tr(
      td(checkIn.dateTime.toString),
      td(checkIn.drink.name),
      td(s"${checkIn.quantity}mL"),
      td(s"${checkIn.totalCaffeine.toString}g"),
      td(button(`class` := "btn", onClick(Msg.RemoveCheckIn(checkIn)))("Remove"))
    )

  def renderHistory(history: History): Html[Msg] =
    table(`class` := "table")(
      (tr(
        th("Date"),
        th("Name"),
        th("Quantity"),
        th("Total Caffeine"),
        th("Remove")
      ) :: history.checkIns.map(renderCheckIn))*
    )

  def renderStats(history: History, settings: Settings): Html[Msg] =
    val plot = CaffeinePlot.getImage(history, settings, 1024, 512)
    div(`class` := "card")(
      div(`class` := "card-header")(div(`class` := "card-title h5")("Stats")),
      div(`class` := "card-image")(img(`class` := "img-responsive", src := plot.src))
    )

  def view(model: Model): Html[Msg] =
    div(`class` := "container")(
      div(`class` := "columns")(
        div(`class` := "column col-12")(
          h2("Stats:"),
          renderStats(model.history, model.settings),
          h2("Add Drink:"),
          renderDrinks,
          h2("History:"),
          renderHistory(model.history)
        )
      )
    )

  def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

final case class Model(
    history: History = History(),
    settings: Settings = Settings()
)

enum Msg:
  case AddCheckIn(checkIn: CheckIn)
  case RemoveCheckIn(checkIn: CheckIn)
  case NoOp
