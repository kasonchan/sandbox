# Processes

@since Dec-2016

Process provide ways to run command line process. It does support multiple command on remote server but not locally.
We need to import `import scala.sys.process._` this in order to use processes. 

- `run`: the most general method, it returns a scala.sys.process.Process immediately, and the external command executes concurrently.
- `!`: blocks until all external commands exit, and returns the exit code of the last one in the chain of execution.
- `!!`: blocks until all external commands exit, and returns a String with the output generated.

`ProcessLogger` is used to log the process actions. In this example, I set it to print out the input.
`Process` has a option to set the directory to be run on: `Process(command, new java.io.File(currentDirectory))`

`trim()` returns a copy of the string, with leading and trailing whitespace omitted.

References:
- http://www.scala-lang.org/api/2.12.x/scala/sys/process/ProcessBuilder.html
- https://www.garysieling.com/scaladoc/scala.sys.process/2016/02/15/scala__sys_process.html
- https://www.tutorialspoint.com/scala/scala_strings.htm
