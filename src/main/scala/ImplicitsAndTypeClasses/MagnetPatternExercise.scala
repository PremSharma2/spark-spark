package ImplicitsAndTypeClasses

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions

object MagnetPatternExercise {
  /*
TODO
 Problem Statement
  Let’s say we would like to write an overloaded method
  which completes the futures and return their result.
  The invocation of function will look as below
TODO
 completeFuture( Future {1}) // returns value 1
 completeFuture( Future{"hello"}) // return value "hello"
 Using Method Overloading
 One of the way to define above function is to use method overloading. The below code does the same
TODO
 def completeFuture(value:Future[String]):String = Await.result(value,Duration.Zero)
TODO
 def completeFuture(value: Future[Int]):Int =  Await.result(value,Duration.Zero)
 But when you try to compile this you will get below compilation error

 completeFuture(_root.scala.concurrent.Future) is already defined in scope
TODO
 Type Erasure
 Type erasure is feature inherited from Java to Scala. This feature turn above two functions as below
 def completeFuture(value:Future):String = Await.result(value,Duration.Zero)
 def completeFuture(value:Future):Int = Await.result(value,Duration.Zero)
 As you can see from above code, both method signature looks exactly same.
 This make Scala think that method is defined multiple times in same scope.

 TOdo
  As we cannot use the method overload in this scenario, we need to use Scala type machinery to handle the same.
  This is where magnet pattern comes into picture.
  Magnet pattern is a design pattern which use Scala’s implicits and dependent types.

  The below sections will guide you about different parts of the pattern.
TODO
 Defining a Magnet Trait
 A Magnet trait defines the application and result of the type.
 For our example, the below will be the trait
 type class
 sealed trait FutureMagnet[R] {
  def apply() : R
}
TODO
 Define completeFuture using Magnet
 Now the completeFuture method will be defined as below
TODO
  def completeFuture(magnet: FutureMagnet):magnet.Result = magnet()

 Implementing Magnet for Int and String
Once the above is defined, then we need to implement the magnet for needed types.

object FutureMagnet {
    implicit def intFutureCompleter(future:Future[Int]) = new FutureMagnet {
      override type Result = Int

      override def apply(): Result = Await.result(future,Duration.Zero)
    }

    implicit def stringFutureCompleter(future:Future[String]) = new FutureMagnet {
      override type Result = String

      override def apply(): Result = Await.result(future,Duration.Zero)
    }

  }
   */

  sealed trait FutureMagnet[R] {

    def apply() : R
  }

  private def completeFuture[R](magnet: FutureMagnet[R])= magnet()

  object FutureMagnet {
    implicit def intFutureCompleter(future:Future[Int]): FutureMagnet[Int] = new FutureMagnet[Int] {

      override def apply(): Int = Await.result(future,Duration.Zero)
    }

    implicit def stringFutureCompleter(future:Future[String]): FutureMagnet[String] = new FutureMagnet[String] {

      override def apply(): String = Await.result(future,Duration.Zero)
    }

  }
  completeFuture[Int]( Future {1})
  completeFuture[String]( Future{"hello"})
}
