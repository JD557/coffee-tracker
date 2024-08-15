package eu.joaocosta.coffee

import scala.annotation.targetName
import tyrian.*
import tyrian.Html.*

case class TagBuilder(name: String):
  def apply[M](attributes: Attr[M]*)(children: Elem[M]*): Html[M] =
    Tag(name, attributes.toList, children.toList)
  @targetName("tag-list-repeated")
  def apply[M](attributes: List[Attr[M]])(children: Elem[M]*): Html[M] =
    Tag(name, attributes, children.toList)
  @targetName("tag-repeated-list")
  def apply[M](attributes: Attr[M]*)(children: List[Elem[M]]): Html[M] =
    Tag(name, attributes.toList, children)
  @targetName("tag-list-list")
  def apply[M](attributes: List[Attr[M]])(children: List[Elem[M]]): Html[M] =
    Tag(name, attributes, children)
