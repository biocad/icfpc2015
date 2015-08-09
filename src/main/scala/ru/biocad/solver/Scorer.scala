package ru.biocad.solver

import ru.biocad.game.{GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */

case class Weights(
                  a: Double = 3,
                  b: Double = 2.5,
                  c: Double = 5,
                  d: Double = -8,
                  e: Double = -4,
                  f: Double = -1
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

    val bee = gameState.bee
    val lockedCellsNearBee = gameState.boardState.cellsAround(bee)

    val cb = lockedCellsNearBee.size
    val cw = gameState.boardState.filled.count(cell => cell.r == 0 || cell.r == gameState.boardState.getBoard.width)
    val cf = gameState.boardState.filled.count(cell => cell.q == gameState.boardState.getBoard.height)
    val uc = 0
    val bc = 0

    // Can be board height - rmin
    val bh = {
      // val rmax = bee.members.map(m => m.r).max
      val rmax = gameState.boardState.getBoard.height
      val rmin = bee.members.map(m => m.r).min
      rmax - rmin
    }

    val filledFields = gameState.boardState.filled

    //gameState.score
    // w.a * filledFields.size + w.b * gameState.score
     w.a * cb + w.b * cw + w.c * cf + w.d * uc + w.e * bc + w.f * bh
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
