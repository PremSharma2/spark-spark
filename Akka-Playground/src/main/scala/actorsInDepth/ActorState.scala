package actorsInDepth

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object ActorState {

  /**
  TODO
      Exercise:
          use the setup method to create a word counter which
        - splits each message into words
        - keeps track of the TOTAL number of words received so far
        - log the current # of words in received message + TOTAL # of words
     */



  object WordCounter {
    def apply(): Behavior[String] =
      Behaviors.setup {
        context =>
      var total = 0

      Behaviors.receiveMessage {
        message =>
        val newCount = message.split(" ").length
        //todo  change the state of actor
        total += newCount
        context.log.info(s"Message word count: $newCount - total count: $total")
        Behaviors.same
      }
    }
  }

  /**
TODO
    How to define a message type in Akka ?
    Message types must be IMMUTABLE and SERIALIZABLE.
    - use case classes/objects/ADTs
    - use a flat type hierarchy
   */

  // ADTs
  sealed trait SimpleThing
  case object EatChocolate extends SimpleThing

  case object CleanUpTheFloor extends SimpleThing

  case object LearnAkka extends SimpleThing


  object SimpleHuman {
    def apply(): Behavior[SimpleThing] =
      Behaviors.setup {
        context =>
       var happiness = 0

      Behaviors.receiveMessage {
        case EatChocolate =>
          context.log.info(s"[$happiness] Eating chocolate")
          //todo  update the state of actor
          happiness += 1
          Behaviors.same
        case CleanUpTheFloor =>
          context.log.info(s"[$happiness] Wiping the floor, ugh...")
          happiness -= 2
          Behaviors.same
        case LearnAkka =>
          context.log.info(s"[$happiness] Learning Akka, YAY!")
          happiness += 99
          Behaviors.same
      }
    }
  }

  def demoWordCounter: Unit = {
    val wordCounter = ActorSystem(WordCounter_V2.apply(), "WordCounterDemo")

    wordCounter ! "I am learning Akka"
    wordCounter ! "I hope you will be stateless one day"
    wordCounter ! "Let's see the next one"

    Thread.sleep(1000)
    wordCounter.terminate()
  }


  def demoSimpleHuman(): Unit = {
    //todo  : -> boot strapping the actor system
    val humanActorSystem = ActorSystem(SimpleHuman_V2(), "DemoSimpleHuman")

    humanActorSystem ! LearnAkka
    humanActorSystem ! EatChocolate

    //bombardment of messages
    (1 to 30).
      foreach(_ => humanActorSystem ! CleanUpTheFloor)

    Thread.sleep(1000)
    humanActorSystem.terminate()
  }

  /**
  todo
      Tips:
      - each var/mutable field becomes an immutable METHOD ARGUMENT
      - each state change = new behavior obtained by calling the method with a different argument
        These are not recursive calls in the traditional sense.
        They don't result in a growing call stack.
        Instead, they represent the actor's next behavior.
        When an actor in Akka Typed finishes processing a message,
        it defines how it should behave for the next received message.
        This "next behavior" might be the same
        (e.g., with the same happiness value)
        or different (e.g., with an incremented happiness value).
        Immutability & Stateless Actors:
         Instead of using mutable variables to store state,
         this pattern leverages method arguments
         and the ability to describe new behaviors after each message.
         This is a pattern for creating stateful actors without using mutable state.

         Self-Descriptive Code:
            The actor defines its behavior in terms of its state (happiness).
               Messages modify this state by returning
              a new behavior based on the new state.

            Dynamic Behavior:
            Instead of having a fixed set of actions in
             response to messages,
             Akka Typed actors can dynamically
           change their behavior based on their state or other conditions.
     */
  object SimpleHuman_V2 {
    def apply(): Behavior[SimpleThing] = statelessHuman(0)

    def statelessHuman(happiness: Int): Behavior[SimpleThing] = Behaviors.receive { (context, message) =>
      message match {
        case EatChocolate =>
          context.log.info(s"[$happiness] Eating chocolate")
          statelessHuman(happiness + 1)
        case CleanUpTheFloor =>
          context.log.info(s"[$happiness] Wiping the floor, ugh...")
          statelessHuman(happiness - 2)
        case LearnAkka =>
          context.log.info(s"[$happiness] Learning Akka, YAY!")
          statelessHuman(happiness + 99)
      }
    }
  }

  /**
   *
   * Exercise: refactor the "stateful" word counter into a "stateless" version.
   */
  object WordCounter_V2 {
    def apply(): Behavior[String] = active(0)

    def active(total: Int): Behavior[String] = Behaviors.receive {
         (context,message) =>
        val newCount = message.split(" ").length
        //var total += newCount
        context.log.info(s"Message word count: $newCount - total count: ${total + newCount}")
        active(total + newCount)
    }
  }
  def main(args: Array[String]): Unit = {
    demoWordCounter
    demoSimpleHuman
  }
}
