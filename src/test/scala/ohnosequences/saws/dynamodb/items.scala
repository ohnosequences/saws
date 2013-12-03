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

  val attr =  (id ->> 3223)

  val item =  (id ->> 3223)             ::
              (name ->> "Antonio Roca") ::
              (age ->> 52)              :: HNil

  test("Convert items and attributes to HLists of tuples") {
    
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

  test("Convert items and attributes to List[(String, String)]") {

    val attr_str = attribute.toStr(attr)
    assert (attr_str === "id -> 3223")

    val attr_strPair = attribute.toStringPair(attr)
    assert (attr_strPair === ("id","3223"))

    val item_str = item map attribute.toStr
    assert (
      item_str 
      ===
      "id -> 3223" :: "name -> Antonio Roca" :: "age -> 52" :: HNil
    )

    val item_strPair = item map attribute.toStringPair
    assert(
      item_strPair
      ===
      ("id","3223") :: ("name","Antonio Roca") :: ("age","52") :: HNil
    )

    val item_ListStrPair: List[(String, String)] = item_strPair.toList
    println(item_ListStrPair)
    // val attr_kv = attribute.toTuple(attr)

  }
}
