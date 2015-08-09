package ru.biocad.game

import ru.biocad.solver.{Scorer, DecisionTree, TreeSolver}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 21:26
 */
class GamePlayer(scorer : Scorer) {
  var game : Game = null.asInstanceOf[Game]
  var state : GameState = null.asInstanceOf[GameState]

  var lastSolver : TreeSolver = null.asInstanceOf[TreeSolver]
  var lastTree : DecisionTree = null.asInstanceOf[DecisionTree]

  var lastGameId : Int = 0
  var lastSeed : Int = 0

  var solution = ""

  def releaseSolver() : Unit = {
    lastSolver = null
    lastTree = null
  }

  def startNewGame() : Option[GameState] =
    startNewGame(lastGameId, lastSeed)

  def startNewGame(gameId : Int, seed : Int) : Option[GameState] = {
    solution = ""
    lastSolver = null
    lastTree = null
    lastGameId = gameId
    lastSeed = seed
    val (gm, s) = Game.loadGame(gameId, seed)
    game = gm
    state = s
    Some(state)
  }

  def moveItPlease() : Option[GameState] = {
    if (lastSolver == null) {
      lastSolver = new TreeSolver(game, scorer)
    }
    if (lastTree == null) {
      lastSolver.getTree(state, 5) match {
        case Some(tree) =>
          lastTree = tree
        case None =>
          // println("Shit happend!")
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
        // println("End of game")
        None
    }
  }

  def moveItMoveIt(move : Move) : Option[GameState] = {
    // println(s"Update: ${move.name}")
    updateState(move)(game.movement(state)(move))
  }

  def updateState(move : Move) : Either[GameState, EndState] => Option[GameState] = {
    case Left(gs) =>
      solution += move.symbols.head
      state = gs
      // println(s"Current: $solution")
      Some(state)
    case Right(ge) =>
      // println(s"End (${ge.name}): $solution${move.symbols.head}")
      None
  }
}
