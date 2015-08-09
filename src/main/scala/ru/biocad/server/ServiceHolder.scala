package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import ru.biocad.game._
import ru.biocad.solver.{DecisionTree, TreeSolver}
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

  var lastSolver : TreeSolver = null.asInstanceOf[TreeSolver]
  var lastTree : DecisionTree = null.asInstanceOf[DecisionTree]

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
      startNewGame()
    }
    else if (move == 's') {
      moveItPlease()
    }
    else {
      lastSolver = null
      lastTree = null
      moveItMoveIt(Move(move))
    }
  }

  def startNewGame() : Option[GameState] = {
    solution = ""
    lastSolver = null
    lastTree = null
    val (gm, s) = Game.loadGame(currentGame, currentSeed)
    game = gm
    state = s
    Some(state)
  }

  def moveItPlease() : Option[GameState] = {
    if (lastSolver == null) {
      lastSolver = new TreeSolver(game)
    }
    if (lastTree == null) {
      lastSolver.getTree(state, 4) match {
        case Some(tree) =>
          lastTree = tree
        case None =>
          println("Shit happend!")
          return None
      }
    }

    lastSolver.makeDecision(lastTree, 1) match {
      case Some((move, newTree)) =>
        lastTree = newTree
        newTree.state match {
          case gs : GameState =>
            updateState(move)(Left(gs))
          case es : EndState =>
            updateState(move)(Right(es))
        }
      case None =>
        println("End of game")
        None
    }
  }

  def moveItMoveIt(move : Move) : Option[GameState] = {
    println(s"Update: ${move.name}")
    updateState(move)(game.movement(state)(move))
  }

  def updateState(move : Move) : Either[GameState, EndState] => Option[GameState] = {
    case Left(gs) =>
      solution += move.symbols.head
      state = gs
      println(s"Current: $solution")
      Some(state)
    case Right(ge) =>
      println(s"End (${ge.name}): $solution${move.symbols.head}")
      None
  }

  def newGame : String => Option[GameState] = game_sid => {
    solution = ""
    game_sid.split('_') match {
      case Array(g, sid) =>
        currentGame = g
        currentSeed = sid.toInt
        startNewGame()
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
