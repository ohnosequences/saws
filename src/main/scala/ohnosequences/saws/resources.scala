package ohnosequences.saws


trait ResourceAux {  type service <: ServiceAux  }

trait StateOfAux {  type resource <: ResourceAux  }
abstract class StateOf[R <: ResourceAux](r: R) {  type resource = R  }

