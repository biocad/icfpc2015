package ru.biocad.solver

import ru.biocad.game.{GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */
class Scorer(weights : List[Double]) {
  def apply(state : Either[GameState, EndState]) : Double = {
    state match {
      case Left(gs) => gs.score
      case Right(es) => es.score
    }
  }
}
