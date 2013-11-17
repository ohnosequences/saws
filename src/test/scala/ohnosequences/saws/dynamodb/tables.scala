package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import shapeless._

object testTables {

  import testAttributes._
  import testServices._

  object users extends Table(
    pKey = Hash(id), 
    keys = name :: id  :: age :: HNil,
    service = intercrossingDynamoEU
  ) {}
  
}