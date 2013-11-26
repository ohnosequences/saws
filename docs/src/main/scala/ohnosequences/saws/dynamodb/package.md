### Index

+ src
  + test
    + scala
      + ohnosequences
        + saws
          + dynamodb
            + [items.scala](../../../../../test/scala/ohnosequences/saws/dynamodb/items.md)
            + [tables.scala](../../../../../test/scala/ohnosequences/saws/dynamodb/tables.md)
            + [services.scala](../../../../../test/scala/ohnosequences/saws/dynamodb/services.md)
  + main
    + scala
      + ohnosequences
        + [saws.scala](../../saws.md)
        + experiments
          + [abstractObjects.scala](../../experiments/abstractObjects.md)
        + saws
          + sqs
            + [queues.scala](../sqs/queues.md)
            + [service.scala](../sqs/service.md)
            + [messages.scala](../sqs/messages.md)
          + [accounts.scala](../accounts.md)
          + [regions.scala](../regions.md)
          + [users.scala](../users.md)
          + dynamodb
            + [package.scala](package.md)
            + [tables.scala](tables.md)
            + [attributes.scala](attributes.md)
            + [services.scala](services.md)
          + [typeops.scala](../typeops.md)
          + [resources.scala](../resources.md)
          + [services.scala](../services.md)
          + ec2
            + [service.scala](../ec2/service.md)

------


```scala
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
  
  trait AnyDynamoDBStateOf 
    extends AnyStateOf { type Resource <: AnyDynamoDBResource }

  // dynamodb type restrictions
  type Bytes = Seq[Byte]
  type Num   = Int
  type Values = either[Num]#or[String]#or[Bytes]#or[Set[Num]]#or[Set[String]]#or[Set[Bytes]]
  // not documented; the API informs you about it if you try not to adhere to it
  type PrimaryKeyValues = either[String]#or[Num]

  type TableThroughput = ReadCapacity.type :: WriteCapacity.type :: HNil
}
```

