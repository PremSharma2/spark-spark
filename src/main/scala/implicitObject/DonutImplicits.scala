package implicitObject

/*
 * Assume that you were given a Donut type in a library or dependency and as such do not have access to modify the Donut source code.
 *  In addition, say you are given a requirement to create a unique identifier for the Donut type.

 

   With Implicit Class, you can easily extend the functionality of the Donut type. 
   In the example below, 
   we add a new method named uuid which returns a String and it uses the name and productCode of the Donut type to construct the unique id.
 * 
 * 
 */
object DonutImplicits {

  implicit class AugmentedDonut(donut: Donut) {
    def uuid: String = s"${donut.name} - ${donut.productCode.getOrElse(12345)}"
  }

}