package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.*
import java.time.format.DateTimeFormatter

object CheckInModal extends Modal[History]:

  private val dateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  val init: Model = Model(false, History(), History())

  def renderDrink(drink: Drink, checkInInstant: Instant): Html[Msg] =
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
                  CheckIn(
                    drink,
                    OffsetDateTime.ofInstant(checkInInstant, ZoneId.systemDefault()),
                    volume
                  )
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
      Material.textField(
        `type` := "datetime-local",
        label  := "Check in date",
        value := dateTimeFormatter
          .withZone(ZoneId.systemDefault())
          .format(model.scratch.currentCheckInTime),
        onInput(str =>
          scala.util.Try(dateTimeFormatter.withZone(ZoneId.systemDefault()).parse(str))
            .map(value =>
              Msg.UpdateScratch(
                _.copy(currentCheckInTime = Instant.from(value))
              )
            )
            .getOrElse(Msg.NoOp)
        )
      )(),
      Material.list()(
        Drink.defaults.map(drink =>
          renderDrink(drink, model.scratch.currentCheckInTime)
        )*
      )
    )
