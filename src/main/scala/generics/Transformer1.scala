package generics

import generics.GenericBasics.{Animal, Cat, Dog}
object Transformer1 {

  // this is also an action hence it is also Contravrient
  trait MyTransformer[-A, +B] {

    def transform(element: A): B
  }



  def api(tx: MyTransformer[Dog, Cat]) = {
    val mycat: Cat =tx.transform(new Dog)
  }

  // Hence it is conform that Variance is jst to add Type Restriction at compile time
  object Transform extends MyTransformer[Animal, Cat] {
    override def transform(element: Animal): Cat = new Cat

  }

  api(Transform)

}