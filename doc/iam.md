# IAM concepts

All the IAM stuff looks pretty involved at first sight, but it is fairly easy once you abstract over it a bit.

## resources

Everything is a _resource_, and ultimately all aws things are addressable via their `arn`: _Amazon Resource Name_. This is just a string, with the structure

> `arn:aws:<service>:<region>:<namespace>:<relative-id>`

Now, all IAM stuff (users, groups, roles) are also resources.

## actions

An action is composed by (horrible aws terminology):

  1. **principal** this is the resource performing the action
  2. **action** the action being performed: createUser, deleteObject, etc
  3. **resource** the resource being acted upon.
  4. **conditions** a predicate (based on time, resource type, etc) which determines if the action can be performed.

## permissions

A permission is a specification of a set of actions. 

## policies

This is an assignment of a permission to _one of_ the two resources involved: the _principal_ or the _resource_. If you apply the policy to the principal this is said to be a _user-based_ permission; if you apply it to the resource, a _resource-based_ one. In fact, it is a completely artificial distinction: you can constrain both principals and resources in the same way, at the same time.

