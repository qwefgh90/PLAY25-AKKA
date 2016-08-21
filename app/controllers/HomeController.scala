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

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(db: Database, system: ActorSystem) extends Controller {
  implicit val timeout = Timeout(5 seconds)
  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  val pdfActor = system.actorOf(PDFRunner.props, "pdfActor")

  /*def index = Action {
   Ok(views.html.index("Your new application is ready."))
   }*/
  def index = Logging{ LoggingAction {
    println(pdfActor.path.toString)
//    for (i <- 1 to 500)
//      pdfActor ! PDFRunner.PDFRequest(i.toString)
    println("tell() is the end")
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
    println("exit()")
    Ok(views.html.index(outString))

  }}
  
  def test1 = Action{
    implicit request => Ok(request.headers.toString + "\n" + request.body)
  }

  def test2 = Action{
    Redirect("/")
  }

  object LoggingAction extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A] => Future[Result])) = {
      Logger.info(request.toString + ": Calling Action")
      block(request)
    }

  }

}
case class Logging[A](action: Action[A]) extends Action[A] {

  def apply(request: Request[A]): Future[Result] = {
    Logger.info("Resuable: ")
    action(request)
  }

  lazy val parser = action.parser
}
