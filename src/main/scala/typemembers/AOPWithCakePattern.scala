package typemembers

import typemembers.AOPWithCakePattern.DaoImpl.InMemoryShoppingCartRepositoryComponent
import typemembers.AOPWithCakePattern.ServiceImpl.BasicShoppingCartServiceComponent

object AOPWithCakePattern {



  // Main Business Logic
  trait ShoppingCartComponent {
    def addItem(item: String): Unit
    def removeItem(item: String): Unit
  }

  // Cross-Cutting Concerns
  trait LoggerComponent {
    def log(message: String): Unit
  }

  trait SecurityComponent {
    def isAuthenticated(): Boolean
  }



  /**
 TODO
     Add Cross-Cutting Concerns to Main Logic (Point-cuts)
   Now, let's define traits that add cross-cutting logic to our main component.
   These traits will serve as our point-cuts.
   */

  trait LoggedShoppingCartComponent extends ShoppingCartComponent {
    this: LoggerComponent =>

     override def addItem(item: String): Unit = {
      log(s"Attempting to add item: $item")
      //super.addItem(item)
      log(s"Item added: $item")
    }

     override def removeItem(item: String): Unit = {
      log(s"Attempting to remove item: $item")
      //super.removeItem(item)
      log(s"Item removed: $item")
    }
  }

  trait SecureShoppingCartComponent extends ShoppingCartComponent {
    this: SecurityComponent =>

    abstract override def addItem(item: String): Unit = {
      if (isAuthenticated()) {
        super.addItem(item)
      } else {
        println("Unauthorized attempt to add an item.")
      }
    }

    abstract override def removeItem(item: String): Unit = {
      if (isAuthenticated()) {
        super.removeItem(item)
      } else {
        println("Unauthorized attempt to remove an item.")
      }
    }
  }

/**
TODO
    Implement Concrete Components
   Now, let's implement our components.

 */

trait BasicShoppingCartComponent extends ShoppingCartComponent {
  var items: List[String] = List()

  def addItem(item: String): Unit = {
    items = item :: items
  }

  def removeItem(item: String): Unit = {
    items = items.filter(_ != item)
  }
}


  trait ConsoleLoggerComponent extends LoggerComponent {
    def log(message: String): Unit = println(s"LOG: $message")
  }

  trait SimpleSecurityComponent extends SecurityComponent {
    def isAuthenticated(): Boolean = true  // Assume always authenticated for this example
  }



  // Repository or DAO Layer
  trait ShoppingCartRepositoryComponent {
    def getItems(): List[String]
    def insertItem(item: String): Unit
    def deleteItem(item: String): Unit
  }

  // Service Layer
  trait ShoppingCartServiceComponent {
    this: ShoppingCartRepositoryComponent =>

    def addItemToCart(item: String): Unit
    def removeItemFromCart(item: String): Unit
  }

object DaoImpl {
  // Implement the DAO Layer
  trait InMemoryShoppingCartRepositoryComponent extends ShoppingCartRepositoryComponent {
    var items: List[String] = List()

    def getItems(): List[String] = items

    def insertItem(item: String): Unit = items = item :: items

    def deleteItem(item: String): Unit = items = items.filter(_ != item)
  }
}
  object ServiceImpl {
    // Implement the Service Layer
    trait BasicShoppingCartServiceComponent extends ShoppingCartServiceComponent {
      // todo : this is for dependency injection
      this: ShoppingCartRepositoryComponent =>

      def addItemToCart(item: String): Unit = insertItem(item)

      def removeItemFromCart(item: String): Unit = deleteItem(item)
    }

  }


  object ShoppingCartApp extends App
    with InMemoryShoppingCartRepositoryComponent
    with ConsoleLoggerComponent
    with SimpleSecurityComponent
    with BasicShoppingCartServiceComponent
    with LoggedShoppingCartComponent
    with SecureShoppingCartComponent {

    addItemToCart("Apple")
    removeItemFromCart("Apple")

  }

}
