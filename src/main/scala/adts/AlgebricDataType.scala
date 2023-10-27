package adts

object AlgebricDataType  extends App{

  /*

  I debated for a long time about how to introduce Algebraic Data Types (ADTs) in this book.
  I finally decided that ADTs aren’t a way of proactively designing FP code;
  instead,
  they’re a way of categorizing the FP code you’ve already written, specifically
   your FP data models.
   That is, you don’t sit down and say,
   “I want to write my Customer class as a ‘sum of the products’ ADT.”
    Instead, if you do anything, you might look at some code and say,
    “Hey, Mary, did you notice that the Customer class is a ‘sum of the products’ ADT?”

Because I look at ADTs as a way of categorizing or classifying code rather than designing code,
I decided it was best to include this topic as an appendix in this book, rather than as a lesson in the front of the book.

The “TL;DR” version of this lesson goes like this:
If you create your data models using (a)
case classes with immutable fields and (b) case objects, and (c)
those data types have no methods, you’re already writing ADTs.

Surprise: Almost every case class and case object in this book is some form of ADT.
I just didn’t think it was important to categorize them when I wrote them.

Goals, motivation
The goals of this lesson are:

To define basic ADT terminology
To show examples of the different ADT types
To help demonstrate another way to see your Scala/FP code as algebra
In regards to that last point,
a main benefit of being aware of ADTs is that you begin to see
 your code as being even more like algebra.
You specifically begin to see your case classes and case objects as sets of objects,
 and the functions that operate on them as operators on those data sets.
 To understand what that means, it will help to formally define algebra.



What is “Algebra”?
To understand ADTs, you first have to understand what is meant by the word “algebra.”
 Informally, an algebra can be thought of as consisting of two things:

A set of objects
The operations that can be applied to those objects to create new objects
Technically an algebra also consists of a third item, the laws that govern the algebra,
 but I’m not going to cover laws in this lesson.

If you’re like me, you never thought of algebra as anything other than high school algebra,
 but it turns out that any concept that can be thought of as (a) a set of objects,
 and (b) operators on those objects is a form of algebra. Here are a few examples.

Numeric algebra
“High school algebra” is the algebra we learned back in high school (or possibly earlier).
 It’s more formally known as “numeric algebra,” the algebra of numbers. You can think of it like this:

A set of objects, such as whole numbers
The operations that can be used on those objects: +, -, * (and /)
One thing I never thought about is that the operators are used on existing
 numbers (objects) to create
new numbers (objects):

1 + 1 = 2
3 * 3 = 9
Notice that even in basic math, the numbers 2 and 9 are “created”
 from the numbers 1 and 3 by using the + and * operators.

Relational algebra
Another type of algebra is known as “relational algebra.”
 This is the algebra of relational databases,
  and in this algebra the database tables are the “set of objects,”
  and query tools like SELECT, UPDATE, and JOIN are the “operators”
   that let you create new objects from the existing objects.

Algebra in programming
Throughout this book you’ve been using algebra, possibly without knowing it.
 (I certainly didn’t know it when I started working with FP.)
 For example, take a look at this case class:

case class Pair (
    a: Int,
    b: Int
)
This code creates a new type Pair from two instances of the existing type Int.
The class constructor itself is an “operator” that
 lets you create new types from existing Scala types,
  just like + and * let you create new numbers from existing numbers.

Here’s another example of how you can use
 Scala operators to create new data types from existing ones:

sealed trait Direction
case object North extends Direction
case object South extends Direction
case object East extends Direction
case object West extends Direction
To learn about the “algebra” of these two examples, read on ...

Three types of Algebraic Data Types
ADTs fall into three main categories:

Sum type
Product type
Hybrid types
You actually just saw the first two types,
and I’ll explain all three of them in the following sections.

The Sum type
The Direction example I just showed is called a “Sum type,” or Sum ADT.
 The Sum type is also referred to as an “enumerated type”
 because you simply enumerate all of the possible instances of the type.
  A few important points about this are:

Sum types are typically created with a sealed trait as the base type,
 with instances created as case objects.
You use a sealed trait because you don’t want them to be extended.
The number of enumerated types you list are the only possible instances of the base type.
 In this example, Direction has four possible values: North, South, East, or West.
We use the phrases “is a” and “or” when talking about Sum types.
 For example, North is a type of Direction,
and Direction is a North or a South or an East or a West.
People use different names for the concrete instances in a Sum type,
 including value constructors, alternates, and cases.

Another example
As another example, imagine that you need to write your own “boolean” type for Scala.
 You can write them as a Sum type like this:

sealed trait Bool
case object True extends Bool
case object False extends Bool
Just like the Direction example,
 the base type is defined as a sealed trait and the
 two possible values are defined as case object.
 Also notice that this approach uses the sealed trait and
  case object syntax as operators to create new data types.

Why use sealed trait?
A great feature of using sealed trait is that
 it lets the compiler perform “exhaustiveness checking.”
What happens is that a sealed trait can only be extended in the file
in which it was defined;
because it can’t be extended anywhere else,
the compiler knows all of the subtypes of the trait
that can possibly exist.
Because of this,
the compiler can exhaustively check the possible cases in match expressions,
 and it will emit a warning if the match expression isn’t exhaustive.
 This makes your programming life easier, and your code safer.

Why use case object?
The reason Sum types use the case object declaration
is that they only require singleton instances,
 and the Scala object provides that functionality.
 For instance, with the Bool example it makes sense to have only one
 True instance in all of your code.
 There’s no need to create new True and False instances
 every time you work with boolean values.
 Scala’s object gives you this singleton functionality.

As Scala/FP developers, we further use case object — as opposed to object —
 because it provides important additional functionality,
 with the most important feature being support for pattern matching;
 its automatically-generated unapply method
 lets case objects work easily in match expressions.
 (case object also provides default equals and hashCode methods, extends Serializable,
  has a good default toString method, etc.)

The Product type
The second type of ADT is known as a “Product type.”
 It’s name comes from the fact that you use the Scala
 case class constructor to create a data type
 whose number of possible concrete instances
 can be determined by multiplying
 the number of possibilities of all of its constructor fields.

Take this class for example:

case class DoubleBoo (
    b1: Bool,
    b2: Bool
)
How many possible instances of this class can you have? Well,
each field can either be True or False, so the possibilities are:

DoubleBoo(True, True)
DoubleBoo(True, False)
DoubleBoo(False, True)
DoubleBoo(False, False)
Therefore, the correct answer is that there are four possible instances.
 You can also derive this answer mathematically:

b1 has two possibilities
b2 has two possibilities
The total number of possible instances
is determined by multiplying the number of possibilities of
 each constructor field, and 2 multiplied by 2 is 4
Because the number of possible instances of Product ADTs can be calculated by multiplying the number of possible values of every constructor parameter, what do you think the number of possibilities of this Pair type are:

case class Pair (
    a: Int,
    b: Int
)
If you answered, “A lot,” that’s close enough. An Int has 2^32 possible values, so if you multiply the number of possible Int values by itself, you get a very large number.

Next, what do you think the number of possibilities are for this class:

case class Person (
    firstName: String,
    lastName: String,
    mother: Person,
    father: Person
)
If you answered, “Infinite,” that’s a good answer. Because a String has an infinite number of possibilities,
 Person can have an infinite number of concrete instances.

While I don’t concern myself with ADTs too much, this particular point had a significant impact on me. When I first saw this, I realized that any time a function accepted a String, that String had an infinite number of possibilities. That’s a lot to account for. Similarly, a boolean value has two possibilities, a Byte has 256 possible values, and the Direction Sum type has four possibilities. The lesson for me is that the fewer possibilities you have to deal with, the simpler your code will be. (At the very least, this was the last time I ever used a series of string constants instead of enumerations.)



Before we move on, here are a few important points about the Product type:

Writing case class and defining the constructor parameters is essentially the “product” operator.
The number of possible values of a Product type is the product of all possible combinations of the constructor parameters (i.e., a Cartesian product).
We use the phrases “has a” and “and” when talking about Product types. Pair has a a and a b; Person has a firstName, lastName, mother, and father.
As shown in the Person example, Product types can be recursive; mother and father are declared as Person types inside the Person definition.
Hybrid types
The Sum and Product types are the two base ADTs;
all other ADTs are hybrids created from those base types.
 As the book Essential Scala states, “An algebraic data type is any data that uses the above two patterns” (Sum and Product).

One formally-defined hybrid type is known as the “Sum of Products” type.
 With a few minor changes to reflect modern Scala practices, Mario Gleichmann created a good example of this in 2011:
TODO
 sealed trait Shape
 final case class Circle(radius: Double) extends Shape
 final case class Rectangle(width: Double, height: Double) extends Shape
 These types represent a Sum type because Shape is a Circle or a Rectangle;
 Circle is a Product type because it has a radius;
 and Rectangle is also Product type because it has a width and a height.
TODO
 There are other variations of these possibilities,
 which is why I refer to all other combinations as “hybrid” types.
 For instance, the Pizza class in the domain modeling lessons is a Product type that contains three Sum types:

TODO
 case class Pizza (
    crustSize: CrustSize,
    crustType: CrustType,
    toppings: Seq[Topping]
 )
Sum and Product types can be combined in any ways that are needed to solve the problem at hand.
 Hopefully this demonstrates the point I made at the beginning of this lesson:
 ADTs are just a way of formally categorizing the data types in your data model.

TODO
 Pattern matching
 A great benefit of ADTs is that they simplify and encourage the use of pattern matching in your code.
 For instance, given these Shape types:
TODO
 sealed trait Shape
 final case class Circle(radius: Double) extends Shape
 final case class Rectangle(width: Double, height: Double) extends Shape
 you can easily write an isRound function using pattern matching:

TODO
 def isRound(s: Shape): Boolean = s match {
    case Circle(_) => true
    case _ => false
 }

TODO
 Similarly, using the Bool type I created earlier:
TODO
 sealed trait Bool
 case object True extends Bool
 case object False extends Bool
 you can define and and or functions with pattern matching:

TODO
 def and(a: Bool, b: Bool): Bool = (a,b) match {
    case (True, True)   => True
    case (False, False) => True
    case (True, False)  => False
    case (False, True)  => False
 }

def or(a: Bool, b: Bool): Bool = (a,b) match {
    case (True, _) => True
    case (_, True) => True
    case (_, _)    => False
}
This is what those last two functions look like in the Scala REPL:

scala> or(True,False)
res0: Bool = True

scala> and(True,False)
res1: Bool = False
As demonstrated in these examples, using pattern matching with ADTs is a common programming pattern ... a Scala/FP idiom.

Key points
The key points of this lesson are:

If you create your data models using (a) case classes with immutable fields and (b) case objects, and (c) those data types have no methods, you’re already writing ADTs (whether you knew it or not).
I view ADTs as a way of categorizing or observing code, not designing code.
An “algebra” is a set of objects, the operators that can be used on those objects, and laws governing their behavior.
The two main types of ADTs are the Sum type and Product type. Other hybrid ADTs are derived from these base types.
ADTs encourage a pattern-matching style of programming.

   */

}
