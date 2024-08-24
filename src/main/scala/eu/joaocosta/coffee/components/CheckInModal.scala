package eu.joaocosta.coffee.components

import java.time.*
import java.time.format.DateTimeFormatter

import tyrian.Html.*
import tyrian.*

import eu.joaocosta.coffee.model.*

/** Modal to add a new check in
  */
object CheckInModal extends Modal[History]:

  private val dateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  val localStorageKey = "history"
  val defaultValue    = History()

  /** Renders a single drink, with the option to check in the drink.
    *
    * @param drink
    *   drink to render
    * @param checkInInstant
    *   instant of the check in to create
    * @return
    *   HTML element of the drink
    */
  def renderDrink(drink: Drink, checkInInstant: Instant): Html[Msg] =
    Material.dropdown()(
      Material.listItem(
        attribute("slot", "trigger"),
        attribute("icon", "add")
      )(drink.name),
      Material.menu()(
        drink.commonSizes.map((size, volume) =>
          Material.menuItem(
            onClick(
              Msg.UpdateAndSave(
                _.addCheckIn(
                  CheckIn(
                    drink,
                    OffsetDateTime
                      .ofInstant(checkInInstant, ZoneId.systemDefault()),
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
          scala.util
            .Try(dateTimeFormatter.withZone(ZoneId.systemDefault()).parse(str))
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
