package ohnosequences.saws.dynamodb

import shapeless._
import shapeless.record._

object testAttributes {

  case object name extends Attribute[String]
  case object id   extends Attribute[Int]
  case object age  extends Attribute[Int]
  case object raw  extends Attribute[Bytes]
  // invalid value
  // case object ohnoes extends Attribute[List[String]]

  type Item = FieldType[id.type, id.Value]     ::
              FieldType[name.type, name.Value] ::
              FieldType[age.type, age.Value]   :: HNil

  val item: Item =  (id ->> 23423)             ::
                    (name ->> "Paco Romero")   ::
                    (age ->> 34)               :: HNil
}