package ohnosequences.saws.dynamodb

import ohnosequences.saws.typeOps._
import ohnosequences.saws.{AnyARN, AnyResource, AnyState}
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