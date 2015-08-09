package ru.biocad.game

object GameScorer {

  def getScore(previousGameState: GameState, nextGameState: GameState): (Long, Int) = {

    val currentFilled = previousGameState.boardState.filled.groupBy(c => c.r)
    val nextFilled = nextGameState.boardState.filled.groupBy(c => c.r)

    val currentFilledSize = currentFilled.size
    val nextFilledSize = nextFilled.size

    val ls_old = previousGameState.lastAction.linesCleared
    val ls = math.max(currentFilledSize - nextFilledSize, 0)

//    println(s"Lines cleared: ${ls_old} and going to: ${ls}")

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
