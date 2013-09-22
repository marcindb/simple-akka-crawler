package pl.ekodo.crawler

import scala.collection.mutable.ArrayBuffer
import org.jsoup.Jsoup
import scala.annotation.tailrec

case class Link(uri: String)

trait LinkExtractor {
  def extract(text: String): Set[Link]
}

object DefaultLinkExtractor extends LinkExtractor {

  override def extract(text: String): Set[Link] = {
    val doc = Jsoup.parse(text)
    val links = doc.select("a[href]").iterator()
    val result = new ArrayBuffer[Link]
    while (links.hasNext) {
      val link = links.next().attr("href");
      result += Link(Utils.truncate(link))
    }
    result.toSet
  }

}

trait LinkEvaluator {
  def process(link: Link): Boolean
}

object LinkEvaluator {
  implicit def listToEvaluator(list: List[LinkEvaluator]): LinkEvaluator =
    new LinkEvaluator {

      override def process(link: Link): Boolean = fold(link, list)

      @tailrec
      private def fold(link: Link, handlers: List[LinkEvaluator]): Boolean = {
        handlers match {
          case head :: tail if !tail.isEmpty =>
            head.process(link) match {
              case false => false
              case true => fold(link, tail)
            }
          case head :: tail if tail.isEmpty => head.process(link)
          case _ => false
        }
      }

    }
}

class SingleDomainEvaluator(val domain: String) extends LinkEvaluator {
  private val _domain = Utils.truncate(domain)
  override def process(link: Link): Boolean = {
    link.uri.startsWith(_domain)
  }
}

class ValidLinkEvaluator extends LinkEvaluator {
  override def process(link: Link): Boolean = {
    !link.uri.startsWith("#")
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
