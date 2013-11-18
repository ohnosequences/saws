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
        + saws
          + sqs
            + [queues.scala](../sqs/queues.md)
            + [service.scala](../sqs/service.md)
            + [messages.scala](../sqs/messages.md)
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
            + [service.scala](service.md)

------


```scala
package ohnosequences.saws.ec2

import ohnosequences.saws.{AnyService, AnyRegion, AnyAccount}
import ohnosequences.saws.regions._
import ohnosequences.saws.typeOps._

trait AnyEC2Service extends AnyService {

  type validRegions = Is[EU]#or[US]#or[US_WEST_1]
  val namespace = "ec2"
}

abstract class EC2Service[
  R <: AnyRegion : oneOf[AnyEC2Service#validRegions]#λ,
  A <: AnyAccount
](val region: R, val account: A) extends AnyEC2Service {

  type Region = R
  type Account = A

  def endpoint = "https://" + namespace +"."+ region.name + host
}

/////////////////////////////////

object test {

  def isOk[R <: AnyRegion : oneOf[AnyEC2Service#validRegions]#λ](region: R) = true

  isOk(EU)
  isOk(US)
  isOk(US_WEST_1)
  // no no
  // isOk(US_WEST_2)
}
```

