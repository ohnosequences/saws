### Index

+ src
  + test
    + scala
      + ohnosequences
        + saws
          + dynamodb
            + [items.scala](../../../test/scala/ohnosequences/saws/dynamodb/items.md)
            + [tables.scala](../../../test/scala/ohnosequences/saws/dynamodb/tables.md)
            + [services.scala](../../../test/scala/ohnosequences/saws/dynamodb/services.md)
  + main
    + scala
      + ohnosequences
        + [saws.scala](saws.md)
        + saws
          + sqs
            + [queues.scala](saws/sqs/queues.md)
            + [service.scala](saws/sqs/service.md)
            + [messages.scala](saws/sqs/messages.md)
          + [accounts.scala](saws/accounts.md)
          + [regions.scala](saws/regions.md)
          + [users.scala](saws/users.md)
          + dynamodb
            + [package.scala](saws/dynamodb/package.md)
            + [tables.scala](saws/dynamodb/tables.md)
            + [attributes.scala](saws/dynamodb/attributes.md)
            + [services.scala](saws/dynamodb/services.md)
          + [typeops.scala](saws/typeops.md)
          + [resources.scala](saws/resources.md)
          + [services.scala](saws/services.md)
          + ec2
            + [service.scala](saws/ec2/service.md)

------


```scala
package ohnosequences

package object saws {
  
  import saws.regions._
  import saws.typeOps._
}
```

