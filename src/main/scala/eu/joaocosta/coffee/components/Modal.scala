package eu.joaocosta.coffee.components

import tyrian.*

trait Modal[T]:
  case class Model(open: Boolean, data: T, scratch: T):
    def openModal = copy(open = true)
    def closeModal = copy(open = false)
    def update(f: T => T): Model = copy(scratch = f(scratch))
    def drop: Model              = copy(scratch = data)
    def commit: Model            = copy(data = scratch)

  def init: Model

  def update(msg: Msg, model: Model): Model =
    msg match
      case Msg.Open(scratch) =>
        model.copy(scratch = scratch).openModal
      case Msg.Close =>
        model.drop.closeModal
      case Msg.Save =>
        model.commit.closeModal
      case Msg.UpdateAndSave(f) =>
        model.update(f).commit.closeModal
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
