package oops

//you have to specify parameter as val or var to make class param as feild
//Although class parameters name   in scope in the code of your greet method, 
//you can only access their value on the object on which add was invoked. 
//Thus, when you say name  in greet's implementation,
//the compiler is happy to provide you with the values for these class parameters.
//But it won't let you say this.name  because that does not refer to the Rational object on which add was invoked. 
//To access the name  on that, you'll need to make them into fields by making them val or var.
//note deafult value concept works with constructor a s well
class Person (name:String, val age:Int =0){
  def greet(name:String):Unit={
    //this can pull insatnce feild and class parameter here name is class parameter not instance feild
    println(s" ${this.name} says:Hi, $name")
  }
  
  //multiple constructirs
  def this(name:String)=this(name,0)
  
}