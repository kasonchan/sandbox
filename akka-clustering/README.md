# sandbox-akka-clustering

## Execution

- To execute Master
```
sbt -Dconfig.resource=master.conf "runMain Master"
```

- To execute Minion
```
sbt -Dconfig.resource=master.conf "runMain Minion"
```
