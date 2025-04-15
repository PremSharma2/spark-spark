package ImplicitsAndTypeClasses

import scala.language.implicitConversions

object MagnetRefactored {

  // Domain Algebra
  sealed trait Request
  sealed trait Response
  case class P2PRequest(payload: String) extends Request
  case class P2PResponse(status: Int) extends Response

  // Type class
  trait Serializer[T] {
    def serialize(value: T): String
  }

// type class instances
  /*
  This is just syntax sugar —
  Scala allows you to instantiate a trait
   with a single abstract method (called a SAM) using a lambda expression:
(value: P2PRequest) => s"P2PRequest(${value.payload})"

  new Serializer[P2PRequest] {
  def serialize(value: P2PRequest): String =  s"P2PRequest(${value.payload})"
}

   */
implicit val p2pRequestSerializer: Serializer[P2PRequest] =
  (value: P2PRequest) => s"P2PRequest(${value.payload})"

  implicit val p2pResponseSerializer: Serializer[P2PResponse] =
    (value: P2PResponse) => s"P2PResponse(${value.status})"


  //Define magnet
  trait ReceiveMagnet {
    def apply(): Int
  }

  //core API of a magnet pattern
  //Now you have only one method — no overload madness
  def receive(magnet: ReceiveMagnet): Int = magnet()


  //This is where we define behavior for different types using implicits.
  //pimp the library or type Enrichment
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext.Implicits.global

  object ReceiveMagnet {

    implicit def fromInt(status: Int): ReceiveMagnet = () => {
      println(s"Handling Int status: $status")
      status
    }

    implicit def fromRequest(req: P2PRequest): ReceiveMagnet = () => {
      println(s"Handling P2PRequest: ${req.payload}")
      1
    }

    implicit def fromResponse(res: P2PResponse): ReceiveMagnet = () => {
      println(s"Handling P2PResponse: ${res.status}")
      2
    }

    implicit def fromFuture[T](future: Future[T])(implicit serializer: Serializer[T]): ReceiveMagnet =
      () => {
        future.foreach(v => println(s"Handling Future[${serializer.serialize(v)}]"))
        3
      }

    implicit def fromGeneric[T](value: T)(implicit serializer: Serializer[T]): ReceiveMagnet =
      () => {
        println(s"Handling generic: ${serializer.serialize(value)}")
        4
      }
  }

  object syntax {
    implicit class ReceiveOps[T](val value: T) extends AnyVal {
      def handle(implicit m: T => ReceiveMagnet): Int = receive(m(value))
    }
  }

  import ReceiveMagnet._
  import syntax._

  P2PRequest("hi").handle
  42.handle
  Future(P2PResponse(200)).handle


}
