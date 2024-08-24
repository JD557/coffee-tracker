package eu.joaocosta.coffee.components

import tyrian.*

/** Bindings to Material design elements, using MDUI.
  */
object Material:

  val button     = TagBuilder("mdui-button")
  val buttonIcon = TagBuilder("mdui-button-icon")
  val fab        = TagBuilder("mdui-fab")

  val dropdown = TagBuilder("mdui-dropdown")
  val menu     = TagBuilder("mdui-menu")
  val menuItem = TagBuilder("mdui-menu-item")

  val list     = TagBuilder("mdui-list")
  val listItem = TagBuilder("mdui-list-item")

  val collapse     = TagBuilder("mdui-collapse")
  val collapseItem = TagBuilder("mdui-collapse-item")

  val card = TagBuilder("mdui-card")

  val layout         = TagBuilder("mdui-layout")
  val layoutMain     = TagBuilder("mdui-layout-main")
  val topAppBar      = TagBuilder("mdui-top-app-bar")
  val topAppBarTitle = TagBuilder("mdui-top-app-bar-title")
  val bottomAppBar   = TagBuilder("mdui-bottom-app-bar")

  val dialog = TagBuilder("mdui-dialog")

  val textField = TagBuilder("mdui-text-field")

  object Styles:
    val titleLarge: Style = Style(
      "line-height"    -> "var(--mdui-typescale-title-large-line-height)",
      "font-size"      -> "var(--mdui-typescale-title-large-size)",
      "letter-spacing" -> "var(--mdui-typescale-title-large-tracking)",
      "font-weight"    -> "var(--mdui-typescale-title-large-weight)"
    )

    val titleMedium: Style = Style(
      "line-height"    -> "var(--mdui-typescale-title-medium-line-height)",
      "font-size"      -> "var(--mdui-typescale-title-medium-size)",
      "letter-spacing" -> "var(--mdui-typescale-title-medium-tracking)",
      "font-weight"    -> "var(--mdui-typescale-title-medium-weight)"
    )
