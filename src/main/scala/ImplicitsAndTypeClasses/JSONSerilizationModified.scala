package ImplicitsAndTypeClasses

import java.util.Date

object JsonSerialization extends App {

  /**
   * Domain Models that we want to serialize to JSON.
   */
  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /**
   * JSON Abstract Syntax Tree (AST)
   *
   * We represent any JSON value (primitive or nested) using the JsonElement trait.
   * All data types, i.e., in the form of Adts that can appear in a JSON must be encoded to one of these case classes.
   */
  sealed trait JsonElement {
    def toJsonString: String
  }

  /**
   * Represents a JSON object (i.e., Map[String, JsonElement]).
   * Used for serializing custom classes like User, Post, Feed.
   */
  final case class JsonObjectElement(fields: Map[String, JsonElement]) extends JsonElement {
    override def toJsonString: String =
      fields.map { case (key, value) => s""""$key":${value.toJsonString}""" }
        .mkString("{", ",", "}")
  }

  // Represents a JSON string value
  private final case class JsonStringElement(value: String) extends JsonElement {
    override def toJsonString: String = "\"" + value + "\""
  }

  // Represents a JSON number (simplified to Int for now)
  private final case class JsonNumberElement(value: Int) extends JsonElement {
    override def toJsonString: String = value.toString
  }

  // Represents a JSON array of JsonElement values
  private final case class JsonArrayElement(elements: List[JsonElement]) extends JsonElement {
    override def toJsonString: String = elements.map(_.toJsonString).mkString("[", ",", "]")
  }


  /**
   * Type Class: JsonEncoder[T]
   *
   * This trait defines how a type T can be encoded as a JsonElement i.e Json Adts or type enrichment to Json Adts.
   */
  trait JsonEncoder[T] {
    def encode(value: T): JsonElement
  }

  /**
   * Type Class Instances for basic types of type class JsonEncoder and domain models.
   */

  // String → JsonStringElement
  implicit val stringEncoder: JsonEncoder[String] = JsonStringElement.apply

  // Int → JsonNumberElement
  implicit val intEncoder: JsonEncoder[Int] = JsonNumberElement.apply

  // Date → JsonStringElement (using toString format)
  implicit val dateEncoder: JsonEncoder[Date] = date => JsonStringElement(date.toString)

  // Recursive encoder for List[T] → JsonArrayElement
  implicit def listEncoder[T](implicit elementEncoder: JsonEncoder[T]): JsonEncoder[List[T]] =
    list => JsonArrayElement(list.map(elementEncoder.encode))

  // User → JsonObjectElement
  implicit val userEncoder: JsonEncoder[User] = user => JsonObjectElement(Map(
    "name"  -> user.name.toJsonElement,
    "age"   -> user.age.toJsonElement,
    "email" -> user.email.toJsonElement
  ))

  // Post → JsonObjectElement
  implicit val postEncoder: JsonEncoder[Post] = post => JsonObjectElement(Map(
    "content"     -> post.content.toJsonElement,
    "createdDate" -> post.createdAt.toJsonElement
  ))

  // Feed → JsonObjectElement (user and list of posts)
  implicit val feedEncoder: JsonEncoder[Feed] = feed => JsonObjectElement(Map(
    "user"  -> feed.user.toJsonElement,
    "posts" -> feed.posts.toJsonElement
  ))

  /*
   * Syntax Enrichment
   *
   * Adds a `.toJsonElement` method to any type that has an implicit JsonEncoder.
   */
  implicit class JsonSyntax[T](value: T) {
    def toJsonElement(implicit encoder: JsonEncoder[T]): JsonElement = encoder.encode(value)
  }

  /*
   * Usage Example
   */
  val now = new Date()

  val jsonData = JsonObjectElement(Map(
    "user" -> "Prem".toJsonElement,
    "posts" -> JsonArrayElement(List("Jai Mata De".toJsonElement, 111.toJsonElement))
  ))

  println(jsonData.toJsonString)

  val feed = Feed(
    User("John", 43, "john@outlook.com"),
    List(
      Post("Hello- Scala", now),
      Post("Look at the cute puppy", now)
    )
  )

  println(feed.toJsonElement.toJsonString)
}
