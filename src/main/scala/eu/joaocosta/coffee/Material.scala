package eu.joaocosta.coffee

import tyrian.Html.*
import tyrian.*

object Material:
  def button[M](attributes: Attr[M]*)(text: String) =
    raw[M]("mdui-button", attributes.toList)(text)

  def buttonIcon[M](attributes: Attr[M]*)(children: Elem[M]*) =
    tag[M]("mdui-button-icon")(attributes.toList)(children.toList)

  def list[M](attributes: Attr[M]*)(children: Elem[M]*) =
    tag[M]("mdui-list")(attributes.toList)(children.toList)

  def listItem[M](attributes: Attr[M]*)(children: Elem[M]*) =
    tag[M]("mdui-list-item")(attributes.toList)(children.toList)
