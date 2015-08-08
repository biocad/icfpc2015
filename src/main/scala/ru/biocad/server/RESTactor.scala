package ru.biocad.server

import akka.actor.Actor
import ru.biocad.game.GameState
import spray.routing._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:24
 */
class RESTactor(update : Char => Option[GameState]) extends Actor with HttpService {
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
    }

  def magic : Option[GameState] => String = {
    case Some(st) => st.dumpJson
    case None => "{\"end\": true}"
  }
}