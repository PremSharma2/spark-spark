object DarkSyntacticSugar extends App {


  def singleArgumentMehood(str: Int): String = s"$str N little ducks"

  val description = singleArgumentMehood {

    42
  }

  List(1, 2, 3, 4).map {
    x => x + 1

  }

  //single Abstract Method

  trait Action {
    def apply(int: Int): Int
  }

  val anInstance: Action = new Action {
    override def apply(a: Int): Int = a + 1
  }
  val aFunkyInstance: Int => Int = (a: Int) => a + 1

  //val asweetThread= new Thread (()=> println("Sweet, scala"))
  //val aSweetThread= new Thread(() => println("sweet scala"))
  val prependedList = 2 :: List(3, 4)
  /*
  scala compiler converts this into
    List(3,4).::(2)
    reason being: Scala spec last char  decides associativity of method
    :: means right associative
    : means left associative

   */
  1 :: 2 :: 3 :: 4 :: List(6, 7)
  // it will be compiled like this due to right associativity
  List(6, 7).::(4).::(3).::(2).::(1)


  class MyStream[T] {

    def -->:(value: T): MyStream[T] = this
  }

  //  val myStream = 1 -->: 2 -->: 3 -->: 4: -->: new MyStream[Int]


}
