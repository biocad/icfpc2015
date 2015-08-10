package ru.biocad.game

object GameScorer {

  def getScore(previousGameState: GameState, nextGameState: GameState): (Long, Int) = {
    val ls_old = previousGameState.lastAction.linesCleared
    val ls = nextGameState.lastAction.linesCleared

    if (previousGameState.boardState.filled.length != nextGameState.boardState.filled.length) {
      val size = previousGameState.bee.members.size

      val points = size + 100 * (1 + ls) * ls / 2

      val lineBonus = if (ls_old > 1) math.floor((ls_old - 1) * points / 10).toLong else 0

      (points + lineBonus, ls)
    } else {
      (0, ls_old)
    }
  }

}
