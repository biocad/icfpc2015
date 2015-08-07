package ru.biocad.game

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:31
 */
class Game(board : Board) {
  def movement(gs : GameState)(movement : Char) : Option[GameState] = {
    val direction = Control(movement)

    val (boardState, wasLocked) = gs.boardState.update(gs.bee.move(direction))
    if (wasLocked && gs.currentBee == gs.beez.length - 1) {
      None
    }
    else {
      val (bee, currentBee) = if (!wasLocked) (gs.bee, gs.currentBee) else (gs.beez(gs.currentBee + 1), gs.currentBee + 1)
      Some(GameState(boardState, bee, gs.beez, currentBee))
    }
  }
}

case class GameState(boardState : BoardState, bee : Bee, beez : Array[Bee], currentBee : Int)
