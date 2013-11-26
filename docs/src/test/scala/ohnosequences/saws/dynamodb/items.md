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
          + [abstractObjects.scala](../../../../../main/scala/ohnosequences/experiments/abstractObjects.md)
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

import shapeless._
import shapeless.record._

object testAttributes {

  case object name extends Attribute[String]
  case object id   extends Attribute[Int]
  case object age  extends Attribute[Int]
  case object buh  extends Attribute[Set[String]]
  case object raw  extends Attribute[Bytes]
  // invalid value
  // case object ohnoes extends Attribute[List[String]]

  type Item = FieldType[id.type, id.Value]     ::
              FieldType[name.type, name.Value] ::
              FieldType[age.type, age.Value]   :: HNil

  val item: Item =  (id ->> 23423)             ::
                    (name ->> "Paco Romero")   ::
                    (age ->> 34)               :: HNil
}
```

