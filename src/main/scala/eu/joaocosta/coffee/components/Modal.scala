package eu.joaocosta.coffee.components

import io.circe.*
import io.circe.syntax.*
import org.scalajs.dom
import tyrian.*

/** A Modal window, that manages elements of type T.
  */
trait Modal[T](using Codec[T]):
  /** Key used to store the data managed by this modal in the local storage.
    */
  def localStorageKey: String

  /** Default value to use when the local storage is empty.
    */
  def defaultValue: T

  /** Data model for the modal component.
    *
    * @param open
    *   true if the modal is open
    * @param data
    *   data stored by the modal
    * @param scratch
    *   scratch data temporarily used when the data is being edited
    */
  case class Model(open: Boolean, data: T, scratch: T):
    /** Opens the modal. */
    def openModal = copy(open = true)

    /** Closes the modal. */
    def closeModal = copy(open = false)

    /** Updates the scratch data. */
    def update(f: T => T): Model = copy(scratch = f(scratch))

    /** Drops the scratch data, making it the same as the data. */
    def drop: Model = copy(scratch = data)

    /** Commits the scratch data, copying it to the data. */
    def commit: Model = copy(data = scratch)

    /** Saves the data to the local storage */
    def save(): this.type =
      dom.window.localStorage
        .setItem(localStorageKey, data.asJson.noSpaces)
      this

  /** Initial model. Tries to load the data from the local storage, falling back
    * to the default.
    */
  def init: Model =
    val initialData = Option(dom.window.localStorage.getItem(localStorageKey))
      .flatMap(str => parser.parse(str).flatMap(_.as[T]).toOption)
      .getOrElse(defaultValue)
    Model(false, initialData, initialData)

  /** Updates the model component from a message.
    */
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

  /** Renders the cmodal
    *
    * @param model
    *   Modal's model
    * @return
    *   HTML element to render
    */
  def view(model: Model): Html[Msg]

  /** The modal's messages.
    */
  enum Msg:
    case Open(scratch: T)
    case Close
    case Save
    case UpdateAndSave(f: T => T)
    case UpdateScratch(f: T => T)
    case NoOp
