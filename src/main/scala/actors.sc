import akka.typed.ScalaDSL.{Full, Msg, Same, Sig, Static, Total}
import akka.typed._
import akka.typed.AskPattern._
import com.typesafe.config.ConfigFactory

import scala.concurrent.{Await, Future}

sealed trait CounterMessage
case class GetAndIncrement(sender: ActorRef[Int]) extends CounterMessage
case class GetAndAdd(i: Int, sender: ActorRef[Int]) extends CounterMessage

case class DoWork(i: Int)

type Counter = ActorRef[CounterMessage]
case class GetCounter(sender: ActorRef[Counter])

def worker(): Behavior[DoWork] = Static {
  case DoWork(i) => println(s"work: $i")
}

def counter(current: Int, worker: ActorRef[DoWork]): Behavior[CounterMessage] =
  Total {
    case GetAndIncrement(sender) =>
      val eventualInt: Future[Int] = Future(current)
      eventualInt.foreach(sender ! _)
      worker ! DoWork(current)
      counter(current + 1, worker)
    case GetAndAdd(i, sender) =>
      sender ! current
      worker ! DoWork(current)
      counter(current + i, worker)
  }

def guardian(
              state: Option[ActorRef[CounterMessage]]
            ): Behavior[GetCounter] = Full {
  case Sig(ctx, PreStart) =>
    val workerActor = ctx.spawn(Props(worker()), "worker")
    val counterActor = ctx.spawn(Props(counter(0, workerActor)), "counter")

    guardian(Some(counterActor))
  case Msg(ctx, GetCounter(sender)) =>
    state.foreach(sender ! _)
    Same
}

val as: ActorSystem[GetCounter] =
  ActorSystem(
    "test",
    Props(guardian(None)),
    config = Some(ConfigFactory.parseString("akka.daemonic=on")))

val counterFuture: Future[Counter] = as.?[Counter](GetCounter)

for (counter <- counterFuture) {
  val eventualInt: Future[Int] = counter ? GetAndIncrement
  val eventualInt1: Future[Int] = counter.?[Int]{GetAndAdd(5, _)}
}
