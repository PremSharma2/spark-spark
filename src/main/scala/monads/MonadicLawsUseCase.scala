package monads
import scala.concurrent.ExecutionContext.Implicits.global

object MonadicLawsUseCase  extends App {
/*
// Todo : Implementing Associativity Law
    Let’s say we have a bunch of users stored in some database.
    We also have a service that can load a user from that database with a method loadUser().
    It takes a name and provides us with an Option[User]
    because user with that name may or may not exist.
    Each user may or may not have a child (for the sake of the example
    let’s say there’s a law enforced in the state which allows a maximum of one child).
    Note that the child is also of type User, so it can have a child too.
      Last, but not least —
     we have a simple function getChild which returns the child for a given user.
     Now let’s say we want to load a user from the database and if they exist we want to see if they have a grandchild. We need to invoke these three functions:
    String → Option[User] // load from db
    User → Option[User] // get child
    User → Option[User] // get child’s child
 */
  trait User {
    val child: Option[User]
  }
  object UserService {
    def loadUser(name: String): Option[User] = ???
  }
  val getChild = (user: User) => user.child

  val result = UserService.loadUser("mike")
    .flatMap(getChild)
    .flatMap(getChild)

  //unit:     User => Option[User]
  //flatMap: (User => Option[User]) => Option[User]

  // TODO ------------------------------------------------------------------------------------


  /*
 TODO
  Future is a wrapper over some asynchronous operation.
   Once the future has been completed you can do whatever it is you need to do with its result.
   There are two main ways to use a future:
   use future.onComplete()
   to define a callback that will work with the result of the future (not so cool)
  use future.flatMap()
   to simply say which operations should be performed
   on the result once future is complete (cleaner and more powerful
  since you can return the result of the last operation)
  On to our example.
  We have an online store and customers who have placed thousands of orders. For each customer
    we must now get his/her order, check which item the order is for,
  get the corresponding item from the database, make the actual purchase and
   write the result of purchase operation to log. Let’s see that in code.
   */

  // needed for Futures to work
  import scala.concurrent.Future
  trait Order
  trait Item
  trait PurchaseResult
  trait LogResult
  object OrderService {
    def loadOrder(username: String): Future[Order] = ???
  }
  object ItemService {
    def loadItem(order: Order): Future[Item]  = ???
  }
  object PurchasingService {
    def purchaseItem(item: Item): Future[PurchaseResult]  = ???
    def logPurchase(purchaseResult: PurchaseResult): Future[LogResult]  = ???
  }
  val loadItem: Order => Future[Item] = {
    order => ItemService.loadItem(order)
  }
  val purchaseItem: Item => Future[PurchaseResult] = {
    item => PurchasingService.purchaseItem(item)
  }

  val logPurchase: PurchaseResult => Future[LogResult] = {
    purchaseResult => PurchasingService.logPurchase(purchaseResult)
  }


  // TODO or we can do that
  val result1 =
    OrderService.loadOrder("customerUsername")
      .flatMap(loadItem)
      .flatMap(purchaseItem)
      .flatMap(logPurchase)
}
