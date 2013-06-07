package ohnosequences.saws

// regions
sealed trait RegionAux {
  val name: String
}
case object EU extends RegionAux { val name = "eu-west-1" }
case object US extends RegionAux { val name = "us-east-1" }
case object US_WEST_1 extends RegionAux { val name = "us-west-1" }
case object US_WEST_2 extends RegionAux { val name = "us-west-2" }
case object US_WEST_3 extends RegionAux { val name = "us-west-3" }