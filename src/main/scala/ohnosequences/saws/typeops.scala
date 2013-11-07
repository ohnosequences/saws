package ohnosequences.saws

import shapeless._

object typeOps {
  

  // after http://www.chuusai.com/2011/06/09/scala-union-types-curry-howard/#comment-179
  // Lars Hupel
  trait OneOfAux {

    type or[S] <: OneOfAux
    type apply
  }
  // need to add NotIn based on sum type bounds
  trait OneOf[T] extends OneOfAux {  
    type or[S] = OneOf[T with ¬[S]]
    type apply = ¬[T]
  }

  // for convenience
  trait Is[T] extends OneOf[¬[T]]

  type oneOf[W <: OneOfAux] = {
    type λ[X] = ¬¬[X] <:< W#apply
    type is[X] = ¬¬[X] <:< W#apply
  }

  type either[T] = Is[T]
}
