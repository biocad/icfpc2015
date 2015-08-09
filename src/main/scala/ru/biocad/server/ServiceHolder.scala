package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game._
import ru.biocad.solver.{Scorer, DecisionTree, TreeSolver}
import ru.biocad.util.Parser
import spray.can.Http

import scala.concurrent.duration._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:20
 */
class ServiceHolder {
  val problems = loadProblems

  val gamePlayer = new GamePlayer(new Scorer(List.empty[Double]))

  // System
  implicit val system = ActorSystem("honeycomb")
  val service = system.actorOf(Props(classOf[RESTactor], problems, gameFromString, update, getSolution), "stateful")
  var solution = ""

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 5000)

  io.StdIn.readLine()
  system.shutdown()
  system.awaitTermination()


  def getSolution : Unit => String = _ => solution

  def update : Char => Option[GameState] = move => {
    if(move == 'Ы') {
      gamePlayer.startNewGame()
    }
    else if (move == 's') {
      gamePlayer.moveItPlease()
    }
    else {
      gamePlayer.releaseSolver()
      gamePlayer.moveItMoveIt(Move(move))
    }
  }

  def gameFromString : String => Option[GameState] = game_seed =>
    game_seed.split('_') match {
      case Array(gameId, seed) =>
        gamePlayer.startNewGame(gameId.toInt, seed.toInt)
      case _ =>
        None
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
