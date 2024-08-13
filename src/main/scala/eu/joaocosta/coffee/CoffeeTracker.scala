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
    Material.listItem()(
      span()(drink.name),
      div(attribute("slot", "end-icon"))(
        drink.commonSizes.map((size, volume) =>
          Material.button(
            attribute("variant", "tonal"),
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
    Material.listItem(attribute("description", checkIn.dateTime.toLocalTime().toString))(
      span()(
        s"${checkIn.drink.name} (${checkIn.quantity}mL / ${checkIn.totalCaffeine.toString}g)"
      ),
      Material.buttonIcon(
        attribute("variant", "tonal"),
        attribute("slot", "end-icon"),
        attribute("icon", "delete"),
        onClick(Msg.RemoveCheckIn(checkIn))
      )()
    )

  def renderHistory(history: History): Html[Msg] =
    span()(
      history.checkIns
        .groupBy(_.dateTime.toLocalDate())
        .toList
        .sortBy(_._1)
        .flatMap((localDate, checkIns) =>
          List(
            h3(localDate.toString),
            Material.list()(checkIns.map(renderCheckIn)*)
          )
        )
    )

  def renderStats(history: History, settings: Settings): Html[Msg] =
    val plot = CaffeinePlot.getImage(history, settings, 1024, 512)
    img(src := plot.src)

  def view(model: Model): Html[Msg] =
    div()(
      h1()("Coffee Tracker"),
      h2("Stats:"),
      renderStats(model.history, model.settings),
      h2("Add Drink:"),
      renderDrinks,
      h2("History:"),
      renderHistory(model.history)
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
