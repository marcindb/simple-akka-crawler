package pl.ekodo.crawler

import akka.actor.ActorSystem
import spray.http.HttpRequest
import scala.concurrent.Future
import spray.http.HttpResponse
import spray.http.HttpResponse
import spray.http.HttpEntity
import scala.concurrent._
import ExecutionContext.Implicits.global
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.WordSpecLike
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.matchers.MustMatchers
import org.scalatest.Matchers
import akka.testkit.TestKit

class CrawlerTest(_system: ActorSystem) extends TestKit(_system) with WordSpecLike with Matchers with BeforeAndAfterAll{

  def this() = this(ActorSystem("test"))
  val dao = new MockDao
  val testCrawler = new TestCrawler(_system, dao)
  val id = "test"

  override def afterAll = {
    TestKit.shutdownActorSystem(_system)
  }  
    
  "Crawler should" must {
    "load data" in {
      testCrawler.get(Request("http://ekodo.pl"))
      Thread.sleep(1000)
      dao.find("http://ekodo.pl") should not be equal(None)
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