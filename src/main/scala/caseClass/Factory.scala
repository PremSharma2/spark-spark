package caseClass

object Factory {
  
  object Animal {
    
     class Dog extends Animal {
      override def speak { println("woof") }
    }

     class Cat extends Animal {
      override def speak { println("meow") }
    }

    // the factory method
    def apply(s: String): Animal = {
      if (s == "dog") new Dog
      else new Cat
    }

    trait Animal {

      def speak
    }
  }
}
