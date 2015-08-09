package ru.biocad.solver

import ru.biocad.game.{Move, GameState, Game}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 12:14
 */
class TreeSolver(game : Game) {
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
}
