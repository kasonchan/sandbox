# sandbox-gatling

The repository is created for playing with Gatling, an Open-Source Load & 
Performance Testing Tool For Web Applications.

### Develop gatling script locally

Test your gatling scripts locally:

```
$ cd sandbox-gatling
$ sbt gatling:test
```

Clean up built gatling script locally:

```
$ sbt clean
```

---

### Gatling script deployment and execution

To package Gatling bundle, `gatling.zip` will be created on the parent directory:

```
$ cd gatling
$ sbt packageGB
```

To clean built Gatling bundle locally: 

```
$ sbt cleanGB
```

To run your gatling script after deployment. Reports and logs will be created in 
`result` directory after execution:

```
$ unzip gatling.zip
$ cd gatling
$ bin/gatling.sh
```
