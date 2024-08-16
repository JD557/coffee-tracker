package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*

object CheckInHistory:
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
                h3(localDate.toString),
                Material.list()(checkIns.map(renderCheckIn))
              )
            )
          )
    )
