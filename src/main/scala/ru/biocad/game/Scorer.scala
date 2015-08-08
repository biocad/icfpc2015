package ru.biocad.game

object Scorer {

  def getScore(currentGameState: GameState, nextGameState: GameState): Long = {

    val size = currentGameState.bee.members.size
    val currentFilled = currentGameState.boardState.filled.groupBy(c => c.r)
    val nextFilled = nextGameState.boardState.filled.groupBy(c => c.r)

    val currentFilledSize = currentFilled.size
    val nextFilledSize = nextFilled.size

    val ls = math.max(currentFilledSize - nextFilledSize, 0)

    val points = size + 100 * (1 + ls) * ls / 2

    val ls_old = ls

    val lineBonus = if (ls_old > 1) math.floor((ls_old - 1) * points / 10).toLong else 0

    points + lineBonus
  }

}
