package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.regions._

object testServices {

  import shapeless._
  import shapeless.ops.coproduct._

  case object intercrossingDynamoEU extends DynamoDBService(EU, intercrossing) {

    import shapeless.ops.coproduct._

    def create[
      T <: AnyTable.from[here], 
      S <: InitialState[T]
    ](arg: AnyDynamoDBService.CreateTable[T,S]): (arg.Output, arg.OutputState) = {

      val t: arg.Output = arg.table
      val tp = arg.state.throughput

      val outState = TableState(
        table = t,
        throughput = tp,
        status = CREATING
      )(
        created = System.nanoTime(),
        size = 0
      )
      if (outState.created % 2 == 0) {
        (
          arg.table: arg.Output, 
          Coproduct[arg.OutputState](outState)
        )
      } else {
        ( 
          arg.table: arg.Output, 
          Coproduct[arg.OutputState](arg.Exists: arg.Errors)
        )
      }
      
    }

  }
}