package eu.joaocosta.coffee

/**
  * Drink description
  *
  * @param name drink name
  * @param caffeinePerMl grams of caffeine per mL
  * @param commonSizes common sizes (in ml)
  */
final case class Drink(name: String, caffeinePerMl: Double, commonSizes: List[Double])

object Drink:
  val defaults: List[Drink] =
    List(
      Drink("Espresso", 2.14, List(30, 35, 60, 70)),
      Drink("Coke", 0.125, List(200, 330, 500, 750, 1000))
    )
