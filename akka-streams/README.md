# sandbox-akka-streams

- `groupedWithin(n: Int, d: FiniteDuration): Repr[immutable.Seq[Out]]`: 
  Chunk up this stream into groups of elements received within a time window,
  or limited by the given number of elements, whatever happens first
- `def mapAsync[T](parallelism: Int)(f: Out ⇒ Future[T]): Repr[T]`: 
  Transform this stream by applying the given function to each of the elements 
  as they pass through this processing step
