package eu.joaocosta.coffee

import tyrian.Html.*
import tyrian.*

object Material:
  def button[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-button", attributes.toList)(text)
  val buttonIcon = TagBuilder("mdui-button-icon")
  val fab        = TagBuilder("mdui-fab")

  val dropdown = TagBuilder("mdui-dropdown")
  val menu     = TagBuilder("mdui-menu")
  def menuItem[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-menu-item", attributes.toList)(text)

  val list     = TagBuilder("mdui-list")
  val listItem = TagBuilder("mdui-list-item")

  val card = TagBuilder("mdui-card")

  val layout     = TagBuilder("mdui-layout")
  val layoutMain = TagBuilder("mdui-layout-main")
  val topAppBar  = TagBuilder("mdui-top-app-bar")
  def topAppBarTitle[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-top-app-bar-title", attributes.toList)(text)

  val dialog = TagBuilder("mdui-dialog")

  object Styles:

    val titleLarge: Style = Style(
      "line-height" -> "var(--mdui-typescale-title-large-line-height)",
      "font-size" -> "var(--mdui-typescale-title-large-size)",
      "letter-spacing" -> "var(--mdui-typescale-title-large-tracking)",
      "font-weight" -> "var(--mdui-typescale-title-large-weight)"
    )
