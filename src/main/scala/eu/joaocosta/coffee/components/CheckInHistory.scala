package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.format.DateTimeFormatter

object CheckInHistory:

  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  def renderCheckIn(checkIn: CheckIn): Html[Msg] =
    Material.listItem(
      attribute("description", timeFormatter.format(checkIn.dateTime))
    )(
      span()(
        f"${checkIn.drink.name} (${checkIn.quantity}%1.1f mL / ${checkIn.totalCaffeine}%1.1f mg)"
      ),
      Material.buttonIcon(
        attribute("variant", "tonal"),
        attribute("slot", "end-icon"),
        attribute("icon", "delete"),
        onClick(Msg.RemoveCheckIn(checkIn))
      )()
    )

  def render(history: History): Html[Msg] =
    div(style(Style("padding-bottom" -> "4em")))(
      h2(style(Material.Styles.titleLarge))("History") ::
        history.checkIns
          .groupBy(_.dateTime.toLocalDate())
          .toList
          .sortBy(_._1)
          .map((localDate, checkIns) =>
            Material.card()(
              div(style(Style("padding" -> "1rem")))(
                h3(dateFormatter.format(localDate)),
                Material.list()(checkIns.map(renderCheckIn))
              )
            )
          )
    )
