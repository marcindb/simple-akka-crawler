package pl.ekodo.crawler

trait DAO[T] {

  def find(id: String): T

  def save(t: T): Unit

  def remove(id: String): Unit
  
  def clear(): Unit

}