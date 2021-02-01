package codingChallenge

object VarianceTest extends App {

    trait MyTransformer[-A,B] {

      def transform(element: A): B
    }
    // Hence it is conform that Variance is jst to add Type Restriction at compile time
    class  Transform extends MyTransformer[Animal,Dog]{
      override def transform(element: Animal): Dog = new Dog
    }
    class Animal
    class Dog extends Animal

  def useContra( transf:MyTransformer[Dog,Dog] ) =
    transf.transform(new Dog)
  useContra(new Transform)
}
