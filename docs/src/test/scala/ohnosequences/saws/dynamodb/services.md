### Index

+ src
  + test
    + scala
      + ohnosequences
        + saws
          + dynamodb
            + [items.scala](items.md)
            + [tables.scala](tables.md)
            + [services.scala](services.md)
  + main
    + scala
      + ohnosequences
        + [saws.scala](../../../../../main/scala/ohnosequences/saws.md)
        + saws
          + sqs
            + [queues.scala](../../../../../main/scala/ohnosequences/saws/sqs/queues.md)
            + [service.scala](../../../../../main/scala/ohnosequences/saws/sqs/service.md)
            + [messages.scala](../../../../../main/scala/ohnosequences/saws/sqs/messages.md)
          + [accounts.scala](../../../../../main/scala/ohnosequences/saws/accounts.md)
          + [regions.scala](../../../../../main/scala/ohnosequences/saws/regions.md)
          + [users.scala](../../../../../main/scala/ohnosequences/saws/users.md)
          + dynamodb
            + [package.scala](../../../../../main/scala/ohnosequences/saws/dynamodb/package.md)
            + [tables.scala](../../../../../main/scala/ohnosequences/saws/dynamodb/tables.md)
            + [attributes.scala](../../../../../main/scala/ohnosequences/saws/dynamodb/attributes.md)
            + [services.scala](../../../../../main/scala/ohnosequences/saws/dynamodb/services.md)
          + [typeops.scala](../../../../../main/scala/ohnosequences/saws/typeops.md)
          + [resources.scala](../../../../../main/scala/ohnosequences/saws/resources.md)
          + [services.scala](../../../../../main/scala/ohnosequences/saws/services.md)
          + ec2
            + [service.scala](../../../../../main/scala/ohnosequences/saws/ec2/service.md)

------


```scala
package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.regions._

object testServices {

  case object intercrossingDynamoEU extends DynamoDBService(EU, intercrossing)
}
```

