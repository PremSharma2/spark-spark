package ImplicitsAndTypeClasses

import java.util.Date

object JsonSerializationWithSyntacticSugar extends App {


  case class User(name: String, age: Int, email: String)

  case class Post(content: String, createdAt: Date)

  case class Feed(user: User, posts: List[Post])

  sealed trait JSONValue {
    def toJsonstring: String
  }

  final case class JsonDataObject(keyValuePair: Map[String, JSONValue]) extends JSONValue {
    override def toJsonstring: String = keyValuePair.map {
      case (key, value) => "\"" + key + "\":" + value.toJsonstring
    } // so for every lement in this iterable we are calling mkString
      .mkString("{", ",", "}")
  }

  final case class JsonString(values: String) extends JSONValue {
    override def toJsonstring: String = "\"" + values + "\""
  }

  final case class JsonNumber(values: Int) extends JSONValue {
    override def toJsonstring: String = values.toString
  }

  // This is special because it contains List of other JSONValue(other intermediate data types)
  final case class JsonArray(values: List[JSONValue]) extends JSONValue {
    override def toJsonstring: String = values.map(_.toJsonstring).mkString("[", ",", "]")
  }

  // here we are populating JsonDataObject from our application DO
  val jsonData = JsonDataObject.apply {
    Map(
      "user" -> JsonString("Prem"),
      "posts" -> JsonArray(List(JsonString("Scala-Rocks"),
        JsonNumber(453)))
    )
  }
  println(jsonData.toJsonstring)

  trait JsonFormatConverter[T] {
    def convert(value: T): JSONValue
  }

  implicit class JSONEnrichMent[T](value: T) {
    def toJSON(implicit converter: JsonFormatConverter[T]): JSONValue = converter.convert(value)

  }

  // thsese are for existing types
  implicit object StringConverter extends JsonFormatConverter[String] {
    override def convert(value: String): JSONValue = JsonString.apply(value)
  }

  implicit object NumberConverter extends JsonFormatConverter[Int] {
    override def convert(value: Int): JSONValue = JsonNumber.apply(value)
  }

  // This is Json representation for custom objects  USer,because in JSon
  // Custom object goes as Key,value pair that we will do using JSOnDataObject

  implicit object UserConverter extends JsonFormatConverter[User] {
    override def convert(user: User): JSONValue = JsonDataObject(Map(
      "name" -> JsonString(user.name),
      "age" -> JsonNumber(user.age),
      "email" -> JsonString(user.email)
    ))
  }

  // This is Json representation for custom objects  Post,because in JSon
  // Custom object goes as Key,value pair
  implicit object PostConverter extends JsonFormatConverter[Post] {
    override def convert(post: Post): JSONValue = JsonDataObject(Map(
      "content" -> JsonString(post.content),
      "createdDate:" -> JsonString(post.createdAt.toString)
    ))
  }

  // This is Json representation for custom objects  Feed,because in JSon
  // Custom object goes as Key,value pair
  implicit object FeedConverter extends JsonFormatConverter[Feed] {
    override def convert(feed: Feed): JSONValue = {
      // List of key value pair
      val jsonValuelist: List[JSONValue] = feed.posts.map(_.toJSON).toList
      JsonDataObject(Map(
        "user:" -> feed.user.toJSON,
        "posts:" -> JsonArray(jsonValuelist)
      ))
    }
  }

  // now step 2.3 is immplicit conversion


  val now = new Date(System.currentTimeMillis())
  val john = User("John", 43, "john@outlook.com")
  val feed = Feed(john, List(
    Post("Hello- Scala", now),
    Post("Look at the cute puppy", now)
  ))
  println(feed.toJSON.toJsonstring)
}
