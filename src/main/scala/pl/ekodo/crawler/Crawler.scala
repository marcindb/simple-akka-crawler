package pl.ekodo.crawler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.concurrent.duration.FiniteDuration
import scala.util.Failure
import scala.util.Success

import akka.actor.ActorSystem
import akka.util.Timeout
import spray.client.pipelining.Get
import spray.client.pipelining.sendReceive
import spray.http.HttpRequest
import spray.http.HttpResponse

case class Request(val uri: String)
case class Response(val uri: String, val body: String)

trait ActorSystemWrapper {
  implicit val system: ActorSystem
  implicit val timeout: Timeout
}

trait Crawler[T] {
  this: ObjectBuilder[T] with ActorSystemWrapper =>

  def dao: DAO[T]

  def get(request: Request) = getResponse(request)

  def get(request: Request, frequency: FiniteDuration) = {
    system.scheduler.schedule(0 milliseconds, frequency)(getResponse(request))
  }

  import system.dispatcher // execution context for futures
  protected def pipeline(): HttpRequest => Future[HttpResponse] = sendReceive

  private def getResponse(request: Request) = {
    val response: Future[HttpResponse] = pipeline()(Get(request.uri))
    response onComplete {
      case Success(t) =>
        dao.clear()
        val objects = build(Response(request.uri, t.entity.asString))
        objects.foreach(dao.save(_))
      case Failure(t) => throw new IllegalStateException(t.getMessage())
    }
  }
}

trait ObjectBuilder[T] {
  def build(response: Response): List[T]
}