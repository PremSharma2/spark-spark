package oops

object OOBasics extends App {
  /*val person=new Person("john",26)
  println(person.age)
  println(person.greet("Kaushik"))
  */
  
  val author=new Writer("Prem","kaushik",1986)
  author.year
  val novel=new Novel( "bounce-back",2019,author)
  println(novel author_age)
  println(novel isWrittenBy author)
  
  val counter= new Counter
  counter.inc.print
  //incrementing three times
  counter.inc.inc.inc.print
  val counter1=new Counter(0)
  counter1.inc(10).print
  
  
}