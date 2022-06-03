package zio

object StructuringLayersUsingZio {
  /*
  TODO
     In a real application,
     we often need to create heavyweight data structures which are important for various operations.
    The list is longer than we like to admit, but some critical operations usually include
    1:interacting with a database or storage layer
   2: doing business logic
   3: serving a front-facing API, perhaps through HTTP
   4: communicating with other services

 TODO
   Now, if we think about it,
   most of these data structures are created through some sort of effect: for example,
   creating a connection pool,
   reading from some configuration file, opening network ports, etc.


 TODO
   We can therefore conveniently think of these services as a particular kind of effect.
   ZIO matches this pattern perfectly:
TODO
   1:a service may have dependencies, therefore “inputs” or “environment”
   2:a service may fail with an error
   3:a service, once created, may serve as dependency or input to other services

TODO
   This style of thinking about a service is the core idea behind a ZLayer

TODO
   For the rest of this article,
   we’ll write a skeleton for an email newsletter service
   that automatically gives a user a welcome email, once subscribed.
   The implementations are console-based,
   but they can be easily replaced by a real database or a real email service.
   The goal of this example is to show you how to plug together independent components of your application.
   */
}
