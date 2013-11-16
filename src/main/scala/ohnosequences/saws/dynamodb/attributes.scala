package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import shapeless._

// note that all this is OK independently of how we actually inmplement records
object AnyAttribute {
  type of[V] = AnyAttribute { type Value = V }
}
// this is sealed so that we can enforce the Value bound
sealed trait AnyAttribute {
  type Value
}
  class Attribute[V: oneOf[Values]#is] extends AnyAttribute with FieldOf[V] {
    type Value = V
  }