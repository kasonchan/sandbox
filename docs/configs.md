# Configs

@since Dec-2016

This is my first sandbox document. Recently I am interested in refactoring some 
of my constant code to a property file. I learnt that I can do the properties file
in Java in my previous project. I also want to learn if there is a way to do similar
thing in Scala. And I found the Typesafe Config Library. 

It is useful to have configuration file because we can configure
without recompiling everything. I can just change the configuration without touching
the code base. This allows the user to setup according to the need.

To use the library in SBT, add the following to the `build.sbt` file:
```
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
```

Default path of `ConfigFactory` is `application.conf`. We can specify the path at
the `load` function like this: `ConfigFactory.load("config.conf")`

It supports files in three formats: Java properties, JSON, and a human-friendly JSON superset.
In my sandbox, I created configs.properties, configs.json and configs.conf respectively.

The library also allows merging config trees: Any two Config objects can be merged
with an associative operation called withFallback, like 
`merged = firstConfig.withFallback(secondConfig)`. I found this is very useful
on dealing with back versioning.

Akka also uses the Typesafe Config Library, which might also be a good choice 
for the configuration of your own application or library built with or without 
Akka. This library is implemented in Java with no external dependencies.

Reference:
- https://danielasfregola.com/2015/06/01/loading-configurations-in-scala/: 
This blog shows the basic usage of configuration file.
- https://github.com/typesafehub/config: This is the github page of the Typesafe Config Library. 
It shows all detail information on configuration settings. 
- http://typesafehub.github.io/config/latest/api/: This page shows the latest API.
- http://doc.akka.io/docs/akka/snapshot/general/configuration.html: This page 
shows the configuration of Akka.
