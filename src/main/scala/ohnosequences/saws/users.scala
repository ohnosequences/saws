package ohnosequences.saws

trait AnyIAMUser {
  type Account <: AnyAccount
}
trait IAMUser[A <: AnyAccount] extends AnyIAMUser { type Account = A }