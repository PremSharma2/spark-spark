package ImplicitsAndTypeClasses

import scala.language.implicitConversions

object MagnetPattern1 {

  // 1. Define a Magnet Trait
  trait ResponseMagnet {
    type Result

    def apply(): Result
  }

  // 2. Create Implicit Magnet Constructors
  object ResponseMagnet {
    implicit def fromString(s: String): ResponseMagnet {
      type Result = String
    } = new ResponseMagnet {
      type Result = String

      def apply(): Result = s"Responding with a string: $s"
    }

    implicit def fromInt(i: Int): ResponseMagnet {
      type Result = String
    } = new ResponseMagnet {
      type Result = String

      def apply(): Result = s"Responding with an integer: $i"
    }

    implicit def fromStringList(list: List[String]): ResponseMagnet {
      type Result = String
    } = new ResponseMagnet {
      type Result = String

      def apply(): Result = s"Responding with a list: ${list.mkString(", ")}"
    }
  }

  // 3. Single Method Accepting Magnet
  def respond(magnet: ResponseMagnet): magnet.Result = magnet()

  // Real-world usage
  println(respond("Hello")) // Output: Responding with a string: Hello
  println(respond(42)) // Output: Responding with an integer: 42
  println(respond(List("a", "b"))) // Output: Responding with a list: a, b

}
