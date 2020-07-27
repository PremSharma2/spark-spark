package oops

class Emp (ename:String, val age:Int){
  
  //body
  //class field
 val x=2
 println(1+3)
 def greetName(name:String):Unit= println(s"$this.name says:Hi ,$name")
  
}


object OO extends App{
  
  val p=new Emp("Sehwag",32)
  println(p.x)
  println(p.greetName("Daniel"))
  
}