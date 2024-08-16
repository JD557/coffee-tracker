package eu.joaocosta.coffee.components

import eu.joaocosta.coffee.model.*
import eu.joaocosta.minart.backend.ImageDataSurface
import eu.joaocosta.minart.graphics.*
import java.time.Instant
import org.scalajs.dom

object CaffeinePlot:
  private val backgroundColor = Color(0, 0, 0)
  private val plotColor = Color(255, 255, 0)
  private val nowColor = Color(255, 255, 255)
  private val minColor = Color(128, 128, 128)
  private val maxColor = Color(255, 128, 128)
  private val thickness = 1

  def getImage(history: History, settings: Settings, imageWidth: Int, imageHeight: Int): dom.Image =
    val baseSurface = ImageDataSurface.fromImage(new dom.Image(imageWidth, imageHeight))
    baseSurface.fill(backgroundColor)
    val now = Instant.now().getEpochSecond()
    val start = now - (12 * 60 * 60)
    val maxRange = 2 * settings.maxCaffeine

    // Draw plot
    (0 until imageWidth).foreach { pixelX =>
      val delta = ((24 * 60 * 60) * pixelX) / imageWidth
      val caffeine = history.caffeineAt(Instant.ofEpochSecond(start + delta), settings)
      val pixelY = (imageHeight - 1) - (imageHeight * caffeine / maxRange).toInt
      (-thickness to thickness).foreach(dy => baseSurface.putPixel(pixelX, pixelY + dy, plotColor))
      (-thickness to thickness).foreach(dx => baseSurface.putPixel(pixelX + dx, pixelY, plotColor))
    }

    // Draw lines
    val minY = (imageHeight - 1) - (imageHeight * settings.minCaffeine / maxRange).toInt
    val maxY = (imageHeight - 1) - (imageHeight * settings.maxCaffeine / maxRange).toInt
    (0 until imageWidth).foreach { pixelX =>
      baseSurface.putPixel(pixelX, minY, minColor)
      baseSurface.putPixel(pixelX, maxY, maxColor)
    }
    (0 until imageHeight).foreach { pixelY =>
      baseSurface.putPixel(imageWidth / 2, pixelY, nowColor)
    }

    baseSurface.toImage()
