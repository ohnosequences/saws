# dealing with errors

The idea is that coproducts can serve as good way of dealing with error handling.

``` scala
type Err <: Coproduct
type Y <: HList
def do(i: X): Err :+: Y
```

Now you can abstract over your errors and such in an open way