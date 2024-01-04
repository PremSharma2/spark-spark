

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import org.scalatest.wordspec.AnyWordSpecLike

class UsingProbesSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  import UsingProbesSpec._

  "A master actor" should {


    "register a worker" in {
      val master = testKit.spawn(Master(), "master")
      val workerProbe = testKit.createTestProbe[WorkerTask]()
      val externalProbe = testKit.createTestProbe[ExternalProtocol]()

      master ! Register(workerProbe.ref, externalProbe.ref)
      // this shd be the response recived from master
      externalProbe.expectMessage(RegisterAck)
    }


    "send a task to the worker actor" in {
      val master = testKit.spawn(Master())
      val workerProbe = testKit.createTestProbe[WorkerTask]()
      val externalProbe = testKit.createTestProbe[ExternalProtocol]()

      master ! Register(workerProbe.ref, externalProbe.ref)
      externalProbe.expectMessage(RegisterAck)

      val taskString = "I love Akka"
      //mocking the interaction of master actor with External Actor or Sender or Requester Actor
      master ! Work(taskString, externalProbe.ref)

      workerProbe.expectMessage(WorkerTask(taskString, master.ref, externalProbe.ref))

      // mocking the interaction with the worker actor
      //ideally it shd be send from the Worker Actor to master Actor
      master ! WorkCompleted(3, externalProbe.ref)

      //now master sends back the o/p to the external actor or Requester actor
      externalProbe.expectMessage(Report(3))
    }


    "aggregate data correctly" in {

      val master = testKit.spawn(Master())
      val externalProbe = testKit.createTestProbe[ExternalProtocol]()

      //define the custom moked worker behvior
      val mockedWorkerBehavior = Behaviors.receiveMessage[WorkerTask] {
        case WorkerTask(_, masterRef, replyTo) =>
          masterRef ! WorkCompleted(3, replyTo)
          Behaviors.same
      }

      val workerProbe = testKit.createTestProbe[WorkerTask]()
      val mockedWorkerActor = testKit.spawn(Behaviors.monitor(workerProbe.ref, mockedWorkerBehavior))

      // register both the actors first to the master
      master ! Register(mockedWorkerActor, externalProbe.ref)
      // again mocked the interaction b/w master and External Requestor actor
      //it shd be sent from External requestor in natural flow!!!

      externalProbe.expectMessage(RegisterAck)

      //sending tasks to master to execute
      val taskString = "I love Akka"
      master ! Work(taskString, externalProbe.ref)
      master ! Work(taskString, externalProbe.ref)


      externalProbe.expectMessage(Report(3))
      externalProbe.expectMessage(Report(6))
    }
  }
}

object UsingProbesSpec {

  /*
    requester -> master -> worker
              <-        <-
   */
  trait MasterProtocol
  case class Work(text: String, replyTo: ActorRef[ExternalProtocol]) extends MasterProtocol
  case class WorkCompleted(count: Int, originalDestination: ActorRef[ExternalProtocol]) extends MasterProtocol
  case class Register(workerRef: ActorRef[WorkerTask], replyTo: ActorRef[ExternalProtocol]) extends MasterProtocol

  case class WorkerTask(text: String, master: ActorRef[MasterProtocol], originalDestination: ActorRef[ExternalProtocol])

  trait ExternalProtocol
  case class Report(totalCount: Int) extends ExternalProtocol
  case object RegisterAck extends ExternalProtocol

  object Master {
    def apply(): Behavior[MasterProtocol] = Behaviors.receiveMessage {
      case Register(workerRef, replyTo) =>
        replyTo ! RegisterAck
        active(workerRef)
      case _ =>
        Behaviors.same
    }

    def active(workerRef: ActorRef[WorkerTask], totalCount: Int = 0): Behavior[MasterProtocol] =
      Behaviors.receive { (context, message) =>
        message match {
          case Work(text, replyTo) =>
            workerRef ! WorkerTask(text, context.self, replyTo)
            Behaviors.same
          case WorkCompleted(count, destination) =>
            val newTotalCount = totalCount + count
            destination ! Report(newTotalCount)
            active(workerRef, newTotalCount)
        }
      }
  }

  // object Worker { ... }
}