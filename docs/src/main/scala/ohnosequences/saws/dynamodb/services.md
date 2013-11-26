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
package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._ 

trait AnyDynamoDBService extends AnyService {

  type validRegions = either[EU]#or[US]
  val namespace = "dynamodb"
}

abstract class DynamoDBService[
    R <: AnyRegion: oneOf[AnyDynamoDBService#validRegions]#is,
    A <: AnyAccount
  ](val region: R, val account: A) extends AnyDynamoDBService {

    type Region = R
    type Account = A
    def endpoint = "https://" + namespace +"."+ region.name + host
  } 
```

