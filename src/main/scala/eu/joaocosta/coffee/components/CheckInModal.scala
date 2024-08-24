package eu.joaocosta.coffee.components

import java.time.*
import java.time.format.DateTimeFormatter

import io.circe.Codec
import tyrian.Html.*
import tyrian.*

import eu.joaocosta.coffee.model.*

/** Modal to add a new check in
  */
object CheckInModal extends Modal[CheckInModal.DataModel]:

  /** The data model of the check in modal
    *
    * @param currentCheckInTime
    *   instant for the check in being currently edited
    * @param highlightedDrink
    *   drink to highlight when editing a check in
    * @param history
    *   check in history
    */
  final case class DataModel(
      currentCheckInTime: Instant = Instant.now(),
      highlightedDrink: Option[Drink] = None,
      history: History = History()
  ) derives Codec:
    /** Helper method to transform the history with a function. */
    def updateHistory(f: History => History): DataModel =
      copy(history = f(history))

    /** Updates the current check-in time for a new check-in.
      */
    def withNewCheckIn(): DataModel = copy(
      currentCheckInTime = Instant.now(),
      highlightedDrink = None
    )

  private val dateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

  val localStorageKey = "history"
  val defaultValue    = DataModel()

  /** Renders a single drink, with the option to check in the drink.
    *
    * @param drink
    *   drink to render
    * @param checkInInstant
    *   instant of the check in to create
    * @param icon
    *   icon to use for the list element
    * @return
    *   HTML element of the drink
    */
  def renderDrink(
      drink: Drink,
      checkInInstant: Instant,
      icon: String
  ): Html[Msg] =
    Material.dropdown()(
      Material.listItem(
        attribute("slot", "trigger"),
        attribute("icon", icon)
      )(drink.name),
      Material.menu()(
        drink.commonSizes.map((size, volume) =>
          Material.menuItem(
            onClick(
              Msg.UpdateAndSave(
                _.updateHistory(
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
          renderDrink(
            drink,
            model.scratch.currentCheckInTime,
            model.scratch.highlightedDrink match
              case None                  => "add"
              case Some(d) if d == drink => "check"
              case _                     => "swap_horiz"
          )
        )*
      )
    )
