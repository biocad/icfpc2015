package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game.{BoardState, Game, GameState}
import ru.biocad.util.Parser
import spray.can.Http

import scala.concurrent.duration._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder {
  var (game, state) = loadGame("0", 0)

  // System
  implicit val system = ActorSystem("honeycomb")
  val service = system.actorOf(Props(classOf[RESTactor], newGame, update, getSolution), "stateful")
  var solution = ""

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 5000)

  io.StdIn.readLine()
  system.shutdown()
  system.awaitTermination()


  def getSolution : Unit => String = _ => solution

  def update : Char => Option[GameState] = move => {
    if(move == 'Ы') {
      println("Ы")
      solution = ""
      val (gm, s) = loadGame("0", 0)
      game = gm
      state = s
      Some(state)
    }
    else {
      println(s"Update: $move")
      game.movement(state)(move) match {
        case Some(gs) =>
          println(gs)
          solution += move
          state = gs
          Some(state)
        case None =>
          println(s"End: $solution")
          None
      }
    }
  }

  def newGame : String => Option[GameState] = game_sid => {
    game_sid.split('_') match {
      case Array(g, sid) =>
        val (gm, s) = loadGame(g, sid.toInt)
        game = gm
        state = s
        solution = ""
        Some(state)
      case _ =>
        solution = ""
        None
    }
  }

  def loadGame(gid : String, sid : Int) : (Game, GameState) = {
    val rawProblem = scala.io.Source.fromFile(s"problems/problem_$gid.json").mkString
    val parsedProblem = Parser.parseProblem(rawProblem)
    val (board, filled, beezArray) = parsedProblem.getGameRules

    val beez = beezArray(sid)
    println(s"Game $gid ($sid) loaded")
    (new Game(board), GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez, currentBee = 0))
  }
}
