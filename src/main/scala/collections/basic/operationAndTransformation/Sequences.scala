package collections.basic.operationAndTransformation

object Sequences extends App {

  val asequnce = Seq(1, 4, 3, 2)

  println(asequnce)

  println(asequnce.reverse)
  println(asequnce(2))
  println(asequnce ++ Seq(7, 6, 5))
  println(asequnce.sorted)
  //Ranges
  val arange: Seq[Int] = 1 to 10

  arange.foreach(x => println(x))
  val alist = List(1, 3, 5, 7)
  val prepended = 42 +: alist :+ 89
  println(prepended)
  val appless5=List.fill(5)("apple")
  val newlist=9 :: alist
  println(newlist)
  println(appless5)
  println(alist.mkString("-!-"))
  val l = List(1,2,3,4)
  val l1 = List(5,6,7,8)
  val s = Set.apply(1,2,3,4)
  val s1 = Set(5,6,7,8)
  println(l ++ l1
    println(l + 9)

  println(l :: l1)
  println(l ::: l1)

  s.filter(x => s1.apply(x))
  var x,y,z = (1,2,3)
  //println(x,y,z)


}