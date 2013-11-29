package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._

import shapeless._

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

object AnyDynamoDBService {

  trait AnyCreateTable extends AnyAction {

    // note how here we're bounding the implementation to return the very same table
    type Input        <: AnyTable
    type InputState   <: AnyTableState.of[Input]
    type Output        = Input
    type OutputState  <: AnyTableState.of[Output] with AnyTableState.status[CREATING.type] :+: 
                         Errors :+: CNil

    type Errors       <: AnyTableState.of[Output]
  }

  case class CreateTable[T <: AnyTable, S <: InitialState[T]] extends AnyCreateTable {

    // note how here we're bounding the implementation to return the very same table
    type Input        = T
    type InputState   = S
    // TODO impl this
    type OutputState  = AnyTableState.of[Output] with AnyTableState.status[CREATING.type] :+: 
                        Errors :+: CNil

    type Errors       = AnyTableState.of[Output]

  }

} 