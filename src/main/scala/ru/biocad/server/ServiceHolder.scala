package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game.{Game, GameState}
import spray.can.Http
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder(game : Game, gameState : GameState) {
  // System
  implicit val system = ActorSystem("honeycomb")

  private val _game = game
  private var _gameState = gameState

  def update : Char => Option[GameState] = move => {
    _game.movement(_gameState)(move) match {
      case Some(gs) =>
        _gameState = gs
        Some(_gameState)
      case None =>
        None
    }
  }

  val service = system.actorOf(Props(classOf[RESTactor], update), "stateful")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)

  io.StdIn.readLine()
  system.shutdown()
  system.awaitTermination()
}
