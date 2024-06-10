package functional.programming

object AndThenUseCase {
  /*
  TODO
    In Scala, you can design an API using the composition of two functions `f1` and `f2`
     by utilizing the `andThen` method available on functions.
     The `andThen` method allows you to chain two functions together,
      where the output of the first function becomes the input of the second function.
       This functional composition technique provides a clean
       and expressive way to design APIs with reusable and composable components.
       Here's a simple example of designing an API that calculates the
     total price of a shopping cart by composing two functions, `calculateSubtotal` and `applyDiscount`:

   */

  object ShoppingCartAPI {
    // Function to calculate the subtotal of items in the cart
    def calculateSubtotal(items: List[Double]): Double = {
      items.sum
    }

    // Function to apply a discount to the total price
    def applyDiscount(total: Double, discountPercentage: Double): Double = {
      total * (1 - discountPercentage / 100)
    }

    // API function that composes the two functions to get the final total price
    //Its an use of ETA expansion as well here
    def getTotalPrice(items: List[Double], discountPercentage: Double): Double = {
      val calculateTotal: List[Double] => Double = calculateSubtotal _ andThen {
        total => applyDiscount(total, discountPercentage)
      }
      calculateTotal(items)
    }
  }

  /*
  TODO
    In this example, the `getTotalPrice` function is the main API method.
    It composes the `calculateSubtotal` function with the `applyDiscount` function using `andThen`.
    The subtotal calculated by `calculateSubtotal` becomes the input for the
    `applyDiscount` function, which then returns the final discounted total.
  TODO
    Real-world use case:
    Let's assume you are building an e-commerce platform,
     and you want to provide an API to calculate
     the total price of a customer's shopping cart with a discount.
     The API should take the list of item prices
      and the discount percentage as input and return the final price after applying the discount.


   */

  object Main {
    def main(args: Array[String]): Unit = {
      val items = List(25.0, 10.0, 15.0, 30.0)
      val discountPercentage = 10.0

      val totalPrice = ShoppingCartAPI.getTotalPrice(items, discountPercentage)
      println(s"The total price after discount is $$${totalPrice}")
    }
  }

  /*
  TODO
    In this example, the `getTotalPrice` function is the main API method.
    In this real-world use case, you can use the `getTotalPrice` method
    from the `ShoppingCartAPI` object to calculate the final price
    of the shopping cart after applying the discount.
     This allows for easy composability and reusability of the functions,
      making the API design more maintainable and extensible.
   */
}
