package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.regions._

object testServices {

  case object intercrossingDynamoEU extends DynamoDBService(EU, intercrossing)
}