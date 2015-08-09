package ru.biocad.server

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import ru.biocad.game._
import ru.biocad.solver.{Weights, Scorer, DecisionTree, TreeSolver}
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

  val weights = new Weights
  val gamePlayer = new GamePlayer(new Scorer(weights))
  // make a Config with just your special setting
  val confStr = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"application.conf")).mkString
  val myConfig = ConfigFactory.parseString(confStr)
  // load the normal config stack (system props,
  // then application.conf, then reference.conf)
  val regularConfig = ConfigFactory.load()
  // override regular stack with myConfig
  val combined = myConfig.withFallback(regularConfig)
  // put the result in between the overrides
  // (system props) and defaults again
  val complete = ConfigFactory.load(combined)
  // create ActorSystem

  // System
  implicit val system = ActorSystem.create("myname", complete) // instead of ActorSystem("honeycomb")
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
        val rawProblem = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"problems/problem_$i.json")).mkString
        val parsedProblem = Parser.parseProblem(rawProblem)
        s"$i" -> parsedProblem.sourceSeeds
    }.toMap
    res
  }
}
