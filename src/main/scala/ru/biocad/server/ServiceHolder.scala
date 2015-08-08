package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game.{Game, GameState}
import spray.can.Http

import scala.concurrent.duration._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder(val game : Game, gameState : GameState) {
  var state = gameState

  // System
  implicit val system = ActorSystem("honeycomb")
  val service = system.actorOf(Props(classOf[RESTactor], update), "stateful")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 5000)

  io.StdIn.readLine()
  system.shutdown()
  system.awaitTermination()


  def update : Char => Option[GameState] = move => {
    println(s"Update: $move")
    game.movement(state)(move) match {
      case Some(gs) =>
        println(gs)
        state = gs
        Some(state)
      case None =>
        None
    }
  }
}
