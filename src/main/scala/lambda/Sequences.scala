package lambda

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
  
}