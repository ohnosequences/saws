
# AWS data model in Scala

## Scopes

All across AWS something that we could call _scopes_ play a really important role.

### top-level entities

Here we can at least consider

##### 1. accounts

AWS accounts are global entities, and they are not linked with anything

##### 2. regions

Regions are completely independent of each other, and also not linked with accounts in any way.

### all the rest

all other resources and entities ultimately depend on one (or several) of those ?? top-level entities. For example,

1. IAM users are linked to a particular account, but _not_ to a specific region
2. services have region-specific endpoints, and they are all the same for every account.

## ARNs

Amazon uses something called _Amazon Resource Name_, which acts as a global identifier for a resource. It is just a string, and incorporates scoping, in a fairly obvious way:

- `arn:aws:service:region:account:resource`

As AWS is all about non-uniform conventions, they use at least the following variations:

- `arn:aws:service:region:account:resource`
- `arn:aws:service:region:account:resourcetype/resource`
- `arn:aws:service:region:account:resourcetype:resource`

And obviously, to make things easier, each service follows different conventions. Good thing is that now they are documented: 

- [AWS General Reference - ARNs and namespaces](http://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html)

### EC2 and ARNs

EC2 has no notion of ARN, which is sad and unconvenient. The explanation as to why it is so from the official docs is circular at best:

> Amazon EC2 has no Amazon Resource Names (ARNs) because you can't specify particular Amazon EC2 resources in an IAM policy. When you write a policy to control access to Amazon EC2 actions, use * as the resource.
>
> from: [EC2 user guide](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/UsingIAM.html)

##### auto scaling to the rescue

However, not all hope is lost! [Auto Scaling](http://docs.aws.amazon.com/AutoScaling/latest/DeveloperGuide/) does have ARNs for groups: [Auto Scaling ARN syntax](http://docs.aws.amazon.com/general/latest/gr/aws-arns-and-namespaces.html#arn-syntax-autoscaling), which means that you can always refer to instances through the corresponding autoscaling group wrapping it.
