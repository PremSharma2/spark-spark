package abstractMethodPattern
//single abstarct method pattern
object AbstractMethodPattern  extends  App {

  trait Action{
    def act(x:Int):Int
  }

//  val myAction: Action = (x:Int) =>  x+1
}
