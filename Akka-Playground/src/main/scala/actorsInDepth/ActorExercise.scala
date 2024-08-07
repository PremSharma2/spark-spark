package actorsInDepth

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object ActorExercise {
  /**
   * Exercises
   * 1. Define two "person" actor behaviors, which receive Strings:
   *  - "happy", which logs your message, e.g. "I've received ____. That's great!"
   *  - "sad", .... which logs your message "That sucks."
   *  Test both.
   *
   * 2. Change the actor behavior:
   *  - the happy behavior will turn to sad() if it receives "Akka is bad."
   *  - the sad behavior will turn to happy() if it receives "Akka is awesome!"
   *
   * 3. Inspect my code and try to make it better.
   */


  object Person {
    def happy(): Behavior[String] = Behaviors.receive {
      (context, message) =>
      message match {
        case "Akka is bad." =>
          context.log.info("Don't you say anything bad about Akka!!!")
          sad()
        case _ =>
          context.log.info(s"I've received '$message'.That's great!")
          Behaviors.same
      }
    }

    def sad(): Behavior[String] = Behaviors.receive {
      (context, message) =>
      message match {
        case "Akka is awesome!" =>
          context.log.info("Happy now!")
          happy()
        case _ =>
          context.log.info(s"I've received '$message'.That sucks!")
          Behaviors.same
      }
    }

    def apply(): Behavior[String] = happy()
  }

  def testPerson(): Unit = {
    //boot strapping the actor system
    val person = ActorSystem(Person(), "PersonTest")

    person ! "I love the color blue."
    person ! "Akka is bad."
    person ! "I also love the color red."
    person ! "Akka is awesome!"
    person ! "I love Akka."

    Thread.sleep(1000)

    person.terminate()

  }


  object WeirdActor {
    // wants to receive messages of type Int AND String
    def apply(): Behavior[Any] = Behaviors.receive
    { (context, message) =>
        message match {
        case number: Int =>
          context.log.info(s"I've received an int: $number")
          Behaviors.same
        case string: String =>
          context.log.info(s"I've received a String: $string")
          Behaviors.same
      }
    }
  }

  // solution: add wrapper types & type hierarchy (case classes/objects)
  object BetterActor {
    trait Message
    case class IntMessage(number: Int) extends Message
    case class StringMessage(string: String) extends Message

    def apply(): Behavior[Message] = Behaviors.receive {
      (context, message) =>
      message match {
        case IntMessage(number) =>
          context.log.info(s"Better Actor received Message : I've received an int: $number")
          Behaviors.same
        case StringMessage(string) =>
          context.log.info(s"Better Actor  received Message :I've received a String: $string")
          Behaviors.same
      }
    }
  }


  def demoWeirdActor(): Unit = {
    import BetterActor._

    val weirdActor = ActorSystem(BetterActor(), "WeirdActorDemo")
    weirdActor ! IntMessage(43) // ok
    weirdActor ! StringMessage("Akka") // ok
    // weirdActor ! '\t' // not ok

    Thread.sleep(1000)
    weirdActor.terminate()
  }

  def main(args: Array[String]): Unit = {
    //demoWeirdActor()
    testPerson
    demoWeirdActor
  }
}
