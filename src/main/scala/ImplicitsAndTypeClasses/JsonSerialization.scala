package ImplicitsAndTypeClasses

import java.util.Date

object JsonSerialization extends App {
/*
Serialize these case classes
 */
  case class User(name:String, age:Int , email:String)
  case class Post(content:String, createdAt:Date )
  case class Feed(user:User, posts:List[Post] )

  /*
  1 Lets create the intermediate data types :-> Int , String,List, Date
   because we need to JSON representation For these
 We will pass these intermediate data types to Json Data Object
 which is object representation of JSoN
   */
  // this is the contract for intermediate data types
sealed trait JSONValue{
    def toJSONString:String
  }
  /*
 This final case class will represent that intermediate Custom data
 This is the intermediate Single representation  for all pojos i.e all
 JsonValue intermediate object
 ** Note -> plus it will represent custom JsonValue (data object) like user , feed etc...
 we have defined above this we want to serialize
 Here one imp thing to notice is that
 json key is String like name age etc
 which is string and value is  JSONValue which is  (intermediate data types)-> it could be  array/ string/Int ..... etc
 i.e JSONValue is representing for All intermediate data type like Int ,List,Date etc....
we will multiple final case classes to represent different type to intermediate data types
but as far as JsonDataObject is concern it is a Map[Key,Value]
key is string and value could be another JSON i.e nested Json JsonDataObject  or JsonValue
like this
{
 name:"Prem"
 age: 22
 friends : [......]
 this is nested object Post which again a key value pair
 latestPost : {
 content: "Scala Rocks"
 createdAt: 23-june-2019
 }
This is basically the whole DO which represent whole json
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
 or
 {"user:"{"name":"John","age":43,"email":"john@outlook.com"},
"posts:":[{"content":"Hello- Scala","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}
,{"content":"Look at the cute puppy","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}]}
  */
final case class JsonDataObject(keyValuePair: Map[String,JSONValue]) extends JSONValue {
  override def toJSONString: String = keyValuePair.map{
        // here we are List("key":value, "key":value) collecting key value as string in list
        // first iteration of user will give
        // List("user" :"{"name":"John","age":43,"email":"john@outlook.com"})
    case (key,value) => "\"" + key + "\":" + value.toJSONString
  }// so for every lement in this iterable we are calling mkString
    .mkString("{",",","}")
}
  final case class JsonString(value: String) extends JSONValue{
    override def toJSONString: String = "\"" + value + "\""
  }

  final case class JsonNumber(value: Int) extends JSONValue{
    override def toJSONString: String = value.toString
  }
  // This is special because it contains List of other JSONValue(other intermediate data types)
  // input is List(JsonDataObject(Map(
  // content -> JsonString(Hello- Scala),
  // createdDate: -> JsonString(Tue Sep 08 08:41:38 BST 2020))))
  final case class JsonArray(value: List[JSONValue]) extends JSONValue{
    //When using mkString with a Scala array, list, seq, etc.,
    // you can also define a prefix, suffix, and element separator, as shown in these examples:
    //[{"content":"Hello- Scala","createdDate:":"Mon Sep 07 10:25:07 BST 2020"}]
    override def toJSONString: String = {
      // output of this is {"content":"Hello- Scala","createdDate:":"Tue Sep 08 09:25:05 BST 2020"}
      val convertedJsontoJsonString: Seq[String] =value.map(_.toJSONString)
     val jsonString= convertedJsontoJsonString.mkString("[",",","]")
      jsonString
    }
  }
  // here we are populating JsonDataObject from our application DO

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
  trait JsonConverter[T]{
    def convert(value : T):JSONValue
  }
  // thsese are for existing types
implicit object StringConverter extends JsonConverter[String]{
  override def convert(value: String): JSONValue = JsonString.apply(value)
}

  implicit object NumberConverter extends JsonConverter[Int]{
    override def convert(value: Int): JSONValue = JsonNumber.apply(value)
  }
  // This is Json representation for custom objects  USer,because in JSon
  // Custom object goes as Key,value pair that we will do using JSOnDataObject

  implicit object UserConverter extends JsonConverter[User]{
    override def convert(user: User): JSONValue = JsonDataObject(Map(
      "name" -> JsonString(user.name),
      "age"  -> JsonNumber(user.age),
      "email" -> JsonString(user.email)
    ))
  }
  // This is Json representation for custom objects  Post,because in JSon
  // Custom object goes as Key,value pair
  implicit object PostConverter extends JsonConverter[Post]{
    override def convert(post: Post): JSONValue = JsonDataObject(Map(
      "content" -> JsonString(post.content),
      "createdDate:"  -> JsonString(post.createdAt.toString)
    ))
  }
  // This is Json representation for custom objects  Feed,because in JSon
  // Custom object goes as Key,value pair
  implicit object FeedConverter extends JsonConverter[Feed]{
    override def convert(feed: Feed): JSONValue ={
      // List of key value pair it will give List of Json data object of post type
      //List(JsonDataObject(Map(content -> JsonString(Hello- Scala),
      //                  createdDate: -> JsonString(Mon Sep 07 07:40:32 BST 2020))))
      val jsonValuelist: List[JSONValue] =feed.posts.map(PostConverter.convert(_)).toList
      val userJson= UserConverter.convert(feed.user)
      // populating feedjson for feed DO
      JsonDataObject(Map(
      "user:" -> userJson,
      "posts:"  -> JsonArray(jsonValuelist)
    ))
  }}
  // now step 2.3 is immplicit conversion

  implicit class JSONEnrichMent[T] (value :T){
    def toJSON(implicit converter:JsonConverter[T]):JSONValue = converter.convert(value)

  }
  val now = new Date(System.currentTimeMillis())

  val jsonData= JsonDataObject.apply{
    Map(
      "user" -> JsonString("Prem"),
      "posts" -> JsonArray(List(JsonString("Jai Mata De"),JsonNumber(111)))

    )
  }
  println(jsonData.toJSONString)


  /*
  {"user":"Prem","posts":["Jai Mata De",111]}
   */
  /*val user= User("John",43,"john@outlook.com")
  // so we need to convert the Feed object and inside feed we have post and user
  val feed= Feed(user, List(
    Post("Hello- Scala", now)
  ))
   */

 // println(feed.toJSON.toJSONString)
  /*
  output will be Feed Json like this which contains user and post JSON as well
  {"user:"{"name":"John","age":43,"email":"john@outlook.com"},
"posts:":[{"content":"Hello- Scala","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}
,{"content":"Look at the cute puppy","createdDate:":"Thu Sep 03 07:54:37 BST 2020"}]}

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

