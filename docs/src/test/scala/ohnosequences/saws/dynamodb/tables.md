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
        + experiments
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
import shapeless._

object testTables {

  import testAttributes._
  import testServices._

  object users extends Table(
    primaryKey  = Hash(id), 
    keys        = id :: name  :: age :: HNil,
    service     = intercrossingDynamoEU
  ) {}

  val user = users.Item(
      (id ->> 23423)             ::
      (name ->> "Paco Romero")   ::
      (age ->> 34)               :: HNil
    )
  
  // this should not compile
  // at least for a strict table or something like that
  val user2 = users.Item(
      (id ->> 23423)             ::
      (age ->> 34)               :: HNil
    )

  val record =  (id ->> 23423)             ::
                (name ->> "Paco Romero")   ::
                (age ->> 34)               :: HNil

  val itemFromRecord = users.Item(record)

  // this should not compile in any case
  // val user3 = users.Item(
  //     (id ->> 23423)              ::
  //     (buh ->> Set("oh", "argh")) ::
  //     (age ->> 34)                :: HNil
  //   )
}
```

