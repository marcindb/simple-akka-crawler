package pl.ekodo.crawler

import com.typesafe.scalalogging.slf4j.Logging

case class MockObject(id: String, body: String)

class MockDao extends DAO[MockObject] with Logging {

  private var map = Map[String, MockObject]()

  def find(id: String): Option[MockObject] = map.get(id)

  def save(t: MockObject) = {
    map = map + (t.id -> t)
  }

  def remove(id: String) = { map = map - id }

  def clear(): Unit = { map = Map[String, MockObject]() }
  
}

trait MockObjectBuilder extends ObjectBuilder[MockObject] {
  def build(response: Response): MockObject = { MockObject(response.uri, response.body) }
}