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
            + [queues.scala](queues.md)
            + [service.scala](service.md)
            + [messages.scala](messages.md)
          + [accounts.scala](../accounts.md)
          + [regions.scala](../regions.md)
          + [users.scala](../users.md)
          + dynamodb
            + [package.scala](../dynamodb/package.md)
            + [tables.scala](../dynamodb/tables.md)
            + [attributes.scala](../dynamodb/attributes.md)
            + [services.scala](../dynamodb/services.md)
          + [typeops.scala](../typeops.md)
          + [resources.scala](../resources.md)
          + [services.scala](../services.md)
          + ec2
            + [service.scala](../ec2/service.md)

------


```scala
package ohnosequences.saws.sqs

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._


trait AnySQSService extends AnyService {

  type validRegions = Is[EU]#or[US]
  val namespace = "sqs"

  // add here bound for queue matching this service
  // maybe queue.type??
  // it would be nice if we could bound the state to be just of Q
  def create[Q <: AnyQueue, S <: StateOf[Q]](queue: Q): (Q, S)
}

abstract class SQSService[
  R <: AnyRegion : oneOf[AnySQSService#validRegions]#λ,
  A <: AnyAccount
](val region: R, val account: A) extends AnySQSService {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

// example sqs service
// case object sqs_service extends SQSService(EU, intercrossing)
```

