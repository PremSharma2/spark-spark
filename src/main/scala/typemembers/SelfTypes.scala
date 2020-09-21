package typemembers

object SelfTypes  extends App {

  trait InstrumentList{
    def play(): Unit
  }
trait Singer{ self: InstrumentList => //whoever implement Singer he has to implement InstrumentList also
def sing():Unit
}
// It is the correct impl which say that whoever extends Singer has to implement InstrumentList
  class LeadSinger extends Singer with InstrumentList{
    override def sing(): Unit = ???

    override def play(): Unit = ???
  }
// Error: Illegal inheritance, self-type VocalList does not conform to InstrumentList
  // self: InstrumentList => this whole construct is called self-type
  // This is wrong
  /*
  class VocalList extends Singer{
    override def sing(): Unit = ???
  }
  */
  // this anonymus class inmpl also correct
   val jamesHAtfield= new Singer with InstrumentList {
    override def sing(): Unit = ???

    override def play(): Unit = ???
  }

  class Guitarist extends InstrumentList{
    override def play(): Unit = println("Solo-Guitar")
  }
  // this is also fine
val ericClapton= new Guitarist with Singer{
  override def sing(): Unit = ???
}
// Self Types vs inheritance
  // Self types are compared with inheritance
  //here below code means that B must be equal to A i.e B and A are equal
  //i.e B must also B and A
  class A
  class B extends  A // B is an A

 trait T
  // Here it is clearly visble that Self type are Compile time constraint
  // it means that S requires T
  // i.e it diffrent from inheritance hence no comparison between them
 trait S {self: T=>} // S requires T
  // Self types are used in cake pattern

// This is classical Dependency injection Pattern
  class Component{
// API
  }
  class ComponentA extends Component
  class DependentComponent(val component:Component)
  // But in scala We have Cake pattern similar to DI in java

  trait ScalaComponent{
    // api
    def action(arg:Int) :String
  }
  // and now instead of passing arguments of Component we can put type constraint using Self-Type
  // ScalaDependentComponent Requires ScalaComponent
  trait ScalaDependentComponent{self: ScalaComponent=>
    // Hence it clearly shows that i am calling Dependent Component action hence dependency has been injected
   def dependentAction(x:Int):String = action(x) + "Scala-Dependency"

  }
  //fundamental difference between Sparing DI and Scala cake pattern is that
  // Dependencies in Spring DI is that Spring Enforces the type check at run time i.e
  // It will check at run time whether the dependency is correct or not but in Cake
  // Scala Self type enforces this is Compile time type constraint
// Cake pattern Impl
  // layer 1 : -> small components
  // In this layer the components declared are lowest small components of any Application
  trait Picture extends ScalaComponent
  trait Stats  extends ScalaComponent
  //2 Layer: => compose components
  // Here we have injected the dependency to 2nd layer component from 1 st layer
  // and the dependency is also the component from first layer
  // Here Profile Requires ScalaComponent{Picture or Stats } which one is designed for this layer
  // will get injected as Dependent component into Profile
  trait Profile extends ScalaDependentComponent with Picture{

  }
  trait Analytics extends ScalaDependentComponent with Stats
  //3 Layer :-> Application Layer
 // Here 3rd layer is top most layer here it is highest level of abstraction of diffretn layers composition
  // here we have used the cake pattern As DI pattern to inject the required Component dependencies
  // into the Component of one layer from the another layer
  trait AnalyticsApplicationLayer extends ScalaDependentComponent with Analytics
  }
