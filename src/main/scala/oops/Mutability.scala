package oops

object Mutability {
  // todo : mutable variables

  val meaningOflife = 42
  var mutablemeaning = 42
  mutablemeaning = 45

  /**
   *  how to  mutate a datastructures in pure FP style
   *
   */

  class Person(private var name: String, private var a: Int) {
    var nAccesesofAge = 0

    def age: Int = {
      nAccesesofAge += 1
      a
    }

    def age_=(newAge: Int): Unit = {
      println(s"Person $name has changed his age from $a to $newAge")
      a = newAge
    }

    def apply(index: Int): Any = index match {
      case 0 => name
      case 1 => a
      case _ => throw new IndexOutOfBoundsException
    }

    def update(index: Int, value: Any): Unit = index match {
      case 0 => this.name = value.asInstanceOf[String] // just for type casting we used here
      case 1 => this.a = value.asInstanceOf[Int]
      case _ => throw new IndexOutOfBoundsException
    }
  }

  /*
TODO
   How to update data in collections in java
    int[] array= new Array[10]
    array(0) = 0
   */

  def main(args: Array[String]): Unit = {
    val alice = new Person("alice", 25)
    //alice.age not accesible here
    val age = alice.age // this is called accessor or getter methods
    alice.age = 25 // compiler will re write this as
    // alice.age_+=(25)
    val name = alice(0) // alice.apply(0)
    //update the Alice age with value of 33
    alice(1) = 33 // alice.update(1,33)
  }

}
