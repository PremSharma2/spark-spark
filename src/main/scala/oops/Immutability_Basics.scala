package oops

import scala.annotation.tailrec
import scala.beans.BeanProperty
/**
 TODO
  * Once the object is created, its fields or properties cannot be altered.
   Any method that appears to modify the object will actually return a new instance with the modified values.
   Since the state of the object never changes after it's created, it is considered to be inherently thread-safe.
  References to immutable objects can be cached as they are not going to change. (i.e. in Hashing it provide fast operations).
  Why Immutability is Important in Functional Programming
   Simplicity and Clarity: Immutable objects are easier to reason
  about because their state doesn't change.
  This eliminates a class of bugs
 to state changes,
  making the code more predictable and easier to understand.

TODO
   Thread Safety:
    In multi-threaded applications,
    immutable objects don't require synchronization,
    as they cannot be modified by concurrent threads.
    This reduces the complexity associated
    with concurrent programming and avoids issues like race conditions.
TODO
  Referential Transparency:
    Functional programming emphasizes the concept of referential transparency,
   where a function's output depends solely on its input parameters
   and has no side effects. Immutability aligns with this principle, ensuring that objects don't change state unexpectedly.

TODO
  Better Memory Management:
    Modern JVMs are highly optimized for dealing with short-lived immutable objects.
 Although creating new objects might seem inefficient,
 in practice, the JVM handles this well,
 often making it more performant than managing mutable objects, especially under heavy multi-threading.

Immutability in Scala
Scala, being a hybrid of object-oriented and functional programming languages,
 supports both mutable and immutable designs.
 However, it encourages immutability:


    Val vs Var:
 Scala has val (value) and var (variable) for declaring immutable and mutable variables,
 respectively. The use of val is preferred in functional programming.
 * 
 * 
 */
class Immutability_Basics(@BeanProperty val count: Int=0) {
  def inc :Immutability_Basics= {
    println("incrementing")
    new Immutability_Basics(this.count + 1) // immutability
  }
  def dec: Immutability_Basics = {
    println("decrementing")
    new Immutability_Basics(count - 1)
  }

  @tailrec
  final def inc(n: Int): Immutability_Basics = if (n <= 0) this else this.inc.inc(n-1)


  def print(): Unit = println(count)

}