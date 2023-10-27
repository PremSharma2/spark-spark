package actorsInDepth
import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import scala.concurrent.duration._
object SupervisionIntro {

  object FussyWordCounter {
    def apply(): Behavior[String] = active()

    def active(total: Int = 0): Behavior[String] = Behaviors.receive {
      (context, message) =>
        context.log.info(s"[FussyWordCounter-Actor] Received the Event ")
        val wordCount = message.split(" ").length
      context.log.info(s"[FussyWordCounter-Actor]:-> Received piece of text: '$message', counted $wordCount words, total: ${total + wordCount}")
      // throw some exceptions (maybe unintentionally)
      if (message.startsWith("Q")) throw new RuntimeException("I HATE queues!")
      if (message.startsWith("W")) throw new NullPointerException

      active(total + wordCount)
    }
  }

  // actor throwing exception gets killed

  def demoCrash(): Unit = {
    val guardian: Behavior[Unit] = Behaviors.setup {
      context =>
      val fussyCounter = context.spawn(FussyWordCounter(), "fussyCounter")
        context.log.info(s"[FussyWordCounter-Actor] message Received ")
      fussyCounter ! "Starting to understand this Akka business..."
      fussyCounter ! "Quick! Hide!"
      fussyCounter ! "Are you there?"

      Behaviors.empty
    }

    val system = ActorSystem(guardian, "DemoCrash")
    Thread.sleep(1000)
    system.terminate()
  }

  def demoWithParent(): Unit = {
    val parentBehavior: Behavior[String] = Behaviors.setup {
      context =>
        context.log.info(s"[Parent-Actor] Boot strapping the child Actor")
      val child = context.spawn(FussyWordCounter(), "fussyChild")
      context.watch(child)

      Behaviors.receiveMessage[String] {
        message =>
          context.log.info(s"[Parent-Actor] received the Event now sending Command to Child Actor")
          child ! message
        Behaviors.same
      }
        .receiveSignal {
          case (context, Terminated(childRef)) =>
            context.log.warn(s"[FussyWordCounter-Actor] failed or Crashed : ${childRef.path.name}")
            Behaviors.same
        }
    }

    val guardian: Behavior[Unit] = Behaviors.setup {
      context =>
        context.log.warn(s"[UserGuardian-Actor] Boot strapping the Actor System and Actor family")
      val fussyCounter = context.spawn(parentBehavior, "fussyCounter")

      fussyCounter ! "Starting to understand this Akka business..."
      fussyCounter ! "Quick! Hide!"
      fussyCounter ! "Are you there?"

      Behaviors.empty
    }

    val system = ActorSystem(guardian, "DemoCrashWithParent")
    Thread.sleep(1000)
    system.terminate()
  }

  def main(args: Array[String]): Unit = {
    demoWithParent
  }
}