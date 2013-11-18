package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._
import shapeless.{HList, KeyConstraint}
import shapeless.LUBConstraint._

object AnyTable {}
trait AnyTable extends AnyDynamoDBResource { thisTable =>
  
  /*
    The primary key of this table. I need to add a constraint for `primaryKey` being part of the `keys` of this table.
    This is not as easy as it sounds: DynamoDB primary keys can consist of several keys.
  */
  type PrimaryKey <: AnyPrimaryKey
  val primaryKey: PrimaryKey

  type Keys <: HList
  val keys: Keys

  /*
    the problem here is how to model the type of items conforming to that attribute; 
    in the shapeless records case we need HList[FieldType[attributes, attributes.value]]
    we can do as with Deps in the Bundle case, and extract this type here through implicits
    _If_ using HLists we will need to modify KeyConstraint
    https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/hlistconstraints.scala#L75
    something like
  */
  type Attributes = {  type is[I <: HList] = KeyConstraint[I, Keys]  }

  /*
    I need this here due to SI-5712
    ideally it would be outside AnyTable; but the best we can get right now in this case is
      `Item[T <: AnyTable, A <: HList: OfAttributesFrom[T]#is](attributes: A)`
  */
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
    ](val primaryKey: PK, val keys: K, val service: S) extends AnyTable {

      type PrimaryKey = PK
      type Keys = K
      type Service = S

      /*
        all this is the resource part
      */
      type ARN = DynamoDBARN[this.type]
      val arn = DynamoDBARN[this.type](this)
      def asString = this.toString
  }

/*
  The state ADT for tables.
*/
trait AnyTableState extends AnyDynamoDBStateOf { 

  type Resource <: AnyTable

  /*
    this contains both read and write capacity units
  */
  val throughput: TableThroughput

  /*
    see `AnyTableStatus` for the possible values
  */
  type Status <: AnyTableStatus
  val status: Status
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
  case object CREATING extends AnyTableStatus
  case object UPDATING extends AnyTableStatus
  case object DELETING extends AnyTableStatus
  case object ACTIVE   extends AnyTableStatus

// TODO do this with TypeSet-based and/or shapeless records
case object ReadCapacity 
  // extends FieldOf[Int]
case object WriteCapacity 
  // extends FieldOf[Int]
