package generics

import generics.GenericBasics.Dog

// this is also an action hence it is also Contravrient
trait MyTransformer[-A,B] {

  def transform(element: A): B
}
 // Hence it is conform that Variance is jst to add Type Restriction at compile time
 class Transform extends MyTransformer[Dog,Dog]{
   override def transform(element: Dog): Dog = new Dog
 }


