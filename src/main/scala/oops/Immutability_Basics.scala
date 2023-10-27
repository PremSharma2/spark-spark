package oops

import scala.annotation.tailrec
import scala.beans.BeanProperty
/**
 TODO
  * An immutable object remains in exactly one state, the state in which it was created.
  * Therefore, immutable object is thread-safe so there is no synchronization issue.
  * They cannot be corrupted by multiple threads accessing them concurrently.
  * This is far and away the easiest approach to achieving thread safety.
  Immutable classes are easier to design, implement, and use than mutable classes.
  Immutable objects are good Map keys and Set elements, since these typically do not change once created.
  Immutability makes it easier to write, use and reason about the code (class invariant is established once and then unchanged).
  Immutability makes it easier to parallelize program as there are no conflicts among objects.
  The internal state of program will be consistent even if you have exceptions.
  References to immutable objects can be cached as they are not going to change. (i.e. in Hashing it provide fast operations).
 * 
 * 
 */
class Immutability_Basics(@BeanProperty val count: Int=0) {
  def inc :Immutability_Basics= {
    println("incrementing")
    new Immutability_Basics(this.count + 1) // immutability
  }
  def dec = {
    println("decrementing")
    new Immutability_Basics(count - 1)
  }

@tailrec
  final def inc(n: Int): Immutability_Basics = if (n <= 0) this else this.inc.inc(n-1)


  def print = println(count)

}