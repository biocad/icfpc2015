package ru.biocad.solver

import ru.biocad.game.{Game, GameState, Move}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 12:14
 */
class TreeSolver(game : Game) {
  def playGame(initial : GameState, depth : Int, depthStep : Int) : String =
    playTree(getTree(initial, depth), depthStep)

  def playTree(initial : DecisionTree, depthStep : Int) : String =
    makeDecision(initial, depthStep) match {
      case Some((move, tree)) =>
        move.symbols.head + playTree(tree, depthStep)
      case None =>
        ""
    }

  def getTree(state : GameState, depth : Int) : DecisionTree = {
    if (depth == 0) {
      DecisionTree(gameState = state, variants = Map.empty[Move, DecisionTree])
    }
    else {
      DecisionTree(gameState = state, variants =
        Move.all.flatMap {
          case move =>
            game.movement(state)(move).map(gs => move -> getTree(gs, depth - 1))
        }.toMap
      )
    }
  }

  def makeDecision(tree : DecisionTree, depthStep : Int) : Option[(Move, DecisionTree)] = {
    if (tree.variants.isEmpty) {
      None
    }
    else {
      Some(tree.variants.map { case (move, child) => (move, scoreOfMove(child)) }.maxBy(_._2) match {
        case (move, _) => (move, updateTree(tree.variants(move), depthStep))
      })
    }
  }

  def updateTree(tree : DecisionTree, depthStep : Int) : DecisionTree = {
    if (tree.variants.isEmpty) {
      getTree(tree.gameState, depthStep)
    }
    else {
      DecisionTree(tree.gameState, variants = tree.variants.map {
        case (move, child) =>
          move -> updateTree(child, depthStep)
      })
    }
  }

  def scoreOfMove(tree : DecisionTree) : Int =
    if (tree.variants.isEmpty) {
      tree.gameState.lastAction.score
    }
    else {
      tree.gameState.lastAction.score + tree.variants.map { case (move, child) => scoreOfMove(child) }.max
    }
}
