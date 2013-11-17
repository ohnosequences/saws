package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

import shapeless.{HList, KeyConstraint}

trait AnyDynamoDBService extends AnyService {

  type validRegions = either[EU]#or[US]
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

trait AnyTable extends AnyDynamoDBResource { thisTable =>
  
  // the primary key for this table
  type PKey <: PrimaryKey
  val pKey: PKey

  // as a type set bounded by the AnyAttribute type
  // type Attributes <: TypeSet.of[AnyAttribute]
  type Keys <: HList
  val keys: Keys

  // the problem here is how to model the type of items conforming to that attribute; 
  // in the shapeless records case we need HList[FieldType[attributes, attributes.value]]
  // we can do as with Deps in the Bundle case, and extract this type here through implicits
  // _If_ using HLists we will need to modify KeyConstraint
  // https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/hlistconstraints.scala#L75
  // something like
  type Attributes = {  type is[I <: HList] = KeyConstraint[I, Keys]  }
  // I need this here due to SI-5712
  // ideally it would be outside AnyTable; but the best we can get right now in this case is
  //    Item[T <: AnyTable, A <: HList: OfAttributesFrom[T]#is](attributes: A)
  case class Item[I <: HList: thisTable.Attributes#is](attributes: I) {
    type Table = thisTable.type
    val table: Table = thisTable
  }
}
  abstract class Table[
      PK <: PrimaryKey,
      K <: HList, // add here a key constraint
      S <: AnyDynamoDBService
    ](val pKey: PK, val keys: K, val service: S) extends AnyTable {

      type PKey = PK
      type Keys = K
      type Service = S

      type ARN = DynamoDBARN[this.type]
      val arn = DynamoDBARN[this.type](this)
      def asString = this.toString
  }

// this models the table state
trait AnyTableState extends AnyDynamoDBStateOf { 

  type Resource <: AnyTable

  // dynamodb table throughput (this contains both read and write capacity)
  val throughput: TableThroughput
  type Status <: AnyTableStatus
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
