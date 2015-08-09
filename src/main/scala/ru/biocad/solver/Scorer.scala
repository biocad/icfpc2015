package ru.biocad.solver

import ru.biocad.game.{Cell, GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */

case class Weights(
                  a: Double = -100,     // locked cells
                  b: Double = -100,     // locked near walls
                  c: Double = 1000,     // locked near floor
                  d: Double = -100,     // blocked cells
                  e: Double = -4,       // blockING cells
                  f: Double = 3,        // distance from bottom
                  g: Double = 100       // cleared lines
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
    val uc = 0 //if (gameState.boardState.filled.isEmpty) 0 else gameState.boardState.numberOfUnreachableBelow(Cell(gameState.boardState.getBoard.width / 2, 0), gameState.boardState.filled.minBy(_.r).r)
    val bc = 0
    val bl = gameState.lastAction.linesCleared
    // Can be board height - rmin
    val bh = {
      // val rmax = bee.members.map(m => m.r).max
      val rmax = gameState.boardState.getBoard.height
      val rmin = bee.members.map(m => m.r).min
        gameState.boardState.getBoard.height - (rmax - rmin)
    }

    w.a * cb + w.b * cw + w.c * cf + w.d * uc + w.e * bc + w.f * bh + w.g * bl
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
