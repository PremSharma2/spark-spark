package oops

class Novel(name :String,  yearOfRelease:Int , author:Writer) {
  def author_age: Int = yearOfRelease - author.year
  def isWrittenBy(author: Writer): Boolean = author== this.author
  def copy(newYear:Int):Novel=new Novel(name,newYear,author)
  
}