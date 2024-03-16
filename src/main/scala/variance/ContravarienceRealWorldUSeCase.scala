package variance

object ContravarienceRealWorldUSeCase {

/*
  Contravariance is a concept in Scala (and in other programming languages with advanced type systems)
  that allows for flexible and safe type hierarchies,
   especially when dealing with function parameters.
   In simple terms, contravariance enables a function
   to accept arguments of a less derived (more general) type than specified.
   This might sound abstract,
    so let's dive into a practical use case in the context of an e-commerce domain to understand its utility better.

    ### Use Case: Processing Payments with Different Payment Methods

  Imagine an e-commerce platform that supports various payment methods,
  such as credit card payments, PayPal, and bank transfers.
  Each payment method might require different information and processing logic.
   Let's define a simplified model:



 */

  //Startegy pattern
  trait PaymentMethod {
    def process(amount: BigDecimal): Boolean
  }

  class CreditCard extends PaymentMethod {
    def process(amount: BigDecimal): Boolean = {
      println(s"Processing $amount with Credit Card")
      true // Assume processing is successful
    }
  }

  class PayPal extends PaymentMethod {
    def process(amount: BigDecimal): Boolean = {
      println(s"Processing $amount with PayPal")
      true // Assume processing is successful
    }
  }


 // Next, we need a payment processor that can handle any payment method:


  class NormalPaymentProcessor {
    def processPayment[T <: PaymentMethod](amount: BigDecimal, method: T): Boolean = {
      method.process(amount)
    }
  }
  /*

  Now,
  let's introduce contravariance to enable
  a more flexible design. Assume we want to apply
  special logging or preprocessing steps for certain payment methods.
  We can define a `PaymentProcessor` trait that uses contravariance:


  */

  trait PaymentProcessor[-T] {
    def processPayment(amount: BigDecimal, method: T): Boolean
  }

  class GeneralPaymentProcessor extends PaymentProcessor[PaymentMethod] {
    def processPayment(amount: BigDecimal, method: PaymentMethod): Boolean = {
      println(s"General preprocessing for $amount")
      method.process(amount)
    }
  }

/*
  With contravariance,
  we can define processors that are specialized for more general types
  but can be used wherever a more specific type processor is expected.
  For example, you could have a `LoggerPaymentProcessor`
  that logs details for any payment method, but due to contravariance,
  it can be used in contexts where a specific method processor is required.
    This flexibility allows for designing a system where components
    can be easily extended or replaced without changing the client code
    that uses these components. In our e-commerce domain,
    it means we can add new payment methods or special processing steps
    with minimal changes to the overall system, enhancing maintainability and scalability.


 */
}
