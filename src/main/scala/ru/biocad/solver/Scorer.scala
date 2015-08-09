package ru.biocad.solver

import ru.biocad.game.{GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */
class Scorer(weights : List[Double]) {
  final def apply(state : Either[GameState, EndState]) : Double = {
    state match {
      case Left(gs) => scoreOfGame(gs)
      case Right(es) => scoreOfEnd(es)
    }
  }

  protected def scoreOfGame(gameState: GameState) : Double = {
    gameState.score
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
