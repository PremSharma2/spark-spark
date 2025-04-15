package concurrency

object MonadicFuture {
  /*

---

 Understand the monadic flow of Futures

Let's start with your code:


val mark: Future[Profile] = SocialNetwork.toFetchProfile("fb.id.1.zuck")
val nameOnTheWall: Future[String] = mark.map(profile => profile.name)
val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))


---

 1. 🧠 What is `Future[T]`?

`Future[T]` is a container for a value of type `T` that will be available later (asynchronously) — it may succeed or fail.

---

 2. 🎁 The Promise Behind the Curtain

Internally, when you do:


val future = Future { calculateSomething() }


Scala wraps this like:


val promise = Promise[T]()
val runnable = new Runnable {
  def run(): Unit = {
    try {
      val result = calculateSomething()
      promise.success(result)
    } catch {
      case e => promise.failure(e)
    }
  }
}
executionContext.execute(runnable)
val future = promise.future


So a `Future` is just a read-only wrapper over a Promise, which is completed later by a background thread.

---

 3. 🔍 Now, what does `.map` do in the Future?

You wrote:


val nameOnTheWall: Future[String] = mark.map(profile => profile.name)


This is monadic mapping: given a `Future[Profile]`, transform it into a `Future[String]` by mapping the result when it becomes available.

# Behind the scenes:

Here's what the `map` call actually looks like:


def map[S](f: T => S)(implicit executor: ExecutionContext): Future[S] =
  transform(_ map f)


Which itself is built upon:


def transform[S](f: Try[T] => Try[S])(implicit executor: ExecutionContext): Future[S]


And finally internally:

** You register a callback using onComplete

** That callback needs to be run asynchronously when the original future finishes

** That callback is submitted to the provided ExecutionContext

def transform[S](f: Try[T] => Try[S])(implicit executor: ExecutionContext): Future[S] = {
  val p = Promise[S]()

  this.onComplete { result =>
    try {
      val transformed = f(result)
      p.complete(transformed)
    } catch {
      case e: Throwable => p.failure(e)
    }
  }(executor)

  p.future
}



---

# So what happens step by step:

1. You call `.map(profile => profile.name)` on `mark: Future[Profile]`
2. `Future.map` creates a new `Promise[String]`
3. It registers a callback on the original `Future[Profile]` via `.onComplete`
4. When `mark` completes (succeeds or fails):
   - If it’s a success, it applies `profile => profile.name` and completes the new promise
   - If it’s a failure, it propagates the error to the new promise
5. The `.future` of that new promise is returned: a `Future[String]`

> ✅ You're not mutating or blocking. You're reacting to a result — that's the essence of reactive programming.

---

 4. 🔄 What does `.flatMap` do in the Future?

You wrote:


val marksBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))


This is different than `.map` — you're returning another `Future` inside the function.

Here's the internal of `.flatMap`:


def flatMap[S](f: T => Future[S])(implicit executor: ExecutionContext): Future[S] =
  transformWith {
    case Success(t) => f(t)     // different here! it "flattens" the nested Future
    case Failure(e) => this.asInstanceOf[Future[S]]
  }


Which uses:


def transformWith[S](f: Try[T] => Future[S])(implicit executor: ExecutionContext): Future[S]


And this works like:


vdef transformWith[S](f: Try[T] => Future[S])(implicit executor: ExecutionContext): Future[S] = {
  val p = Promise[S]() // a final result will go here

  this.onComplete { result: Try[T] =>
    try {
      val newFuture: Future[S] = f(result)
      newFuture.onComplete(p.tryComplete) // pipe final result into p
    } catch {
      case e: Throwable => p.failure(e)
    }
  }(executor)

  p.future
}



---

# So what's happening here:

1. `mark: Future[Profile]` hasn't completed yet.
2. You call `.flatMap(profile => fetchBestFriend(profile))`
3. This registers a callback: once `mark` completes with a `Profile`, call `fetchBestFriend(profile)` which returns a new `Future`
4. The returned `Future` (`marksBestFriend`) is the result of that second async call
5. You get a new chained `Future[Profile]`

✅ `flatMap` lets you chain dependent async operations. It’s key to for-comprehension!

---

 5. 📦 Why is this monadic?

Because `Future` implements:

- `map` → transforms a successful value
- `flatMap` → sequences/chain dependent async computations
- `withFilter` → filtering in for-comprehensions

# For example:


val f = for {
  profile <- SocialNetwork.toFetchProfile("zuck")
  bestie <- SocialNetwork.fetchBestFriend(profile)
} yield s"${profile.name} is besties with ${bestie.name}"


This is sugar for:


SocialNetwork.toFetchProfile("zuck").flatMap { profile =>
  SocialNetwork.fetchBestFriend(profile).map { bestie =>
    s"${profile.name} is besties with ${bestie.name}"
  }
}


---

 🎯 Summary of the Event Flow


val mark = Future { SocialNetwork.toFetchProfile("zuck") }

val nameOnTheWall = mark.map(profile => profile.name)

val bestie = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))


| Step | What happens |
|------|--------------|
| 1 | A thread is scheduled to run `toFetchProfile("zuck")` |
| 2 | When done, it completes the Future `mark` |
| 3 | `mark.map(...)` and `mark.flatMap(...)` both had registered callbacks via `onComplete` |
| 4 | These callbacks fire — one maps `profile.name`, the other triggers another async Future |
| 5 | `Future` returns new Promises for each transformed result |

---

 🧪 Bonus: Want to See the Flow in Action?

Here’s a tiny mock simulation:


case class Profile(id: String, name: String)
object SocialNetwork {
  def toFetchProfile(id: String): Future[Profile] = Future {
    Thread.sleep(500)
    println(s"Fetching profile $id")
    Profile(id, "Zuckerberg")
  }

  def fetchBestFriend(profile: Profile): Future[Profile] = Future {
    Thread.sleep(300)
    println(s"Fetching best friend of ${profile.name}")
    Profile("fb.id.2", "Bestie")
  }
}

val mark = SocialNetwork.toFetchProfile("fb.id.1.zuck")
val bestie = mark.flatMap(p => SocialNetwork.fetchBestFriend(p))
val name = mark.map(_.name)

bestie.onComplete(println)
name.onComplete(println)


---

Let me know if you'd like:
- A visual diagram of Future + Promise flow
- More about `ExecutionContext` internals
- Or custom Future implementation

I'm happy to guide you to expert level 🚀
   */

}
