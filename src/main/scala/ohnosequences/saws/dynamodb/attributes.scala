package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._

sealed trait AnyAttribute
class Attribute[V: oneOf[Values]#is] extends AnyAttribute
  // with FieldOf[V]
sealed trait AnyKeyAttribute extends AnyAttribute
class KeyAttribute[V: oneOf[Values]#is: oneOf[PrimaryKeyValues]#is]
  extends AnyKeyAttribute

trait AnyItem {
  type Table <: AnyTable
  // fields go here as a type set etc
}