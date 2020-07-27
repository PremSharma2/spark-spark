package oops

object AnonymousClass extends App  {
  
  abstract class Animal {
    
    def eat:Unit  
  }
  
  val funnyAnimal :Animal=new Animal{
    override def eat:Unit=println("hahahahahah")
    
  }


}