package ohnosequences.saws

import typeOps._

trait AnyService {
  
  type Region <: AnyRegion
  type Account <: AnyAccount
  // type Auth <: AnyAuth
  val region: Region
  val account: Account
  // val auth: Auth

  val host = "amazonaws.com"
  val namespace: String
  // add here isSecure or something similar
  def endpoint: String

}


