package lazyevaluation

import java.util.NoSuchElementException

import scala.annotation.tailrec

object CustomStreamsUsingLazyEvaluation extends App {


 trait MyStream[+A] {
    def head: A

    def tail: MyStream[A]

    def filter(predicate: A => Boolean): MyStream[A]

    def isEmpty: Boolean


    def #::[B >: A](element: B): MyStream[B]
   // here also another stream is of type Call by name

    def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]

    def forEach(fx: A => Unit): Unit

    def map[B](fx: A => B): MyStream[B]

    def flatMap[B](fx: A => MyStream[B]): MyStream[B]

    def take(n: Int): MyStream[A]

    def takeAsList(n: Int): List[A] = take(n).toList()

    /*
    [1,2,3].toList([])=
    [2,3].toList([1]) =
     [3].toList([2,1])
     Empty.toList([3,2,1])
     */
    @tailrec
    final def toList[B >: A](accumulator: List[B] = Nil): List[B] =
      if (this.isEmpty) accumulator.reverse
      else tail.toList(head :: accumulator)
  }


  object EmptyStream extends MyStream[Nothing] {
    override def head: Nothing = throw new NoSuchElementException

    override def tail: MyStream[Nothing] = throw new NoSuchElementException

    override def isEmpty: Boolean = true


    override def #::[B >: Nothing](element: B): MyStream[B] = new Node[B](element, this)

    override def ++[B >: Nothing](anotherStream: =>  MyStream[B]): MyStream[B] = anotherStream

    override def forEach(fx: Nothing => Unit): Unit = ()

    override def map[B](fx: Nothing => B): MyStream[B] = this

    override def flatMap[B](fx: Nothing => MyStream[B]): MyStream[B] = this

    override def take(n: Int): MyStream[Nothing] = this

    override def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

    override def takeAsList(n: Int): List[Nothing] = Nil
  }

  class Node[+A](hd: A, lazyTail: => MyStream[A]) extends MyStream[A] {
    //tl: => MyStream[A] its callbyname expression which returns MyStream[A]
    override val head: A = hd

    override lazy val tail: MyStream[A] = lazyTail // it is called callByneed

    override def isEmpty: Boolean = false

    /*
  TODO
    val s=new Node(1,EmptyStream)
    // here s is an expression which will be evaluated later
    val prepended=2 #:: s= new Cons(1,s)
    here s will be evaluated later bcz its lazy,
    so s remains unevaluated when prepend operator acts
    i.e until some one access (lazy val tail) it will not evaluated
   null  i.e call by need
   val myLazyStream: MyStream[Int] = new Node(1, new Node(2, new Node(3, ... )))
   Here, when you first access myLazyStream.tail,
   the lazyTail of the first node gets evaluated,
   which is actually the entire expression new Node(2, new Node(3, ... )).
   This result is then stored in tail.
   new Node(2, new Node(3, ...)) in place of this it could be tail recursive expression or tail expression
   1 #:: new Node(2, new Node(3, ...))
     */

    override def #::[B >: A](element: B): MyStream[B] = new Node[B](element, this)

    /*
   TODO
    (lazy val lazytail=tail ++ anotherStream) this expression
    will remain unevaluated because it tail is lazy and byName
    here lazytail will be evaluated later bcz its lazy,
    so s remains unevaluated when prepend operator acts
    as soon as you go ahead with tail it will start evaluating
    the expression lazilyEvaluatedTail =tail ++ anotherStream
    will get evaluated to evaluate tail of  val lazystream=new Node[B](head, tail ++ anotherStream) Stream
    FOR EXAMPLE: -> lazystream.tail
    that means this head, tail ++ anotherStream thi recursive call to ++ function
    will only be made on demand
    i.e until some one access (lazy val tail) it will not evaluated
    i.e call by need
    Because anotherStream is a call-by-name parameter and
    tail is a lazy val, the new tail (tail ++ anotherStream) is
    also lazily evaluated.
    That means the actual concatenation doesn't happen
    until you try to access the elements of the resulting stream.
    This is in line with the lazy nature of a stream,
    where elements are computed only when they are accessed.
    Example:
    stream1 = Node(1, Node(2, Node(3, ...)))
    stream2 = Node(4, Node(5, Node(6, ...)))
    val stream3 = stream1 ++ stream2
    stream3 will be a new Node with:
TODO
   head as 1 (the head of stream1)
   tail as a lazily-evaluated stream representing the concatenation of stream1.tail and stream2.
   So when you access stream3.tail,
   only then will the tail be actually computed,
   and it will be Node(2, Node(3, Node(4, Node(5, Node(6, ...))))).

     */
// here we changed (anotherStream:  MyStream[B]) to (anotherStream: => MyStream[B])
    // because it was eagerly evaluating so stack was blowing
    // so we made this callybyname so that it will x
    override def ++ [B >: A](anotherStream: => MyStream[B]): MyStream[B] =
      new Node[B](head, tail ++ anotherStream)

    override def forEach(fx: A => Unit): Unit = {
      /*
      This is termination operation this will force the
      CallBy name Expression to be evaluated
      i.e  tail.forEach(fx) When we access the lazyTail
      Expression gets evaluated
       */
      fx.apply(head)
      tail.forEach(fx)
    }

    // lazyTail=tail.map(fx) this expression will be evaluated by need,
    // So map preserves the lazy evaluation
    /*
   lazyTail= tail.map(fx) this expression will be evaluated by need,
   So map preserves the lazy evaluation
    Lets understand with this example to understand
    lazyTail represents here Lazy evaluated EmptyStream
    reference which is at present not evaluated
    val s=new Node(1,lazyTail)
    so output of map operation will be
    mapped= s.map(_+1)= new Node(2,lazyTail)
    But actually lazytail=s.tail.map(_+1) is an expression now
    This is actually callByNAme expression for tail(  lazyTail=s.tail.map(_+1) )
    tail.map(_+1) that still not evaluated,Hence whole expression is not evaluated till now
    it will be not be evaluated until someone called
    mapped.lazyTail or mapped.foreach i.e
    if someone call for next iteration then it will be evaluated
    list reference=mapped.lazyTail
    but again that list tail is still not evaluated only head is evaluated
    because it is eager
     */
    override def map[B](fx: A => B): MyStream[B] = new Node[B](fx.apply(head), tail.map(fx))



/*
TODO
    override def ++ [B >: A](anotherStream: => MyStream[B]): MyStream[B] =
    When flatmap is called then it will return
    val stream= new Node[B](head, this.tail ++ anotherStream)
     here anotherStream == this.tail.flatMap(fx)
     so when someone try to acces the stream.tail then  this.tail ++ anotherStream gets evaluated
     new Node[B](2, anotherStream)
     when now some calls tail over it then this.tail.flatMap(fx) it gets evaluated
     new Node(3,lazytail) ++ anotherstream and so on......


 */


    /*
   TODO
        Imagine you have a MyStream[Int] with elements (1, 2, 3)
        and a function fx that maps an integer x to a new MyStream[Int] of (x, x + 1).

  Initial State:
  Original stream: (1, 2, 3)
  fx: x => MyStream(x, x + 1)

First Evaluation:
You request the head of the new MyStream.

fx.apply(1) produces (1, 2)
New MyStream: (1, 2) ++ flatMap of tail (which is still lazy and unevaluated)
like this val stream3 = (1, 2) ++ stream2 == (flatMap of tail (which is still lazy and unevaluated)
Second Evaluation:
You request the next element.

Now the flatMap of tail starts to be evaluated.
fx.apply(2) produces (2, 3)
New MyStream: (1, 2, 2, 3) ++ flatMap of next tail (which is still lazy and unevaluated)
Further Evaluations:
Would continue the process, unfolding elements as you go.



     */
    override def flatMap[B](fx: A => MyStream[B]): MyStream[B] = fx.apply(this.head) ++ this.tail.flatMap(fx)



    override def take(n: Int): MyStream[A] =
      if (n <= 0) EmptyStream
      else if (n == 1) new Node[A](this.head, EmptyStream)
        // here we have a Stream [0,1,2,3] we want to take n items out of this stream
      // new Node(head,[2,3]) after this is lazy evaluated
      // upto here is eager evaluation bcz this is first call
      //i.e when again take is call recursively tail.
      // so at first iteration we will get Node[A](0, tail.take(n - 1))
      // lazyTail=tail.take(n - 1) is lazyTail so it will become like this
      // stream=Node[A](0, lazyTail)
      // so when someone call stream.lazyTail then only tail will get evaluated
      // here this.tail.take(n - 1) we are trying to acccess tail here
      // but still this postion is lazy
      // hence until unless foreach is not called it will not get evualted because
      // foreach arguments are eagerly eveluated
      // take(n-1) as this expression is lazy so it will be evaluated
      // here tail.take(n-1) this is an expression
      //startFrom0.take(5).forEach(println)
        // startFrom0.take(5) =  new Node[A](0, tail.take(5- 1))
        // when u call  Node[A](0, tail.take(n - 1)).forEach then tail gets evaluated
      //Here when first time foreach is called the first tail is evaluated
      // and so on as we call foreach
      else new Node[A](this.head, this.tail.take(n - 1))

    // here first element is eagerly evaluated and remains lazy evaluated
    //tail.filter(predicate) here first is eagerly evaluated
    /*
    TODO
        Initial State:
      Original Stream: (1, 2, 3, 4, 5)
      Predicate: x => x % 2 == 0

First Evaluation:i.e stream.head
TODO
  predicate.apply(1) evaluates to false.
 The function then lazily moves to tail.filter(predicate) but does not evaluate it yet.
  Accessing Head of New Stream:

  Now the tail.filter(predicate) starts to be evaluated.
  predicate.apply(2) evaluates to true.
   The head of the new stream is 2.
     */
    override def filter(predicate: A => Boolean): MyStream[A] = if (predicate.apply(head))
      new Node[A](head, tail.filter(predicate))
    else tail.filter(predicate) //preservs lazy evaluation
  }

  object MyStream {
    //Here Tail will be evaluated on need basis not like in loop via recursion
    // once we ask for tail it is evaluated
    //i.e it will not piled up the function calls in stack like we do while looping using recursion
    //MyStream.from(accumulator)(fx) thi is the callByname expression its output is tail
    // so when someone uses node.tail then it willj  get evaluated
    // initilly it will returnn                                                                                                                                                                                                    ,
    // lazystream=  new Node[A](start, MyStream.from(accumulator)(fx)) #o                                ]
    //MyStream.from(accumulator)(fx) :-> this is callbyname expression
    // when you call lazystream.tail then it will get evaluated
    //Even though from looks recursive, the recursion is deferred by the lazy tail
    def from[A](start: A)(fx: A => A): MyStream[A] = {
      val accumulator = fx.apply(start)
      new Node[A](start, from(accumulator)(fx))
    }
  }

  val naturals: MyStream[Int] = MyStream.from(1)(_ + 1)
  println(naturals)
  println(naturals.head)
  // now tail will get evaluated here i.e the expression callbyname :->
  // MyStream.from(accumulator)(fx) will be evaluated
  println(naturals tail)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)
  val startFrom0Naturals: MyStream[Int] = 0 #:: naturals // naturals.#::(0) because :: is right associative
  println(startFrom0Naturals.head)
  // [0,1,2,3]
  //now inside take method when take try to access the startFrom0Naturals tail
  //new Node [A](start, MyStream.from(accumulator)(fx)) then from gets executed to evaluate the lazy tail
  // so take is evaluating the lazy tail and creating a stream it gets evluated till take(n-1)
  println("finished startFrom0Naturals stream : " + startFrom0Naturals)
  print(startFrom0Naturals.take(2))

  val startNaturalStream: MyStream[Int] = naturals.take(2)
  // TODO now the lazy tail get evaluated
  startNaturalStream.tail

  //Here When We call take on o/p of map then tail is get eveluated
  // so we are calling take for each tail
  // so what foreach will do fx.apply(head) here fx is println
  //      tail.forEach(println(head)) it will evaluate all tails of Stream and print the head
  // so its is printing the new head and accessing the next  tail
  startFrom0Naturals.take(7).forEach(println)


  /*
  TODO
       Lazy Evaluation Preservation
       Stream Generation: The initial Stream.from(1)(_ + 1) does not generate
       all numbers starting from 1 immediately. It only generates them as they are accessed.
    Mapping: The map(_ * 2) operation sets up a recipe
    for how each element should be transformed when it is accessed.
    It doesn't go ahead and multiply every element by 2 right away.
TODO
   Taking:
    take(10) just sets another layer of instruction on top
    of the existing stream to only consider the first 10 elements.
    Again, nothing is evaluated yet.


TODO
  Now, let's say you decide to print all the elements of mappedLazyStream.
  First Element: At this point, the stream evaluates the first element.
  It goes back to the original stream Stream.from(1)(_ + 1),
  takes 1, then applies map(_ * 2) to get 2.
TODO
 Second Element: When you ask for the next element,
 it will generate 2 from Stream.from(1)(_ + 1), then apply map(_ * 2) to get 4.
 TODO
    Third Element: When you ask for the next element,
   Up to Tenth Element: The process will continue like this until the tenth element, thanks to .take(10).

   Beyond Tenth Element: If you try to access the eleventh element,
   there won't be any, because .take(10) limits the stream to 10 elements.

Lazy evaluation is preserved at each step.
Computations are only performed when you actually access the elements,
 allowing you to work efficiently even with potentially infinite streams.


   */
  val mappedLazyStream: MyStream[Int] = startFrom0Naturals.map(_ * 2)
  //new Node( fx(head)*2 , tail.map(fx)) = mappedLazyStream
  val takenStream: MyStream[Int] =mappedLazyStream.take(10)
  // new Node(mappedLazyStreamHead, tail.take(n-1)) = takenLazystream
  val streamToList: Seq[Int] =takenStream.toList()
  // So when we call toList() or foreach then tail i.e (tail.take(n-1))) of takenLazystream
  // gets evaluated inside that
  // tail is also a lazy expression it will get evaluated first we got this from mappedLazy stream
  println(streamToList)

  /*
  TODO
      Stream.from(1)(_+1): This initializes an infinite stream starting
      from 1 and incrementing by 1 for each subsequent element.
      However, these elements are generated lazily, i.e.,
      they're not computed until they're actually needed.
TODO
     def flatMap[B](fx: A => MyStream[B]): MyStream[B] = fx.apply(this.head) ++ this.tail.flatMap(fx)
     flatMap(x => new Node[Int](x, new Node[Int](x+1,EmptyStream))):
      The flatMap function takes each element x
      from the original stream and
      creates a new Node containing two elements: x and x+1. new Node[Int](x, new Node[Int](x+1,EmptyStream))
TODO
   Lazy Evaluation Preservation
   When you apply the flatMap operation,
   you're not immediately creating these nodes
   for each element in the original stream.
   Instead, you're describing a plan of computation
   that will be executed only when necessary, thus preserving lazy evaluation.

 TODO
   Example:
         Let's say you want to access the
         first element of flatMappedStream. Here's what would happen:
TODO
 Stream Initialization:
 The original Stream.from(1)(_+1) produces the first element,
 1, only because you requested it.

TODO
  Flat Mapping:
   Now, flatMap kicks in.
   It takes this 1 and produces a new Node with elements 1 and 2 (x and x+1). new Node[Int](1, new Node[Int](2,EmptyStream))
   These nodes now become the first and second elements of your flatMappedStream.

When you access the first element, you get 1.
When you access the second, you get 2.
After that, only if you request the third element will the original stream produce 2,
and flatMap will then generate a new Node with elements 2 and 3.

The crucial point here is that each step happens only when you ask for it.
If you never access an element, no computation is performed for that element.
This allows your code to be extremely efficient and makes it possible to work
with infinite or computationally expensive streams.
val lazyytail=[flatMap(from(2))]

flatMappedStream =
  Node(1,
    Node(2,
      lazyytail))

      flatMappedStream
├─ head = 1                          ← from f(1)
├─ tail
│  ├─ head = 2                      ← from f(1)
│  ├─ tail
│  │  ├─ thunk: flatMap(from(2))   ← from(2) not evaluated yet
│  │  │  ├─ println("Generating: 2")
│  │  │  ├─ f(2) → Node(2, Node(3))
│  │  │  ├─ ++ thunk: flatMap(from(3))

   */
val flatMappedStream=startFrom0Naturals.flatMap(x => new Node[Int](x, new Node[Int](x+1,EmptyStream)))
// here in case of flat map we have the implementation like this
  // fx.apply(this.head) ++ this.tail.flatMap(fx) and ++ signature is
  // (anotherStream: => MyStream[B]) which is eager eveluation it is not callby name
  // hence due to this this.tail.flatMap(fx) Expression will be eagerly evaluated
  // which by means that it will keep calling tail.faltMap and tail is from method
  // hence we will keep calling from method so stack will blow up
  // to fix this we changed the signature of ++ to anotherStream: => MyStream[B]
  // now fx.apply(this.head) ++ this.tail.flatMap(fx)
  // this.tail.flatMap(fx) expression will be lazily evaluated

println(flatMappedStream)
  // now everything will be evaluated here in take
  val flatMappedTakenStream=flatMappedStream.take(10)
  val streamtoList: Seq[Int] = flatMappedTakenStream.toList()
  println(streamtoList)
  val filteredStream: MyStream[Int] =startFrom0Naturals.filter(_ <10)
  // filteredStream=new Node[A](head, tail.filter(predicate)) = filteredStream
  //TODO
  // Here Lazytail is  tail.filter(predicate)
  // now when we will call filteredStream.tail the expression gets executed
  //  tail.filter(predicate)  first tail is evaluated for the current stream
  // because that is also lazy and after that tail.filter(predicate)
  // this expression gets executed on demand
  // as you ask filteredStream.tail it will only gets executed
  //inside take it will be evaluated this i.e this: -> tail.filter(predicate)
  val filteredTakenStreamList=filteredStream.take(8).toList()
  // this will also blow up same reason as for the flatmap we discussed take is manadatory
  //startFrom0Naturals.forEach(println)
  println(filteredTakenStreamList)
  //Stream of fibonacci numbers Execise



}