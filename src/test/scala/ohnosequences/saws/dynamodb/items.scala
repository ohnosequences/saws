package ohnosequences.saws.dynamodb

import shapeless._
import shapeless.record._

object testAttributes {

  case object name extends Attribute[String]
  case object id   extends Attribute[Int]
  case object age  extends Attribute[Int]
  case object buh  extends Attribute[Set[String]]
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

import testAttributes._

class ItemOps extends org.scalatest.FunSuite {

  test("Convert items to HLists of tuples") {

    val item =  (id ->> 3223)             ::
                (name ->> "Antonio Roca") ::
                (age ->> 52)              :: HNil

    val attr = (id ->> 3223)
    val attr_kv = attribute.toTuple(attr) 
    assert(attr_kv === (id -> 3223))

    val item_kv = item map attribute.toTuple
    assert(
      item_kv === (
        (id -> 3223)             ::
        (name -> "Antonio Roca") ::
        (age -> 52)              :: HNil
      )
    )
  }
}
