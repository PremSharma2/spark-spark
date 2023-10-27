package actorsInDepth

import akka.actor.typed.{ActorSystem, Behavior, PostStop}
import akka.actor.typed.scaladsl.Behaviors


object StoppingActors {

  object SensitiveActor {

    def apply(): Behavior[String] = Behaviors.receive[String] {
      (context, message) =>
        context.log.info(s"Received ${message}")
        if (message == "Stopping")
          Behaviors.stopped(
            ()=> context.log.info("Actor Stopping  Now will no longer received any message ")
          )

        else Behaviors.same

    }
      .receiveSignal{
        case (context, PostStop) =>
          context.log.info(s"Received PostStop signal for Cleanup cleaning up actor resources As Actor had already shut down")
          Behaviors.same //  not used anymore in case of stopping As Actor is Dead now but useful for any other signal
      }
  }

  def main(args: Array[String]) = {
    val userGuardianBehavior: Behavior[Unit] = Behaviors.setup { context =>
      // setup all the imp Buisness Actors of your application
      //setup the interaction between the Actors
      val sensitiveActor = context.spawn(SensitiveActor(), "Sensitive-Actor")
      sensitiveActor ! "Hi"
      sensitiveActor ! "How are you"
      sensitiveActor ! "you're ugly"
      sensitiveActor ! "Stopping"
      sensitiveActor ! "helllo-"

      Behaviors.empty
    }
    val system = ActorSystem(userGuardianBehavior, "DemoStoppingActor")
    Thread.sleep(1000)
    system.terminate()
  }
}