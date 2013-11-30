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

  trait AnyCreateTable extends AnyAction {

    // note how here we're bounding the implementation to return the very same table
    type Input        <: AnyTable
    // this should be not there
    type InputState   <: AnyTableState.of[Input]
    type Output       <: Input
    type OutputState  <: AnyTableState.of[Output] :+: Errors :+: CNil

    type Errors       <: AnyTableState.of[Output]
  }

  case class CreateTable[T <: AnyTable, S <: InitialState[T]](
    val table: T,
    val state: S
  ) extends AnyCreateTable {

    // note how here we're bounding the implementation to return the very same table
    type Input        = T
    type InputState   = S
    // TODO impl this
    type Output        = Input
    type OutputState   = TableState[Output, CREATING.type] :+: Errors :+: CNil

    type Errors       = Error

    sealed trait Error extends AnyTableState { type Resource = Output; val resource = table }

      case object Exists        extends Error
      case object LimitExceeded extends Error

  }

} 