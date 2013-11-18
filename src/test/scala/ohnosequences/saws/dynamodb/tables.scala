package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import shapeless._

object testTables {

  import testAttributes._
  import testServices._

  object users extends Table(
    primaryKey  = Hash(id), 
    keys        = id :: name  :: age :: HNil,
    service     = intercrossingDynamoEU
  ) {}

  val user = users.Item(
      (id ->> 23423)             ::
      (name ->> "Paco Romero")   ::
      (age ->> 34)               :: HNil
    )
  
  // this should not compile
  // at least for a strict table or something like that
  val user2 = users.Item(
      (id ->> 23423)             ::
      (age ->> 34)               :: HNil
    )

  val record =  (id ->> 23423)             ::
                (name ->> "Paco Romero")   ::
                (age ->> 34)               :: HNil

  val itemFromRecord = users.Item(record)

  // this should not compile in any case
  // val user3 = users.Item(
  //     (id ->> 23423)              ::
  //     (buh ->> Set("oh", "argh")) ::
  //     (age ->> 34)                :: HNil
  //   )
}