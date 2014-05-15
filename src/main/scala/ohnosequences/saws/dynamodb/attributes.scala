package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import shapeless._

/*
  ## attributes

  Attributes are just key-values in the DynamoDB terminology. Note that all these types are independent from any particular implementation of Items.
*/

/*
  this is sealed so that we can enforce the `Value` bound restricting it to be one of those valid for the the DynamoDB service
*/
sealed trait AnyAttribute { type Value }

object AnyAttribute {

  type of[V] = AnyAttribute { type Value = V }
}

  class Attribute[V: oneOf[Values]#is] extends AnyAttribute with FieldOf[V] { type Value = V }



object attribute {

  import shapeless.record._
  import shapeless.ops.record._

  // convert this to Show at some point

  object toTuple extends Poly1 {

    // get a witness for the key, split
    implicit def atFieldType[F <: AnyAttribute, V]
      (implicit key: Witness.Aux[F]) = at[FieldType[F, V]](
         
      v => (key.value, v:V)
    )
  }

  object toStringPair extends Poly1 {

    implicit def atFieldType[F <: AnyAttribute, V]
      (implicit key: Witness.Aux[F]) = at[FieldType[F, V]](
         
      v => (key.value.toString, v.toString)
    )
  }
  object toStr extends Poly1 {

    implicit def atFieldType[F <: AnyAttribute, V]
      (implicit key: Witness.Aux[F]) = at[FieldType[F,V]](v => key.value.toString +" -> "+ v.toString)
  }

  // object toKeyValues extends Poly1 {

  //   // need implicits here
  //   implicit def atRecord[R <: HList] = at[R](
  //     record => record map toTuple
  //   )
  // }

}



/*  
  I want to investigate here with the possibility of encoding Items as Records tagged with the corresponding Table (singleton) type.
  This way I could get rid of the nested `Item` case class.

  Also, it would be nice to see if tagging HLists (or any record container) with the corresponding schema of singl types scales.

  This should be done in the standard way, as follows:

  1. get the first field from the schema
  2. find it in the value list
  3. require something like that and tag recursively
*/
object AnyItem {

  type ItemType[S <: HList, A <: HList] = A with SchemaTag[S, A]
  trait SchemaTag[S <: HList, A <: HList]
  // now tag items with the schema coming from table.schema

  // this needs an implicit param for S being a list of fields
  def RecordBuilder[S <: HList] = new RecordBuilder[S]

  import Tagging._
  // S = schema, R = record = values
  class RecordBuilder[S <: HList] {
    // implicit here for proving that v conforms to this type
    def apply[R <: HList: validFor[S]#is](v: R): ItemType[S, R] = v.asInstanceOf[ItemType[S, R]]
  }

  object Tagging {

    type validFor[R <: HList] = { type is[S <: HList] = (R ValidFor S) }

    trait ValidFor[R <: HList, S <: HList]
  }

}