package ru.biocad.game

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:31
 */
class Game(board : Board) {
  def movement(gs : GameState)(movement : Char) : Option[GameState] = {
    val direction = Move(movement)

    val newBee = gs.bee.move(direction)
    val wasLocked = gs.boardState.isLocked(newBee)
    val boardState = if (!wasLocked) gs.boardState else gs.boardState.update(gs.bee)
    val newPrevious = if (wasLocked) {
      List.empty[Bee]
    }
    else {
      newBee :: gs.previous
    }
    if (wasLocked && gs.currentBee == gs.beez.length - 1) {
      None
    }
    else {
      val (bee, currentBee) = if (!wasLocked) (newBee, gs.currentBee) else (gs.beez(gs.currentBee + 1), gs.currentBee + 1)
      if (boardState.isLocked(bee) || gs.previous.contains(newBee)) {
        None
      }
      else {
        val (points, clearedLines) = Scorer.getScore(gs, GameState(boardState, bee, gs.beez, currentBee, newPrevious, 0, 0))
        Some(GameState(boardState, bee, gs.beez, currentBee, newPrevious, points + gs.score, clearedLines))
      }
    }
  }
}

case class GameState(boardState : BoardState, bee : Bee, beez : Array[Bee],
                     currentBee : Int, previous : List[Bee], score: Long,
                     clearedLines: Long,
                     recommendation : Array[String] = Array.empty[String]) {
  def dumpJson : String = {
    val disabled = boardState.filled.map {
      case cell =>
        s"""
          |{
          |  "posX": ${cell.q},
          |  "posY": ${cell.r},
          |  "state": "disabled"
          |}
        """.stripMargin
    }.mkString(",\n")
    val active = bee.members.map {
      case cell =>
        s"""
           |{
           |  "posX": ${cell.q},
           |  "posY": ${cell.r},
           |  "state": "active"
           |}
        """.stripMargin
    }.mkString(",\n")
    val pivot = if (bee.members.exists(c => c.r == bee.pivot.r && c.q == bee.pivot.q)) {
      s"""
         |{
         |  "posX": ${bee.pivot.q},
         |  "posY": ${bee.pivot.r},
         |  "state": "pivot"
         |}
       """.stripMargin
    }
    else {
      s"""
         |{
         |  "posX": ${bee.pivot.q},
         |  "posY": ${bee.pivot.r},
         |  "state": "pivot_out"
         |}
       """.stripMargin
    }


    s"""
      |{
      |  "height": ${boardState.getBoard.height},
      |  "width":  ${boardState.getBoard.width},
      |  "colored": [
      |    ${List(disabled, active, pivot).filter(_.nonEmpty).mkString(",\n")}
      |  ],
      |  "score": $score,
      |  "figures": ${beez.length - currentBee},
      |  "recommendation": [${recommendation.map(r => s""""$r"""").mkString(", ")}]
      |}
    """.stripMargin
  }
}
