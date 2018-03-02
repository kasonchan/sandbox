# sandbox-akka-actors-termination

## Backoff Supervisor Pattern to delay restart

```
val backOffOnStopSupervisor: Props = BackoffSupervisor.props(
    Backoff
      .onStop(
        Props(classOf[Minion]),
        childName = "minion",
        minBackoff = 1.millisecond,
        maxBackoff = 1.milliseconds,
        randomFactor = 0
      )
      .withSupervisorStrategy(
        OneForOneStrategy(maxNrOfRetries = maxNrOfRetries,
                          withinTimeRange = 2.millisecond) {
          case _ => Restart
        }
      ))
```

- Define the `Props` for a backoff supervisor will start with a `Minion` actor
- `minBackOff` is the starting delay time and increment with `randomFactor` 
  until the `maxBackOff` time
- `randomFactor` is in percentage noise to vary the intervals slightly, 
  i.e. put 0.20 to add 20% each time with a growing time delay between restarts
- `Backoff.onStop` supervision is triggered when the child actor stop, 
  while `Backoff.onFailure` is triggered when the child actor throws an exception
- `.withSupervisorStrategy(...)` is a fallback strategy, same as normal explicit 
  SupervisorStratey to either `Restart`, `Resume`, `Stop`, or `Escalate` to the 
  ascendent if the child throws an exception
- `.onFailure` supervisor should not be used with `Akka Persistence` child actors 
as Akka team recommended
  - As `Akka Persistence` actors shutdown unconditionally on `persistFailure()`s 
    rather than throw an exception on a failure like normal actors
- Example show above will always restart the child actor immediately within 
  1 millisecond

## Terminate an actor

```
import akka.actor.Kill

actor ! Kill
```

- Kill an actor, it thows `akka.actor.ActorKilledException: Kill`

```
import akka.actor.PoisonPill

actor ! PoisonPill
```

- `PoisonPill` is a ending message queued to the actor
  - the actor will be stop after processing the `PoisonPill` message without throwing exception

```
context.stop(actor)
```

- Stop an actor this way will stop the actor immediately

## References

- http://blog.colinbreck.com/integrating-akka-streams-and-akka-actors-part-iii/
- https://petabridge.com/blog/how-to-stop-an-actor-akkadotnet/
- https://doc.akka.io/docs/akka/2.5/general/supervision.html
