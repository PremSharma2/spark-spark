package variance

object ContravarienceRealWorldUSeCase {

/*
TODO
     Contravariance is a concept in Scala (and in other programming languages with advanced type systems)
     that allows for flexible and safe type hierarchies,
    especially when dealing with function parameters.
    In simple terms, contravariance enables a function
    to accept arguments of a less derived (more general) type than specified.
    This might sound abstract,
    so let's dive into a practical use case in the context of an e-commerce domain to understand its utility better.
TODO
    ### Use Case: Processing Payments with Different Payment Methods
TODO
  Imagine an e-commerce platform that supports various payment methods,
  such as credit card payments, PayPal, and bank transfers.
  Each payment method might require different information and processing logic.
   Let's define a simplified model:



 */

  // Define the PaymentMethod trait
  //startegy design pattern
  trait PaymentMethod {
    def process(amount: BigDecimal): Boolean
  }

  // Specific payment methods
  class CreditCard extends PaymentMethod {
    def process(amount: BigDecimal): Boolean = {
      println(s"Processing $amount with Credit Card")
      true
    }
  }

  class PayPal extends PaymentMethod {
    def process(amount: BigDecimal): Boolean = {
      println(s"Processing $amount with PayPal")
      true
    }
  }

  // Contravariant PaymentProcessor
  trait PaymentProcessor[-T] {
    def processPayment(amount: BigDecimal, method: T): Boolean
  }

  // General Payment Processor (handles all PaymentMethod types)
  class GeneralPaymentProcessor extends PaymentProcessor[PaymentMethod] {
    def processPayment(amount: BigDecimal, method: PaymentMethod): Boolean = {
      println(s"General preprocessing for $amount")
      method.process(amount)
    }
  }

  // Function that expects a CreditCard processor, but we pass a general processor!
  def processCreditCardPayment(amount: BigDecimal, processor: PaymentProcessor[CreditCard]): Unit = {
    val creditCard = new CreditCard
    println(s"Processing credit card payment of $$${amount}")
    processor.processPayment(amount, creditCard)
  }

  object PaymentAPI {
    def main(args: Array[String]): Unit = {
      val generalProcessor: PaymentProcessor[PaymentMethod] = new GeneralPaymentProcessor

      println("\n=== Passing General Processor Where CreditCard Processor is Expected ===")
      processCreditCardPayment(100, generalProcessor) // Works due to contravariance!
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
