# akka-cluster-typed

This repository is a sandbox playing with Akka cluster typed.

```
+----------+
| Guardian |
+----------+
     |
 +-------+
 | Drone |
 +-------+
     |
 +--------+
 | Worker |
 +--------+        
```

### [Timer](https://doc.akka.io/docs/akka/current/typed/interaction-patterns.html#scheduling-messages-to-self)

- Create a timer key `WorkTimerKey`.
- Schedule to send `Activate` message without delay.
- Schedule timer for every 1 second send timer key `WorkTimerKey` a `New` message. `FixedDelay` will not be delayed by other factors such as system, environment etc.
- Cancel timer scheduler identified by timer key `WorkTimerKey`.

```
# Guardian 

private case object WorkTimerKey

timers.startSingleTimer(Activate, 0.seconds)

timers.startTimerWithFixedDelay(WorkTimerKey,
                                      New(workCount, context.self),
                                      1.second)

timers.cancel(WorkTimerKey)
```

### [Routing](https://doc.akka.io/docs/akka/current/typed/routers.html) 

Create a 5-routee pool router and it restarts the blocking dispatched actor when there is an exception failure.

```
# Guardian

Routers
  .pool(5)(
    // Restart drones if they fail
    Behaviors
      .supervise(Drone.apply)
      .onFailure[Exception](SupervisorStrategy.restart))
  .withRouteeProps(routeeProps = DispatcherSelector.blocking)
```

Create a router group with `groupServiceKey` on random routing and it supervises workers and restarts
the actors when there is an execption failure. New workers can be created anonymous and registered to 
the group by sending `Register` key to system receptionist.

```
val group = Routers.group(groupServiceKey).withRandomRouting
// Restart work if it fails
val worker = context.spawnAnonymous(
  Behaviors
    .supervise(Worker.apply)
    .onFailure[Exception](SupervisorStrategy.restart))
context.system.receptionist ! Receptionist.Register(groupServiceKey,
                                                    worker)
val router: ActorRef[Buzz] = context.spawn(group, "worker-group")
```

### [Cluster](https://doc.akka.io/docs/akka/current/typed/cluster.html)

```
# application.conf

akka.actor.provider = "cluster"
akka.remote.artery.canonical.hostname = "127.0.0.1"
akka.remote.artery.canonical.port = 12010
akka.remote.artery.canonical.port = ${?SERVICE_PORT}
akka.cluster.seed-nodes = ["akka://service@127.0.0.1:12010", "akka://service@127.0.0.1:12020"]
akka.cluster.roles = ["drone"]
akka.cluster.multi-data-center.self-data-center = "dub"
```

#### Add [serializer](https://doc.akka.io/docs/akka/2.6/serialization-jackson.html)

```
# application.conf

akka.cluster.downing-provider-class = "akka.cluster.sbr.SplitBrainResolverProvider"
akka.actor.warn-about-java-serializer-usage = off
akka.actor.serialization-bindings."CborSerializable" = jackson-cbor
```

Extends message classes with `CborSerializable` as stated in `application.conf`.

```
sealed trait CborSerializable
```

#### [Distributed Pub Sub](https://doc.akka.io/docs/akka/current/typed/distributed-pub-sub.html)

- Create anonymous actor on `Buzz` type `notification` topic 
- Subscribe to topic
- Publish `Notification` message to topic

```
# Guardian

val notification: ActorRef[Topic.Command[Buzz]] =
        context.spawnAnonymous(Topic[Buzz]("notification"))

notification ! Topic.Subscribe(context.self)

notification ! Topic.Publish(
                  Notification(
                    context.self,
                    s"${context.system.address}: notification $workCount"))
```

## Run steps

- Seed nodes are set to `12010` and `12020`, two nodes need to be up before others can join.
- Export environment variable to override it from `application.conf`.

```
# akka.cluster.seed-nodes = ["akka://service@127.0.0.1:12010", "akka://service@127.0.0.1:12020"]

akka-cluster-typed $ export SERVICE_PORT=<service port>
akka-cluster-typed $ sbt run
```

## References

- Configuration: https://doc.akka.io/docs/akka/current/general/configuration-reference.html
- Serializers: https://doc.akka.io/docs/akka/2.6/serialization-jackson.html
- K8s discovery: https://doc.akka.io/docs/akka-management/current/discovery/kubernetes.html
- K8s form a cluster: https://doc.akka.io/docs/akka-management/current/kubernetes-deployment/forming-a-cluster.html
