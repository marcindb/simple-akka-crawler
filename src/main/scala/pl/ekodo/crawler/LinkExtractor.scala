package pl.ekodo.crawler

import org.jsoup.Jsoup
import scala.collection.mutable.ArrayBuffer

case class Link(uri: String)

trait LinkExtractor {
  def extract(text: String): Set[Link]
}

object DefaultLinkExtractor extends LinkExtractor {
  def extract(text: String): Set[Link] = {
    val doc = Jsoup.parse(text)
    val links = doc.select("a[href]").iterator()
    val result = new ArrayBuffer[Link]
    while (links.hasNext) {
      val link =links next ()
      result += Link(link.attr("href"))
    }
    result.toSet
  }
}