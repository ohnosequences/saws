package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import shapeless._

object AnyAttribute {
  type of[V] = AnyAttribute { type Value = V }
}
sealed trait AnyAttribute {
  type Value
}  
  class Attribute[V: oneOf[Values]#is] extends AnyAttribute with FieldOf[V] {
    type Value = V
  }

trait AnyItem {
  type Table <: AnyTable
  // fields go here as a type set etc
  type Keys <: HList
}