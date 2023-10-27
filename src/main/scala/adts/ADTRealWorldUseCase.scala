package adts

object ADTRealWorldUseCase {
  /**
  TODO
   In Scala, ADTs (Algebraic Data Types) are a powerful concept used to model data structures in a functional and type-safe manner.
   They are called "algebraic" because they can be understood through algebraic operations like sum (disjoint union) and product (combination).
   ADTs consist of two main types: Sum types and Product types.
TODO
  1. Sum types: Sum types are used to represent a value that can be one of several possible types.
  In Scala, we often use sealed traits and case classes to create sum types.
  A sealed trait can only be extended within the same file,
  which ensures that all possible subtypes are known and exhaustive pattern matching can be done.
TODO
    Real-world project use case:
  Consider a messaging application that can handle different types of messages:
  TextMessage, ImageMessage, and VideoMessage. We can represent this as a sum type:

  **/
  sealed trait Message
  case class TextMessage(content: String) extends Message
  case class ImageMessage(url: String, caption: String) extends Message
  case class VideoMessage(url: String, duration: Int) extends Message

  /**
   TODO
    Here, `Message` is a sealed trait, and `TextMessage`, `ImageMessage`, and `VideoMessage`
    are case classes extending the `Message` trait.
    By using a sum type, we can ensure that all message types are known and handled correctly throughout the application.
TODO
  2. Product types: Product types are used to represent a combination of values.
  In Scala, we often use case classes to create product types.
  A case class automatically provides a constructor and immutable properties.
TODO
  Real-world project use case:
  Consider an e-commerce application that needs to represent
  products with different attributes such as name, price, and category.
  We can use product types to model this:

   **/
/**

  TODO
   Here, `Product` is a case class representing a product with a name, price, and category.
   Using a product type makes it easy to create, update, and manipulate product data in a type-safe manner.
   In combination, sum types and product types provide
   a powerful way to model complex data structures in a clear and concise manner in Scala projects.
   They help ensure type safety and make the code more maintainable and less prone to runtime errors.

**/


    case class Product(name: String, price: Double, category: String)


}
