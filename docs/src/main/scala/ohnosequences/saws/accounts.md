### Index

+ src
  + test
    + scala
      + ohnosequences
        + saws
          + dynamodb
            + [items.scala](../../../../test/scala/ohnosequences/saws/dynamodb/items.md)
            + [tables.scala](../../../../test/scala/ohnosequences/saws/dynamodb/tables.md)
            + [services.scala](../../../../test/scala/ohnosequences/saws/dynamodb/services.md)
  + main
    + scala
      + ohnosequences
        + [saws.scala](../saws.md)
        + experiments
        + saws
          + sqs
            + [queues.scala](sqs/queues.md)
            + [service.scala](sqs/service.md)
            + [messages.scala](sqs/messages.md)
          + [accounts.scala](accounts.md)
          + [regions.scala](regions.md)
          + [users.scala](users.md)
          + dynamodb
            + [package.scala](dynamodb/package.md)
            + [tables.scala](dynamodb/tables.md)
            + [attributes.scala](dynamodb/attributes.md)
            + [services.scala](dynamodb/services.md)
          + [typeops.scala](typeops.md)
          + [resources.scala](resources.md)
          + [services.scala](services.md)
          + ec2
            + [service.scala](ec2/service.md)

------


```scala
package ohnosequences.saws

import shapeless._
import shapeless.Nat._
import shapeless.Sized._

trait AnyAccount {
  
  // this is awkward. Plan to improve this with macros
  type ID = IndexedSeq[Char] Sized _12
  val id: ID
  // need to get more instances of Nats for this to work
  // type CanonicalID = Seq[Char] Sized _64
  // val canonical_id: CanonicalID
}

// sample
case object intercrossing extends AnyAccount {
  
  val id = Sized('8', '5', '4', '9', '2', '3', '7', '7', '4', '0', '4', '3')
}
```

