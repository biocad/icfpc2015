package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game._
import ru.biocad.solver.TreeSolver
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
  var solverSolution : String = ""

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
      updateStrategy()
      game = gm
//      val recommendation = optimizer.startingPowers(solution)
//      state = s.copy(recommendation = recommendation)
      state = s
      Some(state)
    }
    else if (move == 's') {
      game.movement(state)(Move(solverSolution.head)) match {
        case Left(gs) =>
          solution += solverSolution.head
          solverSolution = solverSolution.tail
          state = gs
          println(s"Current: $solution")
          println(s"Strategy next: $solverSolution")
          Some(state)
        case Right(ge) =>
          println(s"End (${ge.name}): $solution$move")
          None
      }
    }
    else {
      println(s"Update: $move")
      game.movement(state)(Move(move)) match {
        case Left(gs) =>
          solution += move
          state = gs
          println(s"Current: $solution")
          Some(state)
        case Right(ge) =>
          println(s"End (${ge.name}): $solution$move")
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

  def updateStrategy() : Unit = {
    println("Starting calculations")
    solverSolution = new TreeSolver(game).playGame(state, 6, 1)
    println(s"My new strategy: $solverSolution")
    println("Strategy created")
  }
}
