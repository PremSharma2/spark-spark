package typeclasses.advanceInheritance

object AdvanceInheritance  extends App {
  trait Writer[T]{

    def write (value :T):Unit
  }

  trait Closable{
    def close(status:Int):Unit
  }
  trait GenericStream[T]{
    def forEach(f: T => Unit):Unit
  }
  // here in the method argument type is that we don't know but we know that it will be Type
  // which implements all three interfaces Writer,Closable,GenericStream
  def processStream[T](stream: GenericStream[T] with Writer[T] with Closable):Unit = {
    stream.forEach(println)
    stream.close(101)
  }
  // Dimaond Problem

  trait Animal{def name: String}
  trait Lion extends Animal {
    override def name: String = "Lion"}
  trait Tiger extends Animal{
    override def name: String = "Tiger"}

  class Mutant extends Lion with Tiger{
    override def name: String = "Alien"
  }

  // This is perfect Mutant class overrides oth the versions to its own version of name method

  // But lets take one more scenerio here i have removed the mutant class overridden version

  trait Animal1{def name: String}
  trait Lion1 extends Animal {
    override def name: String = "Lion"}
  trait Tiger1 extends Animal{
    override def name: String = "Tiger"}

  class Mutant1 extends Lion with Tiger

  val m = new Mutant1
  println(m.name)
  /*
  Here compiler reads this diamond problem as follows

Mutant1 extends Animal  with { override def name: String = "Lion"}
           with Animal with { override def name: String = "Tiger"}
           This is how compiler reads this code when u write m.name
           So compiler looks that Mutant class implements Something which is an Animal and
           which overrides def name  override def name: String = "Lion"
           that is ok then again compiler sees that we again implemented one more Type Animal
           where def is again overridden override def name: String = "Tiger"
           so now what compiler will do he will overlook the previous implementation
           of the Animal Lion now he
           and consider only latest one so code will look like that

           Mutant1 extends Animal  with { override def name: String = "Lion"}
                                    with { override def name: String = "Tiger"}
                                    i.e compiler will say i have two overridden versions now
                                    so lets take latest

                                    i.e Last override gets picked
   */

  // Type Linearization in scala using super keyword
  trait Cold{
    def print:Unit = println("Cold!!!!")

  }

  trait Green extends Cold {
    override def print: Unit = {
      println("Green!!!")
      super.print
    }
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("Blue!!!")
      super.print
    }
  }

  class  Red{
    def print= println("Red!!!")
  }
  class White extends Red with Green with Blue{
    override def print: Unit = {
      println("White!!!")
      super.print
    }
  }
  val color= new White
 color.print
  /*
  Compiler reads this hirerachy as
  Cold = AnyRef with <body of Cold>
  Green = Anyref with <body of  Cold> with <body of Green>
  Blue = Anyref with <body of Cold>  with <body of Blue>
  White= Anyref with <body of Red> with <body of Green> with <body of Blue> with <body of White>
  or just simplify it
  White = Anyred with Red with Green with Blue with <White>
  White = Anyref with <body of Red>
          with ( Anyref with <body of  Cold> with <body of Green>)
          with (Anyref with <body of  Cold> with <body of blue>)
          with (Body of white <White>)
          now to simlify this equation
          Anyref with <body of Red> = <Red>
          So what compiler does here whatever he has seen before i.e whatever
          it sees second time he will jump over it
          i.e
So it will look like this
  type lineazization  =        Anyref with <body of Red>
                             with (  <body of  Cold> with <body of Green>)
                              with (  <body of blue>)
                               with (Body of white <White>)

          And this above structure is called type lineazization
          so in shor white will look like this
          White = Anyref with <Red> with <Cold> with <Green> with <blue> with (Body of white <White>)
          it will traverse form Right to Left as we can se in the output
   */
}
