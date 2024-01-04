

import actorsInDepth.ChildActorsAdvanceExercise.{Initialize, Response, UserProtocol, WordCountReply, WordCountTask, WordCounterMaster, WorkerProtocol}
import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.Effect.Spawned
import akka.actor.testkit.typed.scaladsl.{BehaviorTestKit, ScalaTestWithActorTestKit, TestInbox}
import org.scalatest.wordspec.AnyWordSpecLike
import org.slf4j.event.Level


class SynchronousTestingSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike {

  "A word counter master" should {

    "spawn a child upon reception of the initialize message" in {

    /**
   A BehaviorTestKit is created with the behavior of the WordCounterMaster actor.
   This allows us to simulate sending messages
   to the actor and checking its reactions
   without actually running an actor system.
     */
      val master = BehaviorTestKit(WordCounterMaster())

      /**
       TODO
          This sends (or more accurately, simulates sending)
          the Initialize(1) message
          to the WordCounterMaster actor.
          Because this is a synchronous environment,
          the actor will process this message immediately.
       */

      master.run(Initialize(1)) // synchronous "sending" of the message Initialize(1)

      // check that an effect was produced
      /**
          After sending the message,
          we expect that the actor has produced an effect,
          specifically spawning a child actor.
          The expectEffectType[Spawned[WorkerProtocol]]
          is used to extract this effect and make sure
          it's of the type Spawned[WorkerProtocol],
          where WorkerProtocol would be the protocol
          or set of messages the child actor can handle.
       */

      val effect = master.expectEffectType[Spawned[WorkerProtocol]]

      // inspect the contents of those effects
      effect.childName should equal("Child-Actor-1")
    }


    "send a task to a child" in {
      val master = BehaviorTestKit(WordCounterMaster())
      master.run(Initialize(1))

      // from the previous test - "consume" the event
      val effect = master.expectEffectType[Spawned[WorkerProtocol]]

      val mailbox = TestInbox[UserProtocol]() // the "requester"'s inbox
      // start processing
      master.run(WordCountTask("Akka testing is pretty powerful!", mailbox.ref))

      // mock the reply from the child
      master.run(WordCountReply(0, 5))
      // test that the requester got the right message
      mailbox.expectMessage(Response(5))
    }

    "log messages" in {
      val master = BehaviorTestKit(WordCounterMaster())
      master.run(Initialize(1))
      master.logEntries() shouldBe Seq(CapturedLogEvent(Level.INFO, "[master] initializing with 1 children"))
    }
  }
}