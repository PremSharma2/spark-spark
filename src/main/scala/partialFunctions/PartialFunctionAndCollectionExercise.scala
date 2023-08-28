package partialFunctions

object PartialFunctionAndCollectionExercise  extends App {

  /*
  Seq[A] as PartialFunction[Int, A]
  Being List an indirect subclass of collection.
  Seq and given that the latter has the following definition,
   you can see clearly that every Seq[A] is also a PartialFunction[Int, A]:


  trait Seq[+A] extends PartialFunction[Int, A]


f1: PartialFunction[Int,String] = List(a, c, b)

val transformations: Seq[Data => Data] = Seq(
  transform1,
  transform2,
  transform3
  // ...
)

// Apply the i-th transformation
val transformedData = transformations(i)(originalData)

*/

 val f1: PartialFunction[Int,Int] = List(1,2,3)
 println(f1.apply(0))
}
