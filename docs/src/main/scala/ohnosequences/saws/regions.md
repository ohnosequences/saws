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

// regions
sealed trait AnyRegion {
  val name: String
}

object regions {

  type EU = EU.type
  type US = US.type
  type US_WEST_1 = US_WEST_1.type
  type US_WEST_2 = US_WEST_2.type
  type US_WEST_3 = US_WEST_3.type
  
  case object EU        extends AnyRegion { val name = "eu-west-1" }
  case object US        extends AnyRegion { val name = "us-east-1" }
  case object US_WEST_1 extends AnyRegion { val name = "us-west-1" }
  case object US_WEST_2 extends AnyRegion { val name = "us-west-2" }
  case object US_WEST_3 extends AnyRegion { val name = "us-west-3" }
}

  
```

