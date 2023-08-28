package adts

object SmartConstructorExercise {
  /**
  TODO
   In Scala, a smart constructor is a technique used to enforce certain constraints when creating instances of a class.
   It involves using a private or protected constructor along with a companion object
   that provides a public method for creating instances of the class.
   This way, you can control the construction process and ensure that only valid instances of the class are created.
   One real-time use case for using smart constructors is in modeling geometric shapes,
   particularly when you want to ensure that only valid shapes can be created.
   Let's take the example of modeling circles. In a valid circle,
   the radius should be a positive non-zero value.
   We can enforce this constraint using a smart constructor. Here's how it can be done:

  **/
  // Define the Circle class with a private constructor
  class Circle private (val radius: Double) {
    // Define any other methods or properties related to circles here
    def area: Double = math.Pi * radius * radius
  }

  // Define the companion object for Circle
  object Circle {
    // Smart constructor to create instances of Circle
    def apply(radius: Double): Option[Circle] = {
      if (radius > 0)
        Some(new Circle(radius))
      else
        None
    }
  }

  object Main extends App {
    // Creating instances using the smart constructor
    val validCircle = Circle(5.0) // This will create a valid circle with radius 5.0
    val invalidCircle = Circle(-2.0) // This will return None since the radius is invalid

    // Using pattern matching to work with the Option returned by the smart constructor
    validCircle match {
      case Some(circle) => println(s"Valid Circle with radius ${circle.radius}")
      case None => println("Invalid Circle. Please provide a valid radius.")
    }
  }

  /**
 TODO
  In this example, the `Circle` class has a private constructor,
  making it inaccessible outside of the class.
  The `Circle` companion object provides the `apply` method,
  which acts as the smart constructor.
  When creating a new circle, you use the `Circle(radius)` syntax,
  and the smart constructor checks if the provided radius is greater than zero.
  If the radius is valid, it returns `Some(circle)`,
  where `circle` is the newly created instance of the `Circle` class. Otherwise,
  it returns `None`, indicating that the construction failed due to an invalid radius.
  By using a smart constructor, you can ensure that only valid circles are created,
  and it provides a clear and concise way to enforce the constraints of the geometric shape being modeled.
  This approach can be extended to other use cases where
  you want to control the creation of instances and enforce certain rules or constraints.

**/
}
