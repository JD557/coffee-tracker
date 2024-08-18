package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.format.DateTimeFormatter

object CheckInHistory:

  private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

  def renderCheckIn(checkIn: CheckIn, history: History): Html[Msg] =
    Material.collapseItem()(
      Material.listItem(
        attribute("slot", "header"),
        attribute("icon", "coffee"),
        attribute("end-icon", "edit"),
        attribute("description", timeFormatter.format(checkIn.dateTime))
      )(
        span()(
          f"${checkIn.drink.name} (${checkIn.quantity}%1.1f mL / ${checkIn.totalCaffeine}%1.1f mg)"
        )
      ),
      Material.listItem(attribute("nonclickable", "true"))(
        Material.button(
          attribute("variant", "text"),
          onClick(
            Msg.ModifyCheckInModal(
              CheckInModal.Msg.Open(
                history.removeCheckIn(checkIn)
              )
            )
          )
        )("Edit"),
        Material.button(
          attribute("variant", "text"),
          onClick(
            Msg.ModifyCheckInModal(
              CheckInModal.Msg.UpdateAndSave(_.removeCheckIn(checkIn))
            )
          )
        )("Remove")
      )
    )

  def render(history: History): Html[Msg] =
    div()(
      h2(style(Material.Styles.titleLarge))("History") ::
        history.checkIns
          .groupBy(_.dateTime.toLocalDate())
          .toList
          .sortBy(_._1)
          .reverse
          .map((localDate, checkIns) =>
            Material.card(style(Style("width" -> "100%", "max-width" -> "1024px")))(
              div(style(Style("padding" -> "1rem")))(
                h3(style(Material.Styles.titleMedium))(dateFormatter.format(localDate)),
                Material.list()(
                  Material.collapse(attribute("accordion", "true"))(
                    checkIns.map(checkIn => renderCheckIn(checkIn, history))
                  )
                )
              )
            )
          )
    )
