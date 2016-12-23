# Loggers

@since Dec-2016

Logging is super important once your application fallover. In this blog, I try to use Scala Logging which is a 
convenient and performant logging library wrapping SLF4J. Scala Logging provides `error`, `warn`, `info`, `debug` and 
`trace` options. It is easy to use.

We can configure the logs by adding a `logback.xml` configuration file to the `resources` folder.
```
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <!-- path to your log file, where you want to store logs -->
        <file>/var/tmp/sandbox.log</file>
        <append>true</append>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="debug">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```
We can configure the file path that the logs are written to, I am writing to `<file>/var/tmp/sandbox.log</file>` and always
append to the end of the file.

Open question as follow, what kind of logger is considered good and bad?

References:
- https://github.com/typesafehub/scala-logging