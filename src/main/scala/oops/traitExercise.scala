package oops

//TODO Mixins cake pattern
trait DonutShoppingMixinCartDao[A] {

  donutDatabase: DonutDatabase[A] => // dependency injection

  def add(donut: A): Long = {
    println(s"DonutShoppingCartDao-> add method -> donut: $donut")
    donutDatabase.addOrUpdate(donut)
  }

  def update(donut: A): Boolean = {
    println(s"DonutShoppingCartDao-> update method -> donut: $donut")
    donutDatabase.addOrUpdate(donut)
    true
  }

  def search(donut: A): A = {
    println(s"DonutShoppingCartDao-> search method -> donut: $donut")
    donutDatabase.query(donut)
  }

  def deletefromDb(donut: A): Boolean = {
    println(s"DonutShoppingCartDao-> delete method -> donut: $donut")
    donutDatabase.delete(donut)
  }


}

trait DonutDatabase[A] {

  def addOrUpdate(donut: A): Long = ???

  def query(donut: A): A = ???

  def delete(donut: A): Boolean = ???

}
//wiring is done here
// this can be done in ScalaPB in Scala projects
case class DonutShoppingMixinCart[A]() extends DonutShoppingMixinCartDao[A] with DonutDatabase[A]



