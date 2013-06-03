package ohnosequences.saws

trait IAMUserAux {
  type account <: AccountAux
}
trait IAMUser[A <: AccountAux] extends IAMUserAux { type account = A }