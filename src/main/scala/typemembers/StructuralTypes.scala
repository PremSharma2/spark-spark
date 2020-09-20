package typemembers

object StructuralTypes  extends App {
  type JavaClosable = java.io.Closeable

  // let say our team has implemented some custom impl of closeable

  class HipsterCloseable{
    def close() = println("close")
  }

  // now implement a method that will accept both Java Closeable and the Custom Scala Closeable
 // def closeQuitely(closeable: JavaClosable or HipsterCloseable)
  // this can possible in scala only by structural types
  // it is nothing but type alias with return type of this code block i.e the type structure
  // i.e anything which has this structure which has close method that type will be aliased
  // with UnifiedCloseable
  type UnifiedCloseable = {
   def close() : Unit
  }// this is called stryuctural type
// now we can redefine method
  def closeQuitely(closeable: UnifiedCloseable) = closeable.close()

  closeQuitely(new JavaClosable {
    override def close(): Unit = ???
  })
  closeQuitely(new HipsterCloseable)

  //type refinements
// So AdvancedCloseable is JavaCloseable Plus the Structural type in form of codeblock
  // so this type of aliasing represents two types
  type AdvancedCloseable = JavaClosable {
    def closeSilently(): Unit
  }
  class AdvancedJavaCloesable extends JavaClosable{
    override def close(): Unit = println("Java closable")
    def closeSilently():Unit = println("......Java closes silently...")
  }
  def closeResources(advCloseable: AdvancedCloseable):Unit = advCloseable.closeSilently()
  // Here compiler will read like this that AdvancedJavaCloesable is originates from JavaCloseable
  // and also has structural type hence that what the the compiler wanted
  // it is type aliasing for combo types i.e two types
  closeResources(new AdvancedJavaCloesable)
}
