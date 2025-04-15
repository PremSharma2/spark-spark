package oops

object OrderOFInitilizationInTraits {
/*
TODO
  Initialization Order in Scala
 Linearization of Inheritance Hierarchy:
TODO
   Scala initializes classes and traits in a specific order,
   determined by the linearization of the inheritance hierarchy.
   Essentially, Scala will form a list of all the superclasses and traits
   that a class extends, ensuring that each is initialized only once and in a specific order.
   The order is from the most superclass to the subclass.
   For traits, it's a bit more complex because the traits are linearized based
   on the order in which they are mixed in (left to right).
   Field Initialization in Traits:
TODO
    Fields in traits are not initialized until the trait's constructor is called.
    This is unlike classes, where fields are initialized when the class is instantiated.
    If a trait is mixed into a class,
    the trait’s constructor code does not run until
    after all the superclasses of the class have been initialized.
    This means that if a superclass tries to access a field
     or method defined in a trait, it may access an uninitialized field.
 */


  trait A {
    val x: Int
    println(s"Trait A: x = $x")
  }

  class B extends A {
    override val x: Int = 42
    println(s"Class B: x = $x")
  }
//If you instantiate B, you might expect to see:
  /*
  Trait A: x = 42
Class B: x = 42
But actually, you will see:
TODO
 Trait A: x = 0
 Class B: x = 42
 Scala starts by initializing the superclass of B (which is Object by default). A is not initialized yet.
 Trait Initialization:
 Then Scala moves on to initialize A. However,
 the field x in A is not yet initialized because x is overridden in B and B is not yet initialized.
 Scala tries to access x when printing, but it gets the default value for integers, which is 0.
 Subclass Initialization:
 Finally, Scala initializes B, setting x to 42 and printing the correct value.
   */

}
