package ohnosequences.saws

// a good rule of thumb for whether something should be a resource:
// it has an ARN? if not...
trait AnyResource {  
  type Service <: AnyService
  val service: Service
  val arn: ARN
}

trait ARN

trait AnyStateOf {  
  type Resource <: AnyResource
  val resource: Resource
}
abstract class StateOf[R <: AnyResource](r: R) {  
  type Resource = R
  val resource = r
}

