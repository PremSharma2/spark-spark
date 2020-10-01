package generics

import generics.GenericBasics.{Cat, Dog}

// this is also an action hence it is also Contravrient
trait MyTransformer[-A,B] {

  def transform(element: A): B
}
 // Hence it is conform that Variance is jst to add Type Restriction at compile time
 class Transform extends MyTransformer[Dog,Cat]{
   override def transform(element: Dog): Cat = ???
 }
  
