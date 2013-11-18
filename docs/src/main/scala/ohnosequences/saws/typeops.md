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

object typeOps {
  
  // after http://www.chuusai.com/2011/06/09/scala-union-types-curry-howard/#comment-179
  // Lars Hupel
  trait OneOfAux {

    type or[S] <: OneOfAux
    type apply
  }
  // need to add NotIn based on sum type bounds
  trait OneOf[T] extends OneOfAux {  
    type or[S] = OneOf[T with ¬[S]]
    type apply = ¬[T]
  }

  // for convenience
  trait Is[T] extends OneOf[¬[T]]

  type oneOf[W <: OneOfAux] = {
    type λ[X]  = ¬¬[X] <:< W#apply
    type is[X] = ¬¬[X] <:< W#apply
  }

  // stupid alias
  type either[T] = Is[T]
}
```

