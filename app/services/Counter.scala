package services

import java.util.concurrent.atomic.AtomicInteger
import javax.inject._
import play.api.db._

/**
 * This trait demonstrates how to create a component that is injected
 * into a controller. The trait represents a counter that returns a
 * incremented number each time it is called.
 */
trait Counter {
  def nextCount(): Int
}

/**
 * This class is a concrete implementation of the [[Counter]] trait.
 * It is configured for Guice dependency injection in the [[Module]]
 * class.
 *
 * This class has a `Singleton` annotation because we need to make
 * sure we only use one counter per application. Without this
 * annotation we would get a new instance every time a [[Counter]] is
 * injected.
 */
@Singleton
class AtomicCounter @Inject()(db: Database) extends Counter {  
  private val atomicCounter = new AtomicInteger()
  override def nextCount(): Int = {
	var outString = "Number is "
    val conn = db.getConnection()
    
    try {
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT 9 as testkey ")
      
      while (rs.next()) {
        outString += rs.getString("testkey")
      }
    } finally {
      conn.close()
    }
	Console.println(outString)
    atomicCounter.getAndIncrement()
  }
}
