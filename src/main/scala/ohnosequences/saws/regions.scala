package ohnosequences.saws

// regions
sealed trait Region {
  val name: String
}
case object EU extends Region { val name = "eu-west-1" }
case object US extends Region { val name = "us_east-1" }
case object US_WEST_1 extends Region { val name = "us_west-1" }
case object US_WEST_2 extends Region { val name = "us_west-2" }
case object US_WEST_3 extends Region { val name = "us_west-3" }