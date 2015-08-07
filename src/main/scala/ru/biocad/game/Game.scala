package ru.biocad.game

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:31
 */
class Game(board : Board) {
  def movement(gs : GameState)(movement : Char) : Option[GameState] = {
    val direction = Control.apply(movement)

    val newBee = gs.bee.move(direction)
    val wasLocked = gs.boardState.isLocked(newBee)
    val boardState = if (!wasLocked) gs.boardState else gs.boardState.update(gs.bee)
    if (wasLocked && gs.currentBee == gs.beez.length - 1) {
      None
    }
    else {
      val (bee, currentBee) = if (!wasLocked) (newBee, gs.currentBee) else (gs.beez(gs.currentBee + 1), gs.currentBee + 1)
      Some(GameState(boardState, bee, gs.beez, currentBee))
    }
  }
}

case class GameState(boardState : BoardState, bee : Bee, beez : Array[Bee], currentBee : Int) {
  def dumpJson : String = {
    val disabled = boardState.filled.map {
      case cell =>
        s"""
          |{
          |  "posX": ${cell.x},
          |  "posY": ${cell.y},
          |  "state": "disabled"
          |}
        """.stripMargin
    }.mkString(",\n")
    val active = bee.members.map {
      case cell =>
        s"""
           |{
           |  "posX": ${cell.x},
           |  "posY": ${cell.y},
           |  "state": "active"
           |}
        """.stripMargin
    }.mkString(",\n")

    s"""
      |{
      |  "height": ${boardState.getBoard.height},
      |  "width":  ${boardState.getBoard.width},
      |  "colored": [
      |    ${List(disabled, active).filter(_.nonEmpty).mkString(",\n")}
      |  ],
      |  "score": 100500
      |}
    """.stripMargin
  }
}
