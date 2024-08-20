package eu.joaocosta.coffee.components

import java.time.*

import tyrian.Html.*
import tyrian.*

import eu.joaocosta.coffee.model.*

/** Component to render the current stats, such as the caffeine consumed in a
  * day and the caffeine plot.
  */
object StatsCard:
  /** Renders a stats card.
    *
    * @param history
    *   check in history
    * @param settings
    *   caffeine levels computation settings
    * @return
    *   HTML element to render
    */
  def view(history: History, settings: Settings): Html[Msg] =
    val now   = Instant.now()
    val today = LocalDateTime.ofInstant(now, ZoneId.systemDefault()).toLocalDate
    val plot  = CaffeinePlot.getImage(history, settings, 1024, 256, now)
    val weeklyAverage = (0 until 7)
      .map(delta => today.minusDays(delta))
      .map(history.dailyCaffeine)
      .sum / 7
    div(
      h2(style(Material.Styles.titleLarge))("Overview"),
      Material.card(style(Style("width" -> "100%", "max-width" -> "1024px")))(
        img(src := plot.src, style(Style("width" -> "100%"))),
        div(style(Style("padding" -> "1rem")))(
          Material.list()(
            Material.listItem(
              attribute("nonclickable", "true"),
              attribute("icon", "access_time"),
              attribute(
                "description",
                f"${history.caffeineAt(now, settings)}%1.1f mg"
              )
            )(span(f"Current caffeine")),
            Material.listItem(
              attribute("nonclickable", "true"),
              attribute("icon", "today"),
              attribute(
                "description",
                f"${history.dailyCaffeine(today)}%1.1f mg"
              )
            )(span(f"Caffeine ingested today")),
            Material.listItem(
              attribute("nonclickable", "true"),
              attribute("icon", "calendar_view_week"),
              attribute("description", f"${weeklyAverage}%1.1f mg")
            )(span(f"Average daily caffeiene (7 days)"))
          )
        )
      )
    )
