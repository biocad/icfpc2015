package ru.biocad.solver

import ru.biocad.game.{GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */

case class Weights(
                  a: Double = 1,
                  b: Double = 1,
                  c: Double = 1,
                  d: Double = 1
                    )

class Scorer(weights : Weights) {
  final def apply(state : Either[GameState, EndState]) : Double = {
    state match {
      case Left(gs) => scoreOfGame(gs)
      case Right(es) => scoreOfEnd(es)
    }
  }

  private [this] def w = weights

  protected def scoreOfGame(gameState: GameState) : Double = {

    val cb = 0
    val cw = 0
    val cf = 0
    val uc = 0
    val bc = 0
    val bh = 0

    val filledFields = gameState.boardState.filled

    val bee = gameState.bee

    val beeX = bee.pivot.q
    val beeY = bee.pivot.r

    val beez = gameState.beez.length

    //gameState.score
    w.a * filledFields.size + w.b * beez + gameState.score
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
