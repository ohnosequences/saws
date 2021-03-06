package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._
/*
  These constraints should be specialized to this particular context
*/
import shapeless.{HList, KeyConstraint}
import shapeless.LUBConstraint._

object AnyTable {

  type from[S <: AnyDynamoDBService] = AnyTable { type Service = S }
  // type AnyTableState[] = AnyDynamoDBStateOf[AnyTable]
}
trait AnyTable extends AnyDynamoDBResource { thisTable =>

  type Service <: AnyDynamoDBService
  // maybe?
  val service: Service
  
/*
  The primary key of this table. I need to add a constraint for `primaryKey` being part of the `keys` of this table.
  This is not as easy as it sounds: DynamoDB primary keys can consist of several keys; we will thus need ad-hoc traversals across the `Keys` HList.
*/
  type PrimaryKey <: AnyPrimaryKey
  val primaryKey: PrimaryKey

/*
  The set of keys for this table; they're supposed to be `Attribute`s in the end.
*/
  type Keys <: HList
  val keys: Keys

/*
  the problem we have here is how to model the type of items conforming to that attribute; 
  in the shapeless records case we need `HList[FieldType[attributes, attributes.value]]`
  we can do as with Deps in the Bundle case, and extract this type here through implicits
  _If_ using HLists we will need to modify KeyConstraint
  https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/hlistconstraints.scala#L75

  ### `TypeSet`s?

  If using them for records, as here there cannot be any duplicates, it is enough to check just two things:

  1. all of them come from `FieldValue`s from `Keys`
  2. they have the same length

  Anyway for now what we have is this, which only checks that all the fields that you add there are declared in `Keys`
*/
  type Attributes = {  type is[I <: HList] = KeyConstraint[I, Keys]  }

/*
  I need this here due to SI-5712
  
  Ideally it would be outside AnyTable; but the best we can get right now in this case is
    
  - `Item[T <: AnyTable, A <: HList: OfAttributesFrom[T]#is](attributes: A)`
*/
  // we should be able to get the type of Items from this table, so that we can get rid of the I parameter
  // the implicit will disappear too
  case class Item[I <: HList: thisTable.Attributes#is](attributes: I) {
    type Table = thisTable.type
    val table: Table = thisTable
  }
}
  /*  
  A constructor for `AnyTable`; maybe I should seal the `AnyTable` trait and leave this as the only entry point.
  The drawback is that abstract refinement becomes at best clumsy; but maybe that's a good thing!
  */
  abstract class Table[
      PK <: AnyPrimaryKey,
      K <: HList: <<:[AnyAttribute]#λ, // add here a key constraint
      S <: AnyDynamoDBService
    ](val primaryKey: PK, val keys: K, val service: S) extends AnyTable with AnyDynamoDBResource {

      type PrimaryKey = PK
      type Keys = K
      type Service = S

      /*  
      all this is the resource part
      */
      type ARN = DynamoDBARN[this.type]
      val arn: ARN = DynamoDBARN[this.type](this:this.type)
      def asString = this.toString
  }

/*
  The state ADT for tables.
*/
trait AnyTableState extends AnyDynamoDBState {

  type Resource <: AnyTable
}
// indexes maybe here
case class InitialState[T <: AnyTable](table: T, throughput: TableThroughput) 
  extends AnyTableState {
  type Resource = T
  val  resource = table
  type Status = NOTTHERE.type; val status = NOTTHERE
}

  trait AnyExistingTableState extends AnyTableState  {

    /* this contains both read and write capacity units
    */
    val throughput: TableThroughput
  
    type Status <: AnyExistingTableStatus
    val status: Status
    val created: Long
    val size: Long
  }
    case class TableState[T <: AnyTable, S <: AnyExistingTableStatus](
      val table: T,
      val throughput: TableThroughput,
      val status: S
    )(
      val created: Long,
      val size: Long
    ) extends AnyTableState {

      type Resource = T
      val  resource = table
      type Status = S
    }

object AnyTableState {

  type of[T <: AnyTable] = AnyTableState { type Resource = T }
  type status[S <: AnyTableStatus] = AnyTableState { type Status = S }
  type withStatus[S <: AnyTableState] = { type is[s <: AnyTableStatus] = AnyTableState { type Status = s } }
}

/*
  table primary key ADT
*/
sealed trait AnyPrimaryKey
  /*
    A simple hash key
  */
  trait AnyHash extends AnyPrimaryKey {
    type HashKey  <: AnyAttribute
  }
    case class Hash[
        H: oneOf[Values]#is: oneOf[PrimaryKeyValues]#is
      ](hash: AnyAttribute.of[H]) extends AnyHash {        
      type HashKey = hash.type
    }
  /*
    A composite primary key
  */
  trait AnyHashRange extends AnyPrimaryKey {
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

sealed trait AnyTableStatus
  case object NOTTHERE extends AnyTableStatus
  sealed trait AnyExistingTableStatus extends AnyTableStatus
    case object CREATING extends AnyExistingTableStatus
    case object UPDATING extends AnyExistingTableStatus
    case object DELETING extends AnyExistingTableStatus
    case object ACTIVE   extends AnyExistingTableStatus

import shapeless._
// TODO do this with TypeSet-based and/or shapeless records
case object ReadCapacity  extends Attribute[Int]
case object WriteCapacity extends Attribute[Int]
