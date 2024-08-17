package eu.joaocosta.coffee.components

import org.scalajs.dom
import io.circe.*
import io.circe.syntax.*
import tyrian.*

trait Modal[T](using Codec[T]):
  def localStorageKey: String
  def defaultValue: T

  case class Model(open: Boolean, data: T, scratch: T):
    def openModal                = copy(open = true)
    def closeModal               = copy(open = false)
    def update(f: T => T): Model = copy(scratch = f(scratch))
    def drop: Model              = copy(scratch = data)
    def commit: Model            = copy(data = scratch)
    def save(): this.type        =
      dom.window.localStorage
        .setItem(localStorageKey, data.asJson.noSpaces)
      this

  def init: Model =
    val initialData = Option(dom.window.localStorage.getItem(localStorageKey))
      .flatMap(str => parser.parse(str).flatMap(_.as[T]).toOption)
      .getOrElse(defaultValue)
    Model(false, initialData, initialData)

  def update(msg: Msg, model: Model): Model =
    msg match
      case Msg.Open(scratch) =>
        model.copy(scratch = scratch).openModal
      case Msg.Close =>
        model.drop.closeModal
      case Msg.Save =>
        model.commit.closeModal.save()
      case Msg.UpdateAndSave(f) =>
        model.update(f).commit.closeModal.save()
      case Msg.UpdateScratch(f) =>
        model.update(f)
      case Msg.NoOp =>
        model

  def view(model: Model): Html[Msg]

  enum Msg:
    case Open(scratch: T)
    case Close
    case Save
    case UpdateAndSave(f: T => T)
    case UpdateScratch(f: T => T)
    case NoOp
