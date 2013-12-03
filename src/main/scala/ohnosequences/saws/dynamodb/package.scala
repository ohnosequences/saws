package ohnosequences.saws

import typeOps._

package object dynamodb {

  import shapeless._

  trait AnyDynamoDBARN extends AnyARN {

    type Resource <: AnyDynamoDBResource
    val resource: Resource
    def asString = "arn:aws"                +":"+ 
      resource.service.namespace            +":"+ 
      resource.service.region.toString      +":"+ 
      resource.service.account.id.toString  +":"+
      resource.asString
  }
  case class DynamoDBARN[R <: AnyDynamoDBResource](resource: R) extends AnyDynamoDBARN {
    type Resource = R
  }

  trait AnyDynamoDBResource extends AnyResource { 

    type Service <: AnyDynamoDBService
    type ARN <: AnyDynamoDBARN
    def asString: String
  }
  
  // just a bound
  trait AnyDynamoDBState extends AnyState {
 
   type Resource <: AnyDynamoDBResource 
 }
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