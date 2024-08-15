package eu.joaocosta.coffee

import tyrian.Html.*
import tyrian.*

object Material:
  def button[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-button", attributes.toList)(text)
  val buttonIcon = TagBuilder("mdui-button-icon")
  val fab = TagBuilder("mdui-fab")

  val dropdown = TagBuilder("mdui-dropdown")
  val menu = TagBuilder("mdui-menu")
  def menuItem[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-menu-item", attributes.toList)(text)

  val list = TagBuilder("mdui-list")
  val listItem = TagBuilder("mdui-list-item")

  val layout = TagBuilder("mdui-layout")
  val topAppBar = TagBuilder("mdui-top-app-bar")
  val layoutMain = TagBuilder("mdui-layout-main")


  val dialog = TagBuilder("mdui-dialog")
