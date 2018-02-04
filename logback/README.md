# sandbox-logback

The repository is created for playing with Logback, an Open-Source project that 
is intended as a successor to the popular log4j project, picking up where log4j 
leaves off.

To use logback, add the following to `build.sbt`

```
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
```

Add the following akka library too if you are using logback with akka:

```
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.9"
)
```

## Create configuration

Create a configuration file `logback.xml` in `src/main/resources`

```
<configuration>

  <conversionRule conversionWord="coloredLevel" converterClass="logger.ColoredLevel" />

  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{ISO8601} %coloredLevel from %logger{36} in %thread - %message%n%xException</pattern>
    </encoder>
  </appender>

  <appender name="ASYNCSTDOUT" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="STDOUT" />
  </appender>

  <appender name="FILE" class="ch.qos.logback.core.FileAppender">
    <file>${application.home:-.}/logs/application.log</file>
    <encoder>
      <pattern>%d{ISO8601} [%level] from %logger{36} in %thread - %message%n%xException</pattern>
    </encoder>
  </appender>

  <appender name="ASYNCFILE" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="FILE" />
  </appender>

  <root level="DEBUG">
    <appender-ref ref="ASYNCSTDOUT" />
    <appender-ref ref="ASYNCFILE" />
  </root>

</configuration>
``` 

- `ConversionRule` denotes your custom converter class
- Appender `STDOUT` denotes the standout logging with `encoder` of
  `date` in `ISO8601` format colored `level` `logger` `thread` - `message` and `exception`
- Appender `FILE` denotes the same encoder without the coloring and write the log to
  `${application.home:-.}/logs/application.log`
- `root` level is set to `DEBUG`

## Create colored level

Create `ColoredLevel` to match different `Level` to different color

## References

- https://logback.qos.ch/manual/layouts.html
- https://www.playframework.com/documentation/2.6.x/SettingsLogger
