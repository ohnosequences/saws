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

// I want to investigate here with the possibility of encoding Items as Records tagged with the corresponding Table (singleton) type

object AnyItem {

  type ItemType[S <: HList, A <: HList] = A with SchemaTag[S, A]
  trait SchemaTag[S <: HList, A <: HList]
  // now tag items with the schema coming from table.schema
}