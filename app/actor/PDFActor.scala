package actor

import akka.actor.{Actor, Props}
import scala.concurrent._
import scala.util._
import java.util.concurrent.Executors
import concurrent.ExecutionContext

object PDFRunner {
  def props = Props[PDFRunner]
  case class PDFRequest(path: String)
}

class PDFRunner extends Actor{
  val numThreads = 100
  val pool = Executors.newFixedThreadPool(numThreads)
  val ctx = ExecutionContext.fromExecutorService(pool)

  def receive = {
    case request: PDFRunner.PDFRequest =>
      println("request: " + request.toString)
      //      Thread.sleep(1000)
      ///*
      val future = Future{
        Thread.sleep(1000)
        println("Future Start: "+request.path)
        request.path
      } (ctx)
      future.onComplete({
        case Success(s) =>
          println("Future Suncess: " + s)
        case Failure(f) =>
          println("Failure")
      }) (ctx)
      //*/

  }
}
