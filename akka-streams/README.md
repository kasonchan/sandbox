# sandbox-akka-streams

- `groupedWithin(n: Int, d: FiniteDuration): Repr[immutable.Seq[Out]]`: 
  Chunk up this stream into groups of elements received within a time window,
  or limited by the given number of elements, whatever happens first.
- `def mapAsync[T](parallelism: Int)(f: Out ⇒ Future[T]): Repr[T]`: 
  Transform this stream by applying the given function to each of the elements 
  as they pass through this processing step.

## Actor as Sink

- `def actorRefWithAck[T](ref: ActorRef, onInitMessage: Any, ackMessage: Any, onCompleteMessage: Any,
                            onFailureMessage: (Throwable) ⇒ Any = Status.Failure): Sink[T, NotUsed]`: 
  Sends the elements of the stream to the given `ActorRef` that sends back back-pressure signal.
  First element is always `onInitMessage`, then stream is waiting for acknowledgement message
  `ackMessage` from the given actor which means that it is ready to process
  elements. It also requires `ackMessage` message after each stream element
  to make backpressure work.
- `def alsoTo(that: Graph[SinkShape[Out], _]): Repr[Out]` attaches the given 
  `Sink` to this `Flow`, meaning that elements that pass through will also be 
  sent to the `Sink`.
  
## Set timeout in Actor

- `def setReceiveTimeout(timeout: Duration): Unit` Defines the inactivity 
  timeout after which the sending of a `akka.actor.ReceiveTimeout` message is 
  triggered. When specified, the receive function should be able to handle a 
  `akka.actor.ReceiveTimeout` message. 1 millisecond is the minimum supported timeout.

## Turn off Stream with KillSwitches

- `KillSwitches`: Creates shared or single kill switches which can be used to 
  control completion of graphs from the outside.

## Create a source queue

- `def queue[T](bufferSize: Int, overflowStrategy: OverflowStrategy): Source[T, SourceQueueWithComplete[T]]` Creates a `Source` that
 is materialized as an `akka.stream.scaladsl.SourceQueue`.
  - The strategy `akka.stream.OverflowStrategy.backpressure` will not complete 
    last `offer():Future` call when buffer is full.
- `def offer(elem: T): Future[QueueOfferResult]` Method offers next element to 
  a stream and returns future that 
  - completes with `Enqueued` if element is consumed by a stream
  - completes with `Dropped` when stream dropped offered element
  - completes with `QueueClosed` when stream is completed during future is active
  - completes with `Failure(f)` when failure to enqueue element from upstream
  - fails when stream is completed or you cannot call offer in this moment because of implementation rules
 
## References

- https://doc.akka.io/docs/akka/2.5.5/scala/stream/stages-overview.html
- https://www.youtube.com/watch?v=qaiwalDyayA&index=4&list=PLKKQHTLcxDVayICsjpaPeno6aAPMCCZIz
