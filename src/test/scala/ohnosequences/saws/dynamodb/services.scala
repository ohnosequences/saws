package ohnosequences.saws.dynamodb

import ohnosequences.saws._
import ohnosequences.saws.regions._

object testServices {

  import shapeless._
  import shapeless.ops.coproduct._

  case object intercrossingDynamoEU extends DynamoDBService(EU, intercrossing) {

    import shapeless.ops.coproduct._

    // TODO move this to a case class with default impl
    def create[
      T <: AnyTable.from[here], 
      S <: InitialState[T]
    ](arg: AnyDynamoDBService.CreateTable[T,S]): (arg.Output, arg.OutputState) = {

      // get output types from arg in scope
      import arg._

      val t: Output = table
      val tp = state.throughput

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
          table: Output, 
          Coproduct[OutputState](outState)
        )
      } else {
        ( 
          table: Output,
          // I need to explicitly annotate Exists here
          // looks like this will be needed whenever subtyping is involved
          Coproduct[OutputState](Exists: Errors)
        )
      }
      
    }
  }

}