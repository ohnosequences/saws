package ohnosequences.saws.dynamodb

import ohnosequences.saws._

object testTables {

  import testAttributes._
  import testServices._

  object users extends Table(Hash(id), intercrossingDynamoEU) {}
  
}