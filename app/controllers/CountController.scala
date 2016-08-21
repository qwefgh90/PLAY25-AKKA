package controllers

import play.api.mvc._
import akka.actor._
import javax.inject._
import actor._
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.db._
import akka.util.Timeout
import scala.concurrent._
import akka.pattern.ask
import scala.concurrent.duration._


import services.Counter

/**
 * This controller demonstrates how to use dependency injection to
 * bind a component into a controller class. The class creates an
 * `Action` that shows an incrementing count to users. The [[Counter]]
 * object is injected by the Guice dependency injection system.
 */
@Singleton
class CountController @Inject() (counter: Counter, system: ActorSystem) extends Controller {
  val pdfActor = system.actorSelection("akka://application/user/pdfActor")
  /**
   * Create an action that responds with the [[Counter]]'s current
   * count. The result is plain text. This `Action` is mapped to
   * `GET /count` requests by an entry in the `routes` config file.
   */
  def count = Action { 
    for (i <- 1 to 500)
      pdfActor ! PDFRunner.PDFRequest(i.toString)

    Ok(counter.nextCount().toString) }

  
}
