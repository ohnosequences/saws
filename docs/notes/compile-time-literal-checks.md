# compile-time checks of literals

AWS requires a lot of funny constraints for literals across their APIs. As a simple example, for the name of a SQS queue

>  Maximum 80 characters; alphanumeric characters, hyphens (-), and underscores (_) are allowed.

We want to be able to express _and check_ these constraints at compile time. This is particularly a need in our approach, because our way of instantiating a queue will be

``` scala
// or something similar
case object funny extends SQSQueue("funny")
```

So, it would be pretty stupid to _know_ all this at compile-time (queue names etc are literals) and not check it. 

This post [scala-debate - Can we do custom type constrains?](https://groups.google.com/forum/?fromgroups#!topic/scala-debate/few7ZUpzMaI) will set you in the right context for what is the situation now. Also, let me underline that we are looking for _compile-time_ checks; of course you can go the private constructor route etc, but all that is happening at run-time.

## options

### type-level proofs

_If_ Scala would have support for dependent types, this is obviously the way to go; but it has at most really partial capabilities in this area. As always, keep in mind: _you can only depend on **singleton** types_. Things like `"lalala".type` would certainly help here, but not qualitatively so. 

Anyway, let's look at what you can do: for _some_ simple patterns you certainly can rely on type-level natural numbers together with HLists. The thing is that this way you can only do "structural" checks; no way of inspecting the actual values. Of course, you could always work with an `HList` of `CHAR` where this is a type-level encoding of the `Char` values that you need, but it certainly doesn't look pretty.

``` scala
trait CHAR {  def toChar = this.toString.head }
// "case" here gives you the right toString 
case object a extends CHAR
case object b extends CHAR
case object c extends CHAR
case object d extends CHAR
case object e extends CHAR
case object f extends CHAR
case object g extends CHAR
case object h extends CHAR
// ...

trait ToChar[C <: CHAR] { def apply(): Char }
object ToChar {
  // all values above as implicits here
  implicit def toChar[C <: CHAR](implicit c: C) = new ToChar[c.type] {  def apply() = c.toChar  }
}
```

This would work, but it is pretty ugly, plus not so nice in terms of client code: you'd need to create a queue name as

``` scala
// pretty awful
case object foobar extends SQSQueue(f :: o :: o :: b :: a :: r :: HNil)
```

And it will certainly make compiling even more slow.

#### references

- [travisbrown/kata-bank-ocr.scala](https://gist.github.com/travisbrown/3763016)
- [shapeless-dev - KataBankOCR](https://groups.google.com/forum/#!msg/shapeless-dev/Q0VezBW2bhQ/RKF6uGljwroJ)

### macros

In Scala 2.10, this is certainly an option. One would just create an "assert" macro which would let you check something at compile time; then just use this inside the constructor of your class (or tagged type, or value class). 

I don't like macros. In my opinion, their only use is for displaying language limitations through the need to use them. However, I think this is the only option we have now. About how to do it, I think we should try to encapsulate macro calls as much as possible, and go through the "implicits as proofs" route or value classes (if possible). You just have a generic `Constrained[V]` class together with a `Predicate[T]` and the `assert` method is used at the level of generic implicit evidence generation.

#### references

- [retronym/macrocosm - assert example](https://github.com/retronym/macrocosm/blob/master/src/main/scala/com/github/retronym/macrocosm/Macrocosm.scala#L25)
- [Scala docs - Macros](http://docs.scala-lang.org/overviews/macros/overview.html)