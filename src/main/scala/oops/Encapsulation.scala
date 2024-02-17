package oops

object Encapsulation {
  /**
  TODO
   * In Scala, the `private[this]` modifier is used to create a variable
   * that is private to the current instance of the enclosing class.
   * This means that the variable is not accessible to any other instance of the same class,
   * including other instances of the class itself.
TODO
  Here are a few scenarios where you might use `private[this]` variable declaration:
TODO
 1. Optimization: If you have a variable that is used only within the current instance
  and you know that it will not be accessed by any other instances of the class,
  marking it as `private[this]` can provide performance benefits.
  This is because the Scala compiler can make certain optimizations
  since it knows that the variable is not shared across instances.

TODO
 2. Encapsulation: Using `private[this]` helps to
  enforce encapsulation by making the variable completely private to the current instance.
  It prevents accidental access or modification of the variable from other instances or external code.

  TODO
   3. Security: If you have sensitive data stored in a variable and
    you want to ensure that it cannot be accessed or modified from any other instance,
   `private[this]` provides an additional layer of security.

  Here's an example to illustrate the use of `private[this]`:

 In the above example, the `price` variable is declared as `private[this]`,
  making it accessible and modifiable only within the same instance of the `Product` class.
  It cannot be accessed or modified from other instances like `product2.price = 29.99`,
  which would result in a compile-time error.
   * @param price
   */
  class Product(private[this] var price: Double) {
    def updatePrice(newPrice: Double): Unit = {
      // Can access and modify price within the current instance
      price = newPrice
    }

    def displayPrice(): Unit = {
      // Can access price within the current instance
      println(s"Price: $price")
    }
  }

    val product1 = new Product(10.99)
     product1.displayPrice()  // Output: Price: 10.99

  //TODO Another created the DS Product with some values so it can cause data corruption
  val product2 = new Product(19.99)
  product2.displayPrice()  // Output: Price: 19.99

  // TODO In the main thread ae are trying to update
  //  The following line would cause a compile-time error because price is private[this]
 // product2.price = 29.99

}
