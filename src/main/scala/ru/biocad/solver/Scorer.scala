package ru.biocad.solver

import ru.biocad.game.{Cell, GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */

case class Weights(
                  a: Double = 10,     // locked cells around bee
                  b: Double = -5,     // locked cells around walls
                  c: Double = 10,     // locked cells around floor
                  d: Double = -8,     // unreachable cells
                  e: Double = -4,     // blockING (making other unreachable) cells
                  f: Double = -1,     // current bee top element distance from bottom
                  g: Double = 100     // cleared lines
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
    val uc = if (gameState.boardState.filled.isEmpty) 0 else gameState.boardState.numberOfUnreachableBelow(Cell(gameState.boardState.getBoard.width / 2, 0), gameState.boardState.filled.minBy(_.r).r)
    val bc = 0
    val bl = gameState.lastAction.linesCleared
    val bh = {
      val rmax = gameState.boardState.getBoard.height
      val rmin = bee.members.map(m => m.r).min
      rmax - rmin
    }

    w.a * cb + w.b * cw + w.c * cf + w.d * uc + w.e * bc + w.f * bh + w.g * bl
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
