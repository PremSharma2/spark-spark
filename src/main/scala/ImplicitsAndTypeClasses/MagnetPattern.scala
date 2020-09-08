package ImplicitsAndTypeClasses

import scala.concurrent.Future

object MagnetPattern extends App {
  // This Pattern solves the problem created by method over loading
  class P2PRequest
  class P2PResponse
  class Serializer[T]
  trait Actor{
    def receive(statusCode:Int):Int
    def receive(request:P2PRequest) :Int
    def receive(response:P2PResponse):Int
    def receive[T](message:T)(implicit serializer:Serializer[T]):Int
    def receive [T : Serializer](message:T,statusCode:Int):Int
    def receive(future : Future[P2PRequest]): Int
    // Problems with overloading
  // 1:  def receive(future : Future[P2PResponse]): Int
    // this is not compiling because of the  type erasure
    // what happens here is that type of Generics is removed at run time
    // so it will look same
    /*
    2: Lifting doesnot work for all overloads
    val receive= receive _ // compiler will be confused at this case
    3: Code duplication : bcz impl for all these methods will be more or less same
    4: type inference and default arguments
      actor.receive(default argument we cant give bcz compiler again will get confuse)
     */
  }
  // this problem can be solved by type class pattern
  trait MessageMagnet[Result]{
    def apply():Result
  }
  // in actor api we will only have this receive method here
def receive[R](magnet: MessageMagnet[R]):R= magnet.apply()
  // now how we can make sure that this apply method somehow receive other types as well
  // this we can do by implicit conversion
  // these are type class instances which are helping to implement overloading receive method
  implicit class FromP2PRequest(request:P2PRequest) extends MessageMagnet[Int]{
    override def apply(): Int = {
      // all logic to handle P2P request
      println("handling p2p request")
      202
    }
  }

  implicit class FromP2PResponse(request:P2PResponse) extends MessageMagnet[Int]{
    override def apply(): Int = {
      // all logic to handle P2P response
      println("handling p2p response")
      203
    }
  }
  receive(new P2PRequest)
  receive(new P2PResponse)
}
