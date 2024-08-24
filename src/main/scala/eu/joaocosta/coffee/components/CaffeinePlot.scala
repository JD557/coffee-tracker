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

  private val timeWindowSeconds = 24 * 60 * 60 // 1 day

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
    val nowSecond          = now.getEpochSecond()
    val start              = nowSecond - timeWindowSeconds / 2
    val maxRange           = 2 * settings.maxCaffeine
    val secondsPerPixel    = timeWindowSeconds.toDouble / imageWidth
    val pixelsPerMilligram = imageHeight.toDouble / maxRange

    val baseSurface =
      ImageDataSurface.fromImage(new dom.Image(imageWidth, imageHeight))
    baseSurface.fill(backgroundColor)

    // Draw plot
    (0 until imageWidth).foreach(pixelX =>
      val deltaT = (secondsPerPixel * pixelX).toInt
      val caffeine =
        history.caffeineAt(Instant.ofEpochSecond(start + deltaT), settings)
      val pixelY = (imageHeight - 1) - (caffeine * pixelsPerMilligram).toInt
      baseSurface.fillRegion(
        pixelX - thickness,
        pixelY - thickness,
        2 * thickness,
        2 * thickness,
        plotColor
      )
    )

    // Draw limit lines
    val minY =
      (imageHeight - 1) - (settings.minCaffeine * pixelsPerMilligram).toInt
    baseSurface.fillRegion(0, minY, imageWidth, 1, minColor)
    val maxY =
      (imageHeight - 1) - (settings.maxCaffeine * pixelsPerMilligram).toInt
    baseSurface.fillRegion(0, maxY, imageWidth, 1, maxColor)

    // Draw time lines
    val extraMinutes =
      LocalDateTime.ofInstant(now, ZoneId.systemDefault()).getMinute
    val pixelsPerMinute = 60 / secondsPerPixel
    val pixelsPerHour   = 60 * pixelsPerMinute
    val deltaT          = pixelsPerMinute * extraMinutes
    (1 until 25).foreach(hour =>
      val pixelX = (pixelsPerHour * hour - deltaT).toInt
      (0 until imageHeight by 10).foreach(pixelY =>
        baseSurface.fillRegion(pixelX, pixelY, 1, 5, hourColor)
      )
    )

    // Draw current time line
    baseSurface.fillRegion(imageWidth / 2, 0, 1, imageHeight, nowColor)

    baseSurface.toImage()
