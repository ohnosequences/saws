package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

trait AnyDynamoDBService extends AnyService {

  type validRegions = Is[EU]#or[US]
  val namespace = "dynamodb"
}

trait AnyTable extends AnyDynamoDBResource {
  // as a type set etc
  // type Attributes <: TypeSet
  type Key <: PrimaryKey
}
trait AnyTableState extends AnyDynamoDBStateOf { 

  type Resource <: AnyTable 
  val throughput: TableThroughput
}

// table primary key ADT
sealed trait PrimaryKey
  trait Hash      extends PrimaryKey {
    type HashKey  <: AnyKeyAttribute
  }
  trait HashRange extends PrimaryKey {
    type HashKey  <: AnyKeyAttribute
    type RangeKey <: AnyKeyAttribute
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
