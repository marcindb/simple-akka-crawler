package pl.ekodo.crawler

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LinkExtractorTest extends Specification {

  "Link extractor" should {
    "extract 0 links" in {
      val links = DefaultLinkExtractor.extract("")
      links.size must equalTo(0)
    }
    "extract 1 link" in {
      val links = DefaultLinkExtractor.extract("<html><body><a href='test'></a></body></html>")
      links.size must equalTo(1)
    }

    "extract 1 link" in {
      val links = DefaultLinkExtractor.extract("<html><body><a href='test'></a><a href='test'></a><a href='test'></a></body></html>")
      links.size must equalTo(1)
    }
  }

}