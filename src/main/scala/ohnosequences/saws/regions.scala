package ohnosequences.saws

// regions
sealed trait AnyRegion {
  val name: String
}

object regions {

  type EU = EU.type
  type US = US.type
  type US_WEST_1 = US_WEST_1.type
  type US_WEST_2 = US_WEST_2.type
  type US_WEST_3 = US_WEST_3.type
  
  case object EU        extends AnyRegion { val name = "eu-west-1" }
  case object US        extends AnyRegion { val name = "us-east-1" }
  case object US_WEST_1 extends AnyRegion { val name = "us-west-1" }
  case object US_WEST_2 extends AnyRegion { val name = "us-west-2" }
  case object US_WEST_3 extends AnyRegion { val name = "us-west-3" }
}

  