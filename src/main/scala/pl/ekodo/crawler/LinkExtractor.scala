package pl.ekodo.crawler

import org.jsoup.Jsoup
import scala.collection.mutable.ArrayBuffer

case class Link(uri: String)

trait LinkExtractor {
  def extract(text: String): Set[Link]
  def processLink(link: String): Boolean
}

object DefaultLinkExtractor extends LinkExtractor {

  override def extract(text: String): Set[Link] = {
    val doc = Jsoup.parse(text)
    val links = doc.select("a[href]").iterator()
    val result = new ArrayBuffer[Link]
    while (links.hasNext) {
      val link = links.next().attr("href");
      if (processLink(link))
        result += Link(Utils.truncate(link))
    }
    result.toSet
  }

  override def processLink(link: String): Boolean = {
    !link.startsWith("#")
  }

}

trait LinkEvaluator {
  def process(link: Link): Boolean
}

class SingleDomainEvaluator(val domain: String) extends LinkEvaluator {

  private val _domain = Utils.truncate(domain)

  override def process(link: Link): Boolean = {
    link.uri.startsWith(_domain)
  }

}

private object Utils {

  def truncate(link: String) = {
    val trimmed = link.trim()
    val withoutHttp = if (trimmed.startsWith("http://")) trimmed.substring(7) else trimmed
    val withoutWWW = if (withoutHttp.startsWith("www.")) withoutHttp.substring(4) else withoutHttp
    val withoutEndingSlash = if (withoutWWW.endsWith("/")) withoutWWW.substring(0, withoutWWW.length() - 1) else withoutWWW
    withoutEndingSlash
  }

}
