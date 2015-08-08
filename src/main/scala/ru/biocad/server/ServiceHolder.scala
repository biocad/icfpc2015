package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import spray.can.Http
import scala.concurrent.duration._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder {
  // System
  implicit val system = ActorSystem("honeycomb")
  val service = system.actorOf(Props[RESTactor], "stateful")

  implicit val timeout = Timeout(5.second)
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)

  io.StdIn.readLine()
  system.shutdown()
  system.awaitTermination()


  //
  //  private val _game = game
  //  private var _gameState = gameState
  //
  //  def update : Char => Option[GameState] = move => {
  //    _game.movement(_gameState)(move) match {
  //      case Some(gs) =>
  //        _gameState = gs
  //        Some(_gameState)
  //      case None =>
  //        None
  //    }
  //  }
}
