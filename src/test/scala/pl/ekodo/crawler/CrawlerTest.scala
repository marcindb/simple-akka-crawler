package pl.ekodo.crawler

import org.specs2.mutable.Specification
import akka.actor.ActorSystem
import spray.http.HttpRequest
import scala.concurrent.Future
import spray.http.HttpResponse
import spray.http.HttpResponse
import spray.http.HttpEntity
import scala.concurrent._
import ExecutionContext.Implicits.global

class CrawlerTest extends Specification {

  val dao = new MockDao
  val testCrawler = new TestCrawler(ActorSystem("test"), dao)
  val id = "test"

  "Crawler should" should {
    "load data" in {
      testCrawler.get(Request("http://ekodo.pl"))
      Thread.sleep(1000)
      dao.find("http://ekodo.pl") must not be equalTo(None)
    }
  }

}

class TestCrawler(actorSystem: ActorSystem, mockDao: DAO[MockObject]) extends Crawler[MockObject] with ActorSystemWrapper with MockObjectBuilder {
  import scala.concurrent.duration._
  import akka.util.Timeout
  import system.dispatcher
  implicit val timeout = Timeout(20000)
  implicit val system = actorSystem
  def dao = mockDao
  override protected def pipeline(): HttpRequest => Future[HttpResponse] = mockPipeline
  
  def mockPipeline(request: HttpRequest): Future[HttpResponse] = {
    val entity = HttpEntity("test")
    val response = HttpResponse().withEntity(entity)
    future{response}
  }
}