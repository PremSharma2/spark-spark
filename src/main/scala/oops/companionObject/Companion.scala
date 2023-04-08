package oops.companionObject

object Companion {



  class Director(val firstName: String, val lastName: String, val yearOfBirth: Int) {
    def name: String = firstName concat " " concat lastName

    override def equals(obj: Any): Boolean = {
      if (!obj.isInstanceOf[Director]) return false
      val other: Director = obj.asInstanceOf[Director]

      this.firstName == other.firstName && this.lastName == other.lastName && this.yearOfBirth == other.yearOfBirth
    }
  }

  class Film(val name: String, val yearOfRelease: Int, val imdbRating: Double, val director: Director) {
    def directorsAge: Int = yearOfRelease - director.yearOfBirth

    def isDirectedBy(d: Director): Boolean = d.firstName == director.firstName && d.lastName == director.lastName

    def copy(newName: String, newYearOfRelease: Int, newImdbRating: Double, newDirector: Director): Film = new Film(newName, newYearOfRelease, newImdbRating, newDirector)

    override def equals(obj: Any): Boolean = {
      if (!obj.isInstanceOf[Film]) return false
      val other: Film = obj.asInstanceOf[Film]
      name == other.name && yearOfRelease == other.yearOfRelease && imdbRating == other.imdbRating
    }
  }

  object Director {
    def apply(fName: String, lName: String, yBirth: Int): Director = new Director(fName, lName, yBirth)
    def older(d1: Director, d2: Director): Director = if (d1.yearOfBirth < d2.yearOfBirth) d1 else d2
  }

  object Film {
    def apply(name: String, yearOfRelease: Int, imdbRating: Double, director: Director): Film = new Film(name, yearOfRelease, imdbRating, director)
    def highestRating(f1: Film, f2: Film): Film = if (f1.imdbRating > f2.imdbRating) f1 else f2
    def oldestDirectorAtTheTime(f1: Film, f2: Film): Director = Director.older(f1.director, f2.director)
  }
}
