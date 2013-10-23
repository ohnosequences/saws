package ohnosequences.saws

import shapeless._
import shapeless.Nat._
import shapeless.Sized._

trait AnyAccount {
  
  // this is awkward. Plan to improve this with macros
  type ID = IndexedSeq[Char] Sized _12
  val id: ID
  // need to get more instances of Nats for this to work
  // type CanonicalID = Seq[Char] Sized _64
  // val canonical_id: CanonicalID
}

// sample
case object intercrossing extends AccountAux {
  
  val id = Sized('8', '5', '4', '9', '2', '3', '7', '7', '4', '0', '4', '3')
}