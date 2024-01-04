package actorsInDepth

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

object ChildActorsAdvanceExercise {
/**
 * TODO: Implement
     Distributed Word counting
     requester  ---> Computational task or WordCountTask---> (WCM)--> computation task or WorkerTask --> (worker) assigned to one child of type WCW
     requester  <--- Computational Result or Response<--- (WCM)<-- computation Result or WordCountReply <-- one child of type WCW (flow is Left to right)
     Schemes for scheduling tasks to children is Round Robin
     task1 -- childActor1
     task2---- childActor2
     ...
     ...

     task10 --- childActor10

 */

//todo : -> protocols
 trait  MasterProtocol // messages supported by master
 trait  WorkerProtocol // messages supported by worker
 trait  UserProtocol  // messages supported by Requester i.e its a Response Message

  //todo:These All are Master messages
// This is special message received the
// Master here this means Master will create nChildren
  case class Initialize(nChildren: Int) extends MasterProtocol
  case class WordCountTask( text: String, replyTo:ActorRef[UserProtocol]) extends MasterProtocol
  // This is the message sent back by worker to master
  case class WordCountReply(id: Int, count: Int) extends MasterProtocol


  //todo: These All are Worker Messages
  case class WorkerTask(id: Int,task:String) extends WorkerProtocol

  // Requester message or User Messages
  case class Response(count: Int) extends UserProtocol

//Master Actor or WCM
  object WordCounterMaster {
    def apply():Behavior[MasterProtocol] = Behaviors.receive {

      (context, message) =>
        message match {
          case   Initialize(nChildren) =>
            context.log.info(s"[master] Spinning up the $nChildren children Actors")

            val childrenRefs: Seq[ActorRef[WorkerProtocol]] = for{
              i <- 1 to nChildren
            }yield context.spawn(WordCounterWorker.apply(context.self),s"Child-Actor-$i")

            active(childrenRefs,0,0,Map.empty)

          case _ => context.log.info(s"[Master]: Command  not Supported")
            Behaviors.same
        }

    }

    def active(
                childRefs: Seq[ActorRef[WorkerProtocol]],
                currentChildIndex: Int,
                currentTaskId: Int,
                requestMap: Map[Int, ActorRef[UserProtocol]]
              ): Behavior[MasterProtocol] =

       Behaviors.receive {
        (context, message) =>
         message match {
          case WordCountTask(text, replyTo) =>
            context.log.info(s"[master] I've received $text - I will send it to the child worker $currentChildIndex")

            // prep
            val task = WorkerTask(currentTaskId, text)

            val childRef = childRefs(currentChildIndex)
            // send task to child
            childRef ! task

            // update the state
            val nextChildIndex = (currentChildIndex + 1) % childRefs.length
            val nextTaskId = currentTaskId + 1
            val newRequestMap = requestMap.updated(currentTaskId,replyTo)

            // change behavior
            active(childRefs, nextChildIndex, nextTaskId, newRequestMap)

            //response form worker
          case WordCountReply(id, count) =>
            context.log.info(s"[master] I've received a reply for task-Id $id with $count")
            // prep
            val originalSender = requestMap(id)
            // send back the result to the original requester
            originalSender ! Response(count)
            // change behavior: removing the task id from the map because it's done
            active(childRefs, currentChildIndex, currentTaskId, requestMap - id)
          case _ =>
            context.log.info(s"[master] Command not supported while active")
            Behaviors.same
        }
      }
  }


  object WordCounterWorker {
    def apply(masterRef: ActorRef[MasterProtocol]): Behavior[WorkerProtocol] = Behaviors.receive {
      (context, message) =>
      message match {
        case WorkerTask(taskId, text) =>

          context.log.info(s"[${context.self.path}] I've received task with TaskId:-> $taskId to process the text: -> '$text'")
          // process the Data
          val result = text.split(" ").length
          // send back the result to the master
          masterRef ! WordCountReply(taskId, result)
          Behaviors.same
        case _ =>
          context.log.info(s"[${context.self.path}] Command unsupported")
          Behaviors.same
      }

    }
  }


  //TODO Requester receiving Response from all Workers via WCM and handling response for calculating counts
  object Aggregator{
    def apply():Behavior[UserProtocol] =active()
    def active(totalWords:Int=0):Behavior[UserProtocol] = Behaviors.receive[UserProtocol] {
      (context, message) =>
       message match {
         case Response(count) =>
           context.log.info(s"[Aggregator] received Response from WCM $count and total words are ${totalWords + count}")
           active(totalWords+count)

       }
    }
  }

  def testWordCounter={
    val userGuardianBehavior: Behavior[Unit] = Behaviors.setup { context =>
      // setup all the imp Buisness Actors of your application
      //setup the interaction between the Actors
      context.log.info(s"Bootstrapping the Actor Hierarchy Spinning up the Guardian Actor")
      val aggregator = context.spawn(Aggregator(), "Aggregator-Actor")
      val wcm = context.spawn(WordCounterMaster(), "WCM-Actor")
      // asking master create 3 Child Actors or workers
       wcm ! Initialize(3)
      // This is the task for worker and reply will sent back to Aggregator which is handling all response
       wcm ! WordCountTask("I Love Akka", aggregator)
       wcm ! WordCountTask("Scala is super Dope", aggregator)
       wcm ! WordCountTask("Yes it is !!!", aggregator)
      // userGuardian Actor usually  has no  Behavior of its own
      Behaviors.empty
    }
    val actorSystem = ActorSystem(userGuardianBehavior,"WordCounting")
    Thread.sleep(1000)
    println("Terminating Actor System")
    actorSystem.terminate()
  }
  def main(args: Array[String]): Unit = {
    testWordCounter
  }
}
