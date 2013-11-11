package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

trait AnyDynamoDBService extends AnyService {

  type validRegions = Is[EU]#or[US]
  val namespace = "dynamodb"
}

abstract class DynamoDBService[
    R <: AnyRegion: oneOf[AnyDynamoDBService#validRegions]#is,
    A <: AnyAccount
  ](val region: R, val account: A) extends AnyDynamoDBService {

    type Region = R
    type Account = A
    def endpoint = "https://" + namespace +"."+ region.name + host
  }

trait AnyTable extends AnyDynamoDBResource {
  // as a type set etc
  // type Attributes <: TypeSet
  type Key <: PrimaryKey
  val key: Key
}
  abstract class Table[
      K <: PrimaryKey,
      S <: AnyDynamoDBService
    ](val key: K, val service: S) extends AnyTable {
      type Key = K
      type Service = S

      type ARN = DynamoDBARN[this.type]
      val arn = DynamoDBARN[this.type](this)
      def asString = this.toString
  }
trait AnyTableState extends AnyDynamoDBStateOf { 

  type Resource <: AnyTable 
  val throughput: TableThroughput
}

// table primary key ADT
sealed trait PrimaryKey

  trait AnyHash extends PrimaryKey {
    type HashKey  <: AnyAttribute
  }
    case class Hash[
        H: oneOf[Values]#is: oneOf[PrimaryKeyValues]#is
      ](hash: AnyAttribute.of[H]) extends AnyHash {        
      type HashKey = hash.type
    }
  trait AnyHashRange extends PrimaryKey {
    type HashKey  <: AnyAttribute
    type RangeKey <: AnyAttribute
  }
    case class HashRange[
        H: oneOf[Values]#is: oneOf[PrimaryKeyValues]#is,
        R: oneOf[Values]#is: oneOf[PrimaryKeyValues]#is
      ](hash: AnyAttribute.of[H], range: AnyAttribute.of[R]) extends AnyHashRange {
        type HashKey  = hash.type
        type RangeKey = range.type
      }

sealed trait TableStatus
  case object CREATING extends TableStatus
  case object UPDATING extends TableStatus
  case object DELETING extends TableStatus
  case object ACTIVE   extends TableStatus

// TODO do this with TypeSet-based and/or shapeless records
case object ReadCapacity 
  // extends FieldOf[Int]
case object WriteCapacity 
  // extends FieldOf[Int]
