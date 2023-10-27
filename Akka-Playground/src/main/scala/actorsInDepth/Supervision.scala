package actorsInDepth

import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._
object Supervision {
  //Actor defination
  object FussyWordActor {
    def apply(): Behavior[String] = active()

    def active(total: Int = 0): Behavior[String] = Behaviors.receive {
      (context, message) =>
        context.log.info(s"[FussyWordCounter-Actor] Received the Event ")
        val wordCount = message.split(" ").length
        context.log.info(s"Received piece of text: '$message', counted $wordCount words, total: ${total + wordCount}")
        // throw some exceptions (maybe unintentionally)
        context.log.warn(s"[FussyWordCounter-Actor] found incorrect Data throwing the Exception ")
        if (message.startsWith("Q")) throw new RuntimeException("I HATE queues!")
        if (message.startsWith("W")) throw new NullPointerException

        active(total + wordCount)
    }
  }


//Supervision
  def demoSupervisionWithRestart(): Unit = {
    val parentBehavior: Behavior[String] = Behaviors.setup {
      context =>
      // supervise the child behavior  with a restart "strategy"
        context.log.info(s"[Parent-Actor] supervise the child behavior  with a restart strategy ")

      //val childBehavior = Behaviors.supervise(FussyWordActor())
      //.onFailure[RuntimeException](SupervisorStrategy.restart)


        val childBehavior = Behaviors.supervise(FussyWordActor())
          .onFailure[RuntimeException](SupervisorStrategy.restartWithBackoff(1.second,1.minute,0.2))

        context.log.info(s"[Parent-Actor] creating the Child Actor ")
      val child = context.spawn(childBehavior, "fussyChild")
      context.watch(child)

      Behaviors.receiveMessage[String] {
        message =>
          context.log.info(s"[Parent-Actor] Received the Event message sending it to Child-Actor to Process ")
        child ! message
        Behaviors.same
      }
        .receiveSignal {
          case (context, Terminated(childRef)) =>
            context.log.warn(s"Child failed: ${childRef.path.name}")
            Behaviors.same
        }
    }

    val guardian: Behavior[Unit] = Behaviors.setup {
      context =>
        context.log.info(s"[UserGuardian-Actor] Boot strapping the Custom Actor family")
      val fussyCounter = context.spawn(parentBehavior, "fussyCounter")

      fussyCounter ! "Starting to understand this Akka business..."
      fussyCounter ! "Quick! Hide!"
      fussyCounter ! "Are you there?"
      fussyCounter ! "What are you doing?"
      fussyCounter ! "Are you still there?"

      Behaviors.empty
    }

    val system = ActorSystem(guardian, "DemoCrashWithParent")
    Thread.sleep(1000)
    system.terminate()
  }

  /**
   * Exercise: how do we specify different supervisor strategies for different exception types?
   */
  val differentStrategies = Behaviors.supervise(
    Behaviors.supervise(FussyWordActor())
      .onFailure[NullPointerException](SupervisorStrategy.resume)
  )
    .onFailure[IndexOutOfBoundsException](SupervisorStrategy.restart)

  /*
    OO equivalent:
    try { .. }
    catch {
      case NullPointerException =>
      case IndexOutOfBoundsException =>
    }

    place the most specific exception handlers INSIDE and the most general exception handlers OUTSIDE.
   */
  def main(args: Array[String]): Unit = {
    demoSupervisionWithRestart()
  }

}
