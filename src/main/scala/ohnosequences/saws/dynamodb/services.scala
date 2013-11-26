package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.typeOps._
import ohnosequences.saws.regions._ 

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