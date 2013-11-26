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
import shapeless._
```

  ## attributes

  Attributes are just key-values in the DynamoDB terminology. Note that all these types are independent from any particular implementation of Items.


```scala
object AnyAttribute {
  type of[V] = AnyAttribute { type Value = V }
}
```

this is sealed so that we can enforce the `Value` bound restricting it to be one of those valid for the the DynamoDB service

```scala
sealed trait AnyAttribute { type Value }

  class Attribute[V: oneOf[Values]#is] extends AnyAttribute with FieldOf[V] { type Value = V }
```

  I want to investigate here with the possibility of encoding Items as Records tagged with the corresponding Table (singleton) type.
  This way I could get rid of the nested `Item` case class.


```scala
object AnyItem {

  type ItemType[S <: HList, A <: HList] = A with SchemaTag[S, A]
  trait SchemaTag[S <: HList, A <: HList]
  // now tag items with the schema coming from table.schema
}
```

