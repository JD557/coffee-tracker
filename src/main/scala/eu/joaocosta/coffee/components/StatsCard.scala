package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import tyrian.*
import tyrian.Html.*
import java.time.Instant

object StatsCard:
  def render(history: History, settings: Settings): Html[Msg] =
    val plot = CaffeinePlot.getImage(history, settings, 1024, 512)
    Material.card(style(Style("width" -> "100%")))(
      img(src := plot.src, style(Style("max-width" -> "100%"))),
      div(style(Style("padding" -> "1rem")))(
        h2(style(Material.Styles.titleLarge))("Overview"),
        span(
          f"Current caffeine: ${history.caffeineAt(Instant.now(), settings)}%1.1f mg"
        )
      )
    )
