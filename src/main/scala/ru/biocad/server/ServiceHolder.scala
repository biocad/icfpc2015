package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game._
import ru.biocad.util.Parser
import spray.can.Http

import scala.concurrent.duration._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder {
  var currentGame = "6"
  var currentSeed = 0

//  val optimizer = new MoveOptimizer(Source.fromFile("power_phrases.txt").getLines())
  val problems = loadProblems

  var (game, state) = Game.loadGame(currentGame, currentSeed)

  // System
  implicit val system = ActorSystem("honeycomb")
  val service = system.actorOf(Props(classOf[RESTactor], problems, newGame, update, getSolution), "stateful")
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
      val (gm, s) = Game.loadGame(currentGame, currentSeed)
      game = gm
//      val recommendation = optimizer.startingPowers(solution)
//      state = s.copy(recommendation = recommendation)
      state = s
      Some(state)
    }
    else {
      println(s"Update: $move")
      game.movement(state)(Move(move)) match {
        case Some(gs) =>
          solution += move
//          val recommendation = optimizer.startingPowers(solution)
//          state = gs.copy(recommendation = recommendation)
          state = gs
          println(s"Current: $solution")
          Some(state)
        case None =>
          println(s"End: $solution$move")
//          optimizer.optimize(solution + move)
          None
      }
    }
  }

  def newGame : String => Option[GameState] = game_sid => {
    solution = ""
    game_sid.split('_') match {
      case Array(g, sid) =>
        currentGame = g
        currentSeed = sid.toInt
        update('Ы')
      case _ =>
        None
    }
  }

  def loadProblems : Map[String, Vector[Int]] = {
    val res = (0 to 24).map {
      case i =>
        val rawProblem = scala.io.Source.fromFile(s"problems/problem_$i.json").mkString
        val parsedProblem = Parser.parseProblem(rawProblem)
        s"$i" -> parsedProblem.sourceSeeds
    }.toMap
    res
  }
}
