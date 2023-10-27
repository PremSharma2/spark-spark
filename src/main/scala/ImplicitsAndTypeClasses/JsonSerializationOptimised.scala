package ImplicitsAndTypeClasses

object JsonSerializationOptimised {


  import java.util.Date

  object JsonSerialization extends App {
    case class User(name: String, age: Int, email: String)
    case class Post(content: String, createdAt: Date)
    case class Feed(user: User, posts: List[Post])

    trait JSONValue {
      def toJSONString: String
    }

    /*
 TODO
       val jsonData= JsonDataObject.apply{
  //    Map(
  //      "user" -> JsonString("Prem"),
  //      "posts" -> JsonArray(List(JsonString("Scala Rocks"),JsonNumber(spark)))
  TODO : posts
   JsonDataObject(Map(
      "content" -> JsonString(post.content),
      "createdDate:"  -> JsonString(post.createdAt.toString)
    ))
   JSON representation {"user":"Prem","posts":["Jai Mata De",111]}
    "name" -> JsonString(user.name),
      "age"  -> JsonNumber(user.age),
      "email" -> JsonString(user.email)
  TODO
      JsonDataObject(Map(
      "user:" -> userJson,
      "posts:"  -> JsonArray(postJson) // because posts is Arrayof JSON
    ))

    {
  "user:"{"name":"John","age":43,"email":"john@outlook.com"},
"posts:":[{"content":"Hello- Scala","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}
,{"content":"Look at the cute puppy","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}]
}
     */

    // API to handle the JSON
    final case class JsonDataObject(keyValuePair: Map[String, JSONValue]) extends JSONValue {
      override def toJSONString: String = {
        val sb = new StringBuilder("{")
        keyValuePair.foreach {
          case (key, value) =>
            sb.append("\"")
              .append(key)
              .append("\":")
              .append(value.toJSONString)
              .append(',')
        }
        if (keyValuePair.nonEmpty) sb.setLength(sb.length - 1)
        sb.append("}").toString
      }
    }

    final case class JsonString(value: String) extends JSONValue {
      override def toJSONString: String = "\"" + value + "\""
    }

    final case class JsonNumber(value: Int) extends JSONValue {
      override def toJSONString: String = value.toString
    }

    final case class JsonArray(value: List[JSONValue]) extends JSONValue {
      override def toJSONString: String = {
        value.map(_.toJSONString).mkString("[", ",", "]")
      }
    }


    //type class of type enrichment
    trait JsonConverter[T] {
      def convert(value: T): JSONValue
    }

    implicit object StringConverter extends JsonConverter[String] {
      override def convert(value: String): JSONValue = JsonString(value)
    }

    implicit object NumberConverter extends JsonConverter[Int] {
      override def convert(value: Int): JSONValue = JsonNumber(value)
    }


    implicit object UserConverter extends JsonConverter[User] {
      override def convert(user: User): JSONValue = JsonDataObject(Map(
        "name" -> JsonString(user.name),
        "age" -> JsonNumber(user.age),
        "email" -> JsonString(user.email)
      ))
    }


    implicit object PostConverter extends JsonConverter[Post] {
      override def convert(post: Post): JSONValue = JsonDataObject(Map(
        "content" -> JsonString(post.content),
        "createdAt" -> JsonString(post.createdAt.toString)
      ))
    }


    implicit object FeedConverter extends JsonConverter[Feed] {
      override def convert(feed: Feed): JSONValue = {
        val postJson: List[JSONValue] = feed.posts.map(PostConverter.convert)
        JsonDataObject(Map(
          "user" -> UserConverter.convert(feed.user),
          "posts" -> JsonArray(postJson)
        ))
      }
    }


    implicit class JSONEnrichment[T](value: T) {
      def toJSON(implicit converter: JsonConverter[T]): JSONValue = converter.convert(value)
    }

    val now = new Date(System.currentTimeMillis())

    val userJson=User("John", 43, "john@outlook.com").toJSON
    val feedJSon=feed.toJSON
    val feed = Feed(User("John", 43, "john@outlook.com"), List(
      Post("Hello- Scala", now)
    ))

    println(feed.toJSON.toJSONString)
  }

}
