package pl.ekodo.crawler

import org.scalatest.Matchers
import org.scalatest.WordSpecLike

class LinkExtractorTest extends WordSpecLike with Matchers {

  "Link extractor" should {

    "extract 0 links" in {
      val links = DefaultLinkExtractor.extract("")
      links.size should equal(0)
    }
    "extract 1 link" in {
      val links = DefaultLinkExtractor.extract("<html><body><a href='test'></a></body></html>")
      links.size should equal(1)
    }

    "extract 1 link when there are 3 the same links" in {
      val links = DefaultLinkExtractor.extract("<html><body><a href='test'></a><a href='test'></a><a href='test'></a></body></html>")
      links.size should equal(1)
    }

    "not extract link when it starts with #" in {
      val links = DefaultLinkExtractor.extract("<html><body><a href='#test'></a></body></html>")
      links.size should equal(0)
    }

  }

  "Single page evaluator" should {

    "return true if link is from the same domain" in {
      val le = new SingleDomainEvaluator("www.google.com")
      val result = le process Link("google.com/trends/")
      result should equal(true)
    }

    "return false if link is from different domain" in {
      val le = new SingleDomainEvaluator("www.google.com/")
      val result = le process Link("www.bing.com")
      result should equal(false)
    }

  }

}