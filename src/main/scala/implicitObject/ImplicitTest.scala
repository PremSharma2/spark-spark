package implicitObject

import DonutImplicits._
object ImplicitTest extends App {
  
val vanillaDonut: Donut = Donut("Vanilla", 1.50)
println(s"Vanilla donut name = ${vanillaDonut.name}")
println(s"Vanilla donut price = ${vanillaDonut.price}")
println(s"Vanilla donut produceCode = ${vanillaDonut.productCode}")
println(s"Vanilla donut uuid = ${vanillaDonut.uuid}")

  
}