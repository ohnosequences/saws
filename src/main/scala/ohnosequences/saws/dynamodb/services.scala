package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

import shapeless._

trait AnyDynamoDBService extends AnyService {

  type validRegions = either[EU]#or[US]
  val namespace = "dynamodb"

  type here = this.type

  def create[
    T <: AnyTable.from[here], 
    S <: InitialState[T]
  ](arg: AnyDynamoDBService.CreateTable[T,S]): (arg.Output, arg.OutputState)
}

abstract class DynamoDBService[
    R <: AnyRegion: oneOf[AnyDynamoDBService#validRegions]#is,
    A <: AnyAccount
  ](val region: R, val account: A) extends AnyDynamoDBService {

    type Region = R
    type Account = A
    def endpoint = "https://" + namespace +"."+ region.name + host
  }

object AnyDynamoDBService {

  import AnyTable._

  trait AnyCreateTable extends AnyModifyResourceState {

    // note how here we're bounding the implementation to return the very same table
    type Input        <: AnyTable
  }

  case class CreateTable[T <: AnyTable, S <: InitialState[T]](
    val table: T,
    val state: S
  ) extends AnyCreateTable {

    // note how here we're bounding the implementation to return the very same table
    type Input        = T
    // TODO impl this
    type OutputState   = TableState[Output, CREATING.type] :+: Errors :+: CNil
    type Errors       = Error

    sealed trait Error extends AnyTableState { type Resource = Output; val resource = table }

      case object Exists        extends Error
      case object LimitExceeded extends Error

  }

  trait AnyWriteItem extends AnyAction {

    import AnyTableState._
    type Input <: AnyTable
    val table: Input
    type I <: HList
    // type InputState <: of[Input] with status[ACTIVE.type] :: table.Item[I] :: HNil
    type InputState <: table.Item[I] :: HNil
    val state: InputState

    type Output = Input
    type OutputState = Errors :+: CNil

    sealed trait Errors
      case class ItemTooBig(val item: table.Item[I]) extends Errors
      case object WrongTableState extends Errors

  }

  // no dep constructors, ugly workaround
  // TODO move this to TableOps?
  def writeItem[T <: AnyTable, I0 <: HList](t: T)(i: t.Item[I0]): AnyWriteItem = new AnyWriteItem {

    type I = I0
    type Input = t.type
    val table: t.type = t
    type InputState = t.Item[I0] :: HNil
    val state: table.Item[I] :: HNil = i :: HNil
  }
  // case class writeItem[T <: AnyTable, I <: HList](val table: T)(val item: table.Item[I])

}