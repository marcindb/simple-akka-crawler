package pl.ekodo.crawler

import org.specs2.mutable.Specification
import akka.actor.ActorSystem

class CrawlerTest extends Specification {

  val dao = new MockDao
  val testCrawler = new TestCrawler(ActorSystem("test"), dao)

  "MyService" should {
    "return a greeting for GET requests to the root path" in {
      testCrawler.get(Request("http://www.oktawave.com/pricing.html"))
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
}