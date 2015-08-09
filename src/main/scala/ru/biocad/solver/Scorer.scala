package ru.biocad.solver

import ru.biocad.game.{Cell, GameState, EndState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:26
 */

case class Weights(
                  ca: Double = 1,     // locked cells around bee
                  cw: Double = -4,     // locked cells around walls
                  cf: Double = 5,     // locked cells around floor
                  uc: Double = -10,     // unreachable cells
                  bl: Double = 100,     // cleared lines
                  bh: Double = -1     // current bee top element distance from bottom
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

    val ca = lockedCellsNearBee.size
    val cw = gameState.boardState.filled.count(cell => cell.r == 0 || cell.r == gameState.boardState.getBoard.width)
    val cf = gameState.boardState.filled.count(cell => cell.q == gameState.boardState.getBoard.height)
    val uc = if (gameState.boardState.filled.isEmpty) 0 else gameState.boardState.numberOfUnreachableBelow(Cell(gameState.boardState.getBoard.width / 2, 0), gameState.boardState.filled.minBy(_.r).r)
    val bl = gameState.lastAction.linesCleared
    val bh = gameState.boardState.getBoard.height - bee.members.map(m => m.r).min

    ca * w.ca + cw * w.cw + cf * w.cf + uc * w.uc + bl * w.bl + bh * w.bh
  }

  protected def scoreOfEnd(endState: EndState) : Double = {
    endState.score
  }
}
