package eu.joaocosta.coffee.model

/** Drink description
  *
  * @param name
  *   drink name
  * @param caffeinePerMl
  *   milligrams of caffeine per mL
  * @param commonSizes
  *   common sizes (in ml)
  */
final case class Drink(
    name: String,
    caffeinePerMl: Double,
    commonSizes: List[(String, Double)]
)

object Drink:
  val defaults: List[Drink] =
    List(
      Drink("Espresso", 64.0 / 30.0, List("Single" -> 30, "Double" -> 60)),
      Drink(
        "Black Coffee",
        110.0 / 240.0,
        List(
          "Small Cup (170ml)" -> 170,
          "Cup (240mL)"       -> 240,
          "Glass (350mL)"     -> 350
        )
      ),
      Drink(
        "Americano",
        260.0 / 354.0,
        List(
          "Short (8 oz/240mL)"   -> 240,
          "Tall (12 oz/350mL)"   -> 354,
          "Grande (16 oz/470mL)" -> 473,
          "Venti (20 oz/590mL)"  -> 590
        )
      ),
      Drink(
        "Green Tea",
        27.0 / 240.0,
        List("Small Cup (170ml)" -> 170, "Cup (240mL)" -> 240)
      ),
      Drink(
        "Black Tea",
        32.0 / 240.0,
        List("Small Cup (170ml)" -> 170, "Cup (240mL)" -> 240)
      ),
      Drink(
        "Matcha Tea",
        260.0 / 240.0,
        List("Small Cup (170ml)" -> 170, "Cup (240mL)" -> 240)
      ),
      Drink(
        "Coke",
        33.0 / 330.0,
        List("200mL" -> 200, "330mL" -> 330, "500mL" -> 500, "1L" -> 1000)
      ),
      Drink(
        "Mate",
        20 / 100.0,
        List("200mL" -> 200, "330mL" -> 330, "500mL" -> 500)
      ),
      Drink("Energy Drink", 80.0 / 250.0, List("250mL" -> 250, "500mL" -> 500))
    )
