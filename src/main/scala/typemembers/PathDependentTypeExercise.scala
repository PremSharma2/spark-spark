package typemembers

object PathDependentTypeExercise {

  /**
   * TODO
   *  Extensible Storage Systems: Imagine you're designing a storage system
   *  that can store different types of items, and each item type has its unique key type.
   *  link with that type or coupled with that type
   */

  trait ItemLike {
    type key
  }

  trait Item[K] extends ItemLike {
    override type key = K
  }

  class IntItem(val key: Int) extends Item[Int] {
    override type key = Int
  }

  class StringItem(val key: String) extends Item[String] {
    // key is of type String
  }


  trait Storage[ItemType <: ItemLike] {
    def store(item: ItemType): ItemType#key
//todo The # notation means you're referring to a type
// that's defined inside of another type,
// but you're not tying it to any particular instance of the outer type.
// This is in contrast to path-dependent types,
// which would tie abstractMemberType to a specific instance of Outer.
    def retrieve(key: ItemType#key): Option[ItemType]
  }

  class IntStorage extends Storage[IntItem] {
    private var storage = Map[Int, IntItem]()

    override def store(item: IntItem): Int = {
      // logic to store IntItem
      storage += (item.key -> item)
      item.key
    }

    override def retrieve(key: Int): Option[IntItem] = storage.get(key)
  }

  class StringStorage extends Storage[StringItem] {
    private var storage = Map[String, StringItem]()

    override def store(item: StringItem): String = {
      // logic to store StringItem
      storage += (item.key -> item)
      item.key
    }

    override def retrieve(key: String): Option[StringItem] = storage.get(key)
  }

}
