package generics

import generics.GenericBasics.{Cat, Dog}
object Transformer1 {

  // this is also an action hence it is also Contravrient
  trait MyTransformer[-A, +B] {

    def transform(element: A): B
  }

  // Hence it is conform that Variance is jst to add Type Restriction at compile time
  object Transform extends MyTransformer[Dog, Cat] {
    override def transform(element: Dog): Cat = new Cat

  }

  def api(tx: MyTransformer[Dog, Cat]) = {
    val mycat: Cat =tx.transform(new Dog)
  }

  api(Transform)

}