
# default get myself

See [SO - How to use Scala's this typing, abstract types, etc. to implement a Self type?](http://stackoverflow.com/a/4313266/614394).

``` scala
// from values to types?
trait Me[T <: Me[T]] { self: T =>

  // type Self >: self.type <: T
  val name: String

  def readFrom(n: String): Option[this.type] = if (n == this.name) Some(this) else None 
}

sealed trait Fruit[F <: Fruit[F] with Me[F]] extends Me[F] { self: F => }

case class Apple(override val name: String) extends Fruit[Apple]
object Apple extends Apple("apple")
// no no no
// case class Banana(override val name: String) extends Fruit[Apple]

object test {

  val buh = Apple
  val someApple: Option[Apple.type] = Apple.readFrom("apple")
  val notApple: Option[Apple.type] = Apple.readfrom("banana")
}
```