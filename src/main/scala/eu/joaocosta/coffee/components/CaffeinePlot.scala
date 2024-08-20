package eu.joaocosta.coffee.components

import java.time.*

import org.scalajs.dom

import eu.joaocosta.coffee.model.*
import eu.joaocosta.minart.backend.ImageDataSurface
import eu.joaocosta.minart.graphics.*

/** Plot with caffeine levels.
  */
object CaffeinePlot:
  private val backgroundColor = Color(30, 25, 27)
  private val plotColor       = Color(180, 170, 20)
  private val nowColor        = Color(200, 195, 190)
  private val hourColor       = Color(70, 80, 120)
  private val minColor        = Color(110, 128, 120)
  private val maxColor        = Color(205, 130, 120)
  private val thickness       = 1

  /** Generates an <image> element with the caffeine plot.
    *
    * @param history
    *   check in history
    * @param settings
    *   caffeine levels computation settings
    * @param imageWidth
    *   image width
    * @param imageHeight
    *   image height
    * @param now
    *   current instant
    * @return
    *   image element
    */
  def getImage(
      history: History,
      settings: Settings,
      imageWidth: Int,
      imageHeight: Int,
      now: Instant
  ): dom.Image =
    val baseSurface =
      ImageDataSurface.fromImage(new dom.Image(imageWidth, imageHeight))
    baseSurface.fill(backgroundColor)
    val nowSecond       = now.getEpochSecond()
    val start           = nowSecond - (12 * 60 * 60)
    val maxRange        = 2 * settings.maxCaffeine
    val secondsPerPixel = (24 * 60 * 60).toDouble / imageWidth

    // Draw plot
    (0 until imageWidth).foreach(pixelX =>
      val delta = (secondsPerPixel * pixelX).toInt
      val caffeine =
        history.caffeineAt(Instant.ofEpochSecond(start + delta), settings)
      val pixelY = (imageHeight - 1) - (imageHeight * caffeine / maxRange).toInt
      (-thickness to thickness).foreach(dy =>
        baseSurface.putPixel(pixelX, pixelY + dy, plotColor)
      )
      (-thickness to thickness).foreach(dx =>
        baseSurface.putPixel(pixelX + dx, pixelY, plotColor)
      )
    )

    // Draw limit lines
    val minY =
      (imageHeight - 1) - (imageHeight * settings.minCaffeine / maxRange).toInt
    val maxY =
      (imageHeight - 1) - (imageHeight * settings.maxCaffeine / maxRange).toInt
    (0 until imageWidth).foreach(pixelX =>
      baseSurface.putPixel(pixelX, minY, minColor)
      baseSurface.putPixel(pixelX, maxY, maxColor)
    )

    // Draw time lines
    val extraMinutes =
      LocalDateTime.ofInstant(now, ZoneId.systemDefault()).getMinute
    val pixelsPerMinute = 60 / secondsPerPixel
    val pixelsPerHour   = 60 * pixelsPerMinute
    val delta           = pixelsPerMinute * extraMinutes
    (0 until imageHeight).foreach(pixelY =>
      if (pixelY % 10 < 5)
        (1 until 25).foreach(hour =>
          val pixelX = (pixelsPerHour * hour - delta).toInt
          baseSurface.putPixel(pixelX, pixelY, hourColor)
        )
      baseSurface.putPixel(imageWidth / 2, pixelY, nowColor)
    )

    baseSurface.toImage()
