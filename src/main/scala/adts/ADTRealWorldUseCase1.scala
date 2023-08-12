package adts

object ADTRealWorldUseCase1 {
  /**
  TODO
   In Scala, Algebraic Data Types (ADTs) are a powerful feature that allows
   you to model data structures in a more concise and expressive manner.
   ADTs can be used in various real-time use cases
   to represent complex data and perform pattern matching,
   making your code more robust and maintainable.
   One popular use case where ADTs are commonly employed is
   modeling states in a finite state machine (FSM) for real-time applications.
TODO
  Let's consider a real-time application that simulates
  the behavior of a traffic light. The traffic light can be in one of three states:
  "Red," "Yellow," or "Green." We'll use ADTs to model this scenario:

    **/

  sealed trait TrafficLightState
  case object Red extends TrafficLightState
  case object Yellow extends TrafficLightState
  case object Green extends TrafficLightState


  /**
TODO
  In this example, `TrafficLightState`
  is an ADT defined using a sealed trait and
  three case objects representing the different states of the traffic light.
    Now, let's implement a real-time traffic
 light controller that changes the state of the traffic light based on certain events, such as time intervals or user input:
    **/

  object TrafficLightController {
    var currentState: TrafficLightState = Red

    def changeState(): Unit = {
      currentState = currentState match {
        case Red => Green
        case Yellow => Red
        case Green => Yellow
      }
    }

    def main(args: Array[String]): Unit = {
      // Simulate the traffic light behavior
      for (_ <- 1 to 10) {
        println(s"Current state: $currentState")
        changeState()
        Thread.sleep(2000) // Wait for 2 seconds between state changes
      }
    }
  }
  /**
TODO
  In this example, we have a `TrafficLightController` object that
 simulates the behavior of a traffic light by changing its state every 2 seconds in a loop.
 The `changeState` method uses pattern matching on the `currentState` variable to transition between the different states.

  With this ADT-based approach, the traffic light's states are clearly defined and limited to the specified cases.
Any attempt to use an invalid state will result in a compile-time error, making the code more robust and less error-prone.
Additionally, the use of ADTs helps ensure that all possible cases are considered when handling state transitions,
avoiding potential bugs and making the code easier to maintain and reason about.

    This is just one example of how ADTs can be used in real-time applications. They provide an effective way to model complex data structures and define strict patterns for handling different cases, improving code quality and maintainability.
  **/
}
