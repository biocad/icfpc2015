package ru.biocad.server

import akka.actor.Actor
import ru.biocad.game.GameState
import spray.routing._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:24
 */
class RESTactor(newGame : String => Option[GameState],
                update : Char => Option[GameState],
                getSolution : Unit => String) extends Actor with HttpService {
  override def actorRefFactory = context

  override def receive = runRoute(myRoute)

  val myRoute =
    path("field") {
      get {
        complete {
          magic(update('Ы'))
        }
      }
    } ~
    path("move" / Segment) {
      move =>
        get {
          complete {
            magic(update(move.head))
          }
        }
    } ~
  path("game" / Segment) {
    game_seed =>
      get {
        complete {
          magic(newGame(game_seed))
        }
      }
  }

  def magic : Option[GameState] => String = {
    case Some(st) => st.dumpJson
    case None => s"""{"end": true, "solution": "${getSolution()}"}"""
  }
}