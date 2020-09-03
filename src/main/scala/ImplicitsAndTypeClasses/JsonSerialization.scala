package ImplicitsAndTypeClasses

import java.util.Date

object JsonSerialization extends App {
/*
Serialize thse case classes
 */
  case class User(name:String, age:Int , email:String)
  case class Post(content:String, createdAt:Date )
  case class Feed(user:User, posts:List[Post] )

  /*
  1 Lets create the intermediate data types :-> Int ,
  String,List, Date because we need to JSON representation For these
  2 type classes for conversion form case classes to intermediate data types
  3 serialize those intermediate data types to JSON
   */
  //1: this is for intermediate data type
sealed trait JSONValue{
    def toJsonstring:String
  }
  /*
 This final case class will represent that intermediate Custom data
 This is the intermediate Single representation  for all pojos i.e all
 JsonValue intermediate object
 ** Note -> plus it will represent custom JsonValue (data object) like user , feed etc...
 we have defined above this we want to serialize
 Here one imp thing to notice is that
 json key is String like name age etc
 which is string and value is  JSONValue -> it could be  array/ string/Int ..... etc
 i.e JSONValue is representing for All intermediate data type like Int ,List,Date etc....
we will multiple final case classes to represent different type to intermediate data types
but as far as JsonDataObject is concern This is basically the whole DO which represent whole json
which contains all json values i,e Collection of key value pair of JsonValues
because Custom object in json are represented as Key, Value pairs
Like this i.e the final JSon representation which is going to serialize now
 {
 name:"Prem"
 age: 22
 friends : [......]
 this is nested object Post which again a key value pair
 latestPost : {
 content: "Scala Rocks"
 createdAt: 23-june-2019
 }
 }
  */
final case class JsonDataObject(keyValuePair: Map[String,JSONValue]) extends JSONValue {
  override def toJsonstring: String = keyValuePair.map{
    case (key,value) => "\"" + key + "\":" + value.toJsonstring
  }// so for every lement in this iterable we are calling mkString
    .mkString("{",",","}")
}
  final case class JsonString(values: String) extends JSONValue{
    override def toJsonstring: String = "\"" + values + "\""
  }

  final case class JsonNumber(values: Int) extends JSONValue{
    override def toJsonstring: String = values.toString
  }
  // This is special because it contains List of other JSONValue(other intermediate data types)
  final case class JsonArray(values: List[JSONValue]) extends JSONValue{
    override def toJsonstring: String = values.map(_.toJsonstring).mkString("[",",","]")
  }
  // here we are populating JsonDataObject from our application DO
  val jsonData= JsonDataObject.apply{
    Map(
      "user" -> JsonString("Prem"),
       "posts" -> JsonArray(List(JsonString("Scala-Rocks"),
                                 JsonNumber(453)))
    )
  }
println(jsonData.toJsonstring)
  /*
  2 step is to implement type class pattern for implicit conversion
  for that We need to have
  1 Type classes
  2 Type class companion
  3 Type class instances
  4 pimp my library to use type class instances
  Lets implement it
   */
  // This is our type class this is step 2.1
  trait JsonFormatConverter[T]{
    def convert(value : T):JSONValue
  }
  // thsese are for existing types
implicit object StringConverter extends JsonFormatConverter[String]{
  override def convert(value: String): JSONValue = JsonString.apply(value)
}

  implicit object NumberConverter extends JsonFormatConverter[Int]{
    override def convert(value: Int): JSONValue = JsonNumber.apply(value)
  }
  // This is Json representation for custom objects  USer,because in JSon
  // Custom object goes as Key,value pair that we will do using JSOnDataObject

  implicit object UserConverter extends JsonFormatConverter[User]{
    override def convert(user: User): JSONValue = JsonDataObject(Map(
      "name" -> JsonString(user.name),
      "age"  -> JsonNumber(user.age),
      "email" -> JsonString(user.email)
    ))
  }
  // This is Json representation for custom objects  Post,because in JSon
  // Custom object goes as Key,value pair
  implicit object PostConverter extends JsonFormatConverter[Post]{
    override def convert(post: Post): JSONValue = JsonDataObject(Map(
      "content" -> JsonString(post.content),
      "createdDate:"  -> JsonString(post.createdAt.toString)
    ))
  }
  // This is Json representation for custom objects  Feed,because in JSon
  // Custom object goes as Key,value pair
  implicit object FeedConverter extends JsonFormatConverter[Feed]{
    override def convert(feed: Feed): JSONValue ={
      // List of key value pair
      val jsonValuelist: List[JSONValue] =feed.posts.map(PostConverter.convert(_)).toList
      JsonDataObject(Map(
      "user:" -> UserConverter.convert(feed.user),
      "posts:"  -> JsonArray(jsonValuelist)
    ))
  }}
  // now step 2.3 is immplicit conversion

  implicit class JSONEnrichMent[T] (value :T){
    def toJSON(implicit converter:JsonFormatConverter[T]):JSONValue = converter.convert(value)

  }
  val now = new Date(System.currentTimeMillis())
  val john= User("John",43,"john@outlook.com")
  val feed= Feed(john, List(
    Post("Hello- Scala", now),
    Post("Look at the cute puppy",now)
  ))
  println(feed.toJSON.toJsonstring)
  /*
  output will be

  {
 name:"john",
 age: 43,
 email:john@outlook.com
 friends : [......]
 this is nested object Post which again a key value pair
 post : {
 content: "Scala Rocks"
 createdAt: 23-june-2019
 }
 }

 Its is top most json inside this big json
we have key value pair
here Key is string and value is json of user
and again in next key value pair
pst as key and value is listofJson
{"user:"{"name":"John","age":43,"email":"john@outlook.com"},
"posts:":[{"content":"Hello- Scala","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}
,{"content":"Look at the cute puppy","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}]}
   */
}

