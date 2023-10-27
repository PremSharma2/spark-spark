package typemembers

import scala.language.reflectiveCalls

object StructuralTypes  extends App {
  // type aliasing
  type JavaClosable = java.io.Closeable

  // let say our team has implemented some custom impl of closeable

  class HipsterCloseable{
    def close(): Unit = println("close")
  }

  // now implement a method that will accept both Java Closeable and the Custom Scala Closeable
 // def closeQuitely(closeable: JavaClosable or HipsterCloseable)
  // this can possible in scala only by structural types
  // it is nothing but type alias with return type of this code block i.e the type structure
  // Type Structure :-> Is  anything which has a structure in which we have close method that type will be aliased
  // with UnifiedCloseable
  type UnifiedCloseable = {
   def close() : Unit
  }// this is called structural type
// now we can redefine method
  def closeQuitely(closeable: UnifiedCloseable): Unit = closeable.close()

  closeQuitely(new JavaClosable {
    override def close(): Unit = ???
  })
  closeQuitely(new HipsterCloseable)

  //type refinements
// So AdvancedCloseable is JavaCloseable Plus the Structural type in form of codeblock
  // so this type of aliasing represents two types
  // it is that structural type  which has this structure plus it implements JavaClosable i.e JavaClosable type
  type AdvancedCloseable = JavaClosable {
    def closeSilently(): Unit
  }
  class AdvancedJavaCloesable extends JavaClosable{
    override def close(): Unit = println("Java closable")
    def closeSilently():Unit = println("......Java closes silently...")
  }
  def closeResources(advCloseable: AdvancedCloseable):Unit = advCloseable.closeSilently()
  // Here compiler will read like this that AdvancedJavaCloesable is originates from JavaCloseable
  // and also has structural type hence that what the the compiler wanted
  // it is type aliasing for combo types i.e two types
  closeResources(new AdvancedJavaCloesable)

  // using structural types as standalone types
  // this is like we passed structural Type as an argument in method
  def altCloesable(cloesable: {def close():Unit}): Unit = cloesable.close()



  // type checking --> duck typing using structural types
  type SoundMaker = {
  def makeSound():Unit

  }

  class Dog{
    def makeSound():Unit = println("bark")

  }

  class Car{
    def makeSound():Unit = println("Vrooom!!  ")

  }
  // this is called static Duck typing
  // here RHS is reference of Dog type which matches the Structure of Structure Type SoundMaker
  // one caveat is that it duck typing is possible because of Reflection hence it will be expensive one
val SoundMaker = new Dog

  //Exercise
  trait MyAbstractList[+T]{
    def head:T
    def tail : MyAbstractList[T]
  }
  class Human{
  def head: Brain = new Brain
  }
  class Brain{
    override def toString: String = "Brains"

  }
  def f[T](someThingWithTheHead: { def head : T}): Unit = println(someThingWithTheHead.head)
  /*
  We ned to check that f is compatible with MyList and Human
   */

  /*
  We ned to check that HeadEqualizer is compatible with MyList and Human
   */
  object HeadEqualizer{
    type HeadAble[T] = {def head : T}
    def ===[T](a:HeadAble[T], b:HeadAble[T]) : Boolean = a.head == b.head
  }

  // solution
  //1:
  case object EmptyList extends MyAbstractList[Nothing]{
    override def head: Nothing = ???

    override def tail: MyAbstractList[Nothing] = ???
  }
  case class Node[T](override val head:T, override val tail: MyAbstractList[T]) extends MyAbstractList[T]{

  }
  // Hence proved first question
  f(EmptyList)
  f(Node(2,EmptyList))
  f(new Human) // Compiler plays very smartly it immediately correlates T with human
  // i.e derived the value of T from Human head def return type
  // Question 2

  val brainzList= new Node(new Brain, EmptyList)
  val stringList= Node("Scala",EmptyList)
  HeadEqualizer.===(brainzList,new Human)
  // This is Wrong but what happens what scala does is that
  //it removes Type parameter at run time because ducktyping uses reflection
  // which reduces the === method to  def ===(a:Headable,b:Headable) : Boolean = a.head == b.head
  HeadEqualizer.===(new Human,stringList)

}
