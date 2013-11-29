package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import shapeless._

/*
  ## attributes

  Attributes are just key-values in the DynamoDB terminology. Note that all these types are independent from any particular implementation of Items.
*/
object AnyAttribute {
  type of[V] = AnyAttribute { type Value = V }
}
/*
  this is sealed so that we can enforce the `Value` bound restricting it to be one of those valid for the the DynamoDB service
*/
sealed trait AnyAttribute { type Value }

  class Attribute[V: oneOf[Values]#is] extends AnyAttribute with FieldOf[V] { type Value = V }



object attribute {

  import shapeless.record._
  import shapeless.ops.record._
  import shapeless.ops.record.Keys._

  object toTuple extends Poly1 {

    // get a witness for the key, split
    implicit def atFieldType[F <: AnyAttribute, V]
      (implicit key: Witness.Aux[F]) = 
        at[FieldType[F, V]](
          v => (key.value, v:V)
        )
  }

}



/*
  I want to investigate here with the possibility of encoding Items as Records tagged with the corresponding Table (singleton) type.
  This way I could get rid of the nested `Item` case class.
*/
object AnyItem {

  type ItemType[S <: HList, A <: HList] = A with SchemaTag[S, A]
  trait SchemaTag[S <: HList, A <: HList]
  // now tag items with the schema coming from table.schema
}