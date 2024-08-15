package eu.joaocosta.coffee

/**
  * Drink description
  *
  * @param name drink name
  * @param caffeinePerMl milligrams of caffeine per mL
  * @param commonSizes common sizes (in ml)
  */
final case class Drink(name: String, caffeinePerMl: Double, commonSizes: List[(String, Double)])

object Drink:
  val defaults: List[Drink] =
    List(
      Drink("Espresso", 2.14, List("Single" -> 30, "Double" -> 60)),
      Drink("Coke", 0.125, List(
        "200mL" -> 200,
        "330mL" -> 330,
        "500mL" -> 500,
        "1L" -> 1000))
    )
