package ohnosequences.saws

import typeOps._

package object dynamodb {

  import shapeless._

  trait AnyDynamoDBResource extends 
    AnyResource { type Service <: AnyDynamoDBService }
  trait AnyDynamoDBStateOf 
    extends AnyStateOf { type Resource <: AnyDynamoDBResource }

  // dynamodb type restrictions
  type Bytes = Seq[Byte]
  type Num   = Int
  type Values = either[Num]#or[String]#or[Bytes]#or[Set[Num]]#or[Set[String]]#or[Set[Bytes]]
  // not documented
  type PrimaryKeyValues = either[String]#or[Num]

  type TableThroughput = ReadCapacity.type :: WriteCapacity.type :: HNil
}