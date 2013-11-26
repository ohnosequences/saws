# records for DynamoDB

### tables

A DynamoDB table has an associated _schema_ which specifies

- the primary key type: composite (hash + range) or just hash
- the corresponding key/s
- any local secondary indexes
- initial _state_ for the table (throughput etc)

### attributes

This is just a field: a key-value pair. The key must be a `String`; for value types you are constrained to

``` scala
type Bytes = Seq[Byte]
type Num = Long
type Values = either[Num]#or[String]#or[Bytes]#or[Set[Num]]#or[Set[String]]#or[Set[Bytes]]
// not documented
type PrimaryKeyValues = either[String]#or[Num]
```

It looks like right now you can only use `String` or `Num` values for primary keys: [SO - Does DynamoDB support using one of the set datatypes in a table's primary key?](http://stackoverflow.com/questions/8926017/does-dynamodb-support-using-one-of-the-set-datatypes-in-a-tables-primary-key).

For the non-`Set` values, there is an order available which will be used when making comparisons:

- `Bytes`: as unsigned `Byte`s
- `String`: ASCII codes
- `Long`: standard

All this is detailed in [DynamoDB dev guide - data model](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DataModel.html).

### Implementation (WIP)

For attributes, I think we could subclass `FieldOf[V]`

``` scala
class DynamoDBAttribute[V: oneOf[Values]#is] 
  extends FieldOf[V]
class DynamoDBKeyAttribute[V : oneOf[Values]#is : oneOf[PrimaryKeyValues]#is]
  extends DynamoDBAttribute[V]
```

Then, a record is just a shapeless record; I don't know if it would be a good idea to switch to `TypeSet`s here (or `TypeMap`s, or some other name). If we do this, we should provide some sort of conversion to shapeless records (basically `.toHList`). In the shapeless implementation, a record is just `record: HList[FieldType[K, V]]`; we will have here `record: TypeSet[FieldType[K, V]]`. We could use the singleton thing from shapeless anyway.

We could have then something like `type RecordOf[S <: TypeSet]` for modeling an entry (_item_ in DynamoDB parlance).

``` scala
sealed trait PrimaryKey
trait HashKey {
  type HashKey  <: DynamoDBKeyAttribute
}
trait HashRangeKey {
  type HashKey  <: DynamoDBKeyAttribute
  type RangeKey <: DynamoDBKeyAttribute
}
trait DynamoDBTable {
  // ofDynamoDBAttributes
  type Attributes <: TypeSet
  type Key <: PrimaryKey
}

trait Item {
  type Table <: DynamoDBTable
  // fields go here as a type set etc
}
```

We should also model table state; the main thing here is provisioned throughput; we could perfectly use records here:

``` scala
case object ReadCapacity  extends FieldOf[Int]
case object WriteCapacity extends FieldOf[Int]
type TableThroughput = RecordOf[ReadCapacity :+: WriteCapacity]
```

#### references

- [shapeless - records](https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/records.scala) the type defs
- [sqltyped - records](https://github.com/jonifreeman/sqltyped/blob/master/core/src/test/scala/recordexamples.scala) part of a lib for doing funny stuff with relational DBs
- [DynamoDB - data model](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DataModel.html) available data types and restrictions
- [DynamoDB - limits](http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Limits.html) for names, sizes, etc