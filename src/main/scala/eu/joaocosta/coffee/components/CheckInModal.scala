package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.ZonedDateTime
import java.time.ZoneId

object CheckInModal extends Modal[History]:

  val init: Model = Model(false, History(), History())

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
              Msg.UpdateAndSave(
                _.addCheckIn(
                  CheckIn(drink, ZonedDateTime.now(ZoneId.of("UTC")), volume)
                )
              )
            )
          )(size)
        )*
      )
    )

  def view(model: Model): Html[Msg] =
    Material.dialog(
      attribute("open", model.open.toString),
      attribute("headline", "Add Drink"),
      onEvent("overlay-click", _ => Msg.Close)
    )(
      Material.list()(Drink.defaults.map(renderDrink)*)
    )
