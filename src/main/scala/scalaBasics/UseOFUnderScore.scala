package scalaBasics

object UseOFUnderScore  extends App {

  val _ =5 // defining a value whose name u do'nt really care
  val onlyfive: Seq[Int] = (1 to 10).map(_*2)
  // another usage of under score
  // We really dont care the type of option

  def processList(list : List[Option[_]] ): Int = list.length

  // another scenario is the  default initializer
  // let say i ave a string variable i want let teh jvm decide what the variable will have
  var mystring:String = _


  // lambda sugars
  Seq(1,2,3,4) map(x => x*3)
  Seq(1,2,3,4) map(_*3)
val sumfunction : (Int,Int) => Int  = _ + _

  //Higher Kinded type
  class MyHigherKindedJewel[M[_]]
  val myjewel= new MyHigherKindedJewel[List]
  // variable arguments methods
  def makeSentence(words : String*) = words.toSeq.mkString(",")
makeSentence("I","Love","Scala")
  val words= Array("I","Love","Akka")
  //I,Love,Akka
  //val vararg: Array[String] = words : _*
  // it automatically converts this into varargs
  println(makeSentence(words : _*))

}
