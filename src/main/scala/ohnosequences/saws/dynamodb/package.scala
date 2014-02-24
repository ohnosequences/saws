package ohnosequences.saws

import typeOps._
import shapeless._

package object dynamodb {

  type StateOf[R <: AnyDynamoDBResource] = { type is = AnyDynamoDBState { type Resource = R } }
  
  // dynamodb type restrictions
  type Bytes = Seq[Byte]
  type Num   = Int
  type Values = either[Num]#or[String]#or[Bytes]#or[Set[Num]]#or[Set[String]]#or[Set[Bytes]]
  // not documented; the API informs you about it if you try not to adhere to it
  type PrimaryKeyValues = either[String]#or[Num]

  import shapeless.record._
  type TableThroughput =  FieldType[ReadCapacity.type, ReadCapacity.Value]   :: 
                          FieldType[WriteCapacity.type, WriteCapacity.Value] :: HNil
}