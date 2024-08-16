package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.ZonedDateTime
import java.time.ZoneId

object CheckInModal:
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

  val renderDrinks: Html[Msg] =
    Material.list()(Drink.defaults.map(renderDrink)*)

  def render(open: Boolean): Html[Msg] =
    Material.dialog(
      attribute("open", open.toString),
      attribute("headline", "Add Drink"),
      onEvent("overlay-click", _ => Msg.CloseCheckInModal)
    )(
      renderDrinks
    )
