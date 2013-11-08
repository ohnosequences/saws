package ohnosequences.saws

// a good rule of thumb for whether something should be a resource:
// it has an ARN? if not...
trait AnyResource {  
  type Service <: AnyService
  val service: Service
  val arn: ARN
}

trait ARN

object AnyState {
  type of[R <: AnyResource] = AnyStateOf {  type Resource = R  }
}
trait AnyStateOf {  
  type Resource <: AnyResource
  val resource: Resource
}
abstract class StateOf[R <: AnyResource](r: R) {  
  type Resource = R
  val resource = r
}

// just experimenting...
trait AnyAction {

  type Input  <: AnyResource
  type Output <: AnyResource

  type InputState   <: AnyState.of[Input]
  type OutputState  <: AnyState.of[Output]

  def act(r: Input, s: InputState): (OutputState, Output)
}

