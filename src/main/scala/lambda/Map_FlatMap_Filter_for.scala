package lambda

object Map_FlatMap_Filter_for  extends App{
  
  val list=List(1,2,3)
  val input=List("hello-world","scala","prem")
  println(input.maxBy(x => x.length()))
  println(list.head)
  println(list.headOption)
  println(list.tail)
  val l = List(1,2,3,4)
  val l1 = List(5,6,7,8)

  println(l ++ l1)

  println(l :: l1)
  println(l ::: l1)
  list.foreach(println(_))
  // map
  println(list.map( _+1))
  println(list.map(x => x+1))
  //filter
  println(list.filter(x => x%2 ==0))
  println(list.filter(_%2 ==0))
  //flatmap
  val toPair = (x:Int) => List(x,x+1)
  println(list.flatMap(toPair))
  val numbers=List(1,2,3,4)
  val chars=List('a','b','c','d')
  //iteration logic 
  val combinations= numbers.flatMap(n => chars.map(c => ""+c + n))
  
  println(combinations)
  val forcomprehension= for{
    
    n <- numbers if n%2 ==0
    c <- chars
    
  }yield "" + c + n
  println(forcomprehension)
  
  // syntax overload
  list.map {
    //pattern matching
    case x => x*2
  }
  
}