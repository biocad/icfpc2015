package ru.biocad.server

import akka.actor.Actor
import ru.biocad.game.GameState
import spray.routing._
import spray.http.MediaTypes._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:24
 */
class RESTactor(problems : Map[Int, Vector[Int]],
                newGame : String => Option[GameState],
                update : Char => Option[GameState],
                getSolution : Unit => String) extends Actor with HttpService {
  val index = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"index.html")).mkString
  val hexbin = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"hexbin.js")).mkString

  override def actorRefFactory = context

  override def receive = runRoute(myRoute)

  val magicString = "{" +
    problems.map {
      case (i, s) =>
        s""" "$i": [${s.mkString(", ")}] """
    }.mkString(", ") + "}"

  val myRoute =
    path("") {
      respondWithMediaType(`text/html`) {
        get {
          complete {
            index
          }
        }
      }
    } ~
    path("hexbin.js") {
      respondWithMediaType(`application/javascript`) {
        get {
          complete {
            hexbin
          }
        }
      }
    } ~
    path("problems") {
      get {
        complete {
          magicString
        }
      }
    } ~
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