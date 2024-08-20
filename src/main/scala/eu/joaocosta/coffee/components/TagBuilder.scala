package eu.joaocosta.coffee.components

import scala.annotation.targetName

import tyrian.Html.*
import tyrian.*

/** Helper methods to build Tyrian HTML tags.
  *
  * @param name
  *   tag name
  */
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
