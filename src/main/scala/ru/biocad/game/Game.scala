package ru.biocad.game

import ru.biocad.util.Parser

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:31
 */
class Game(board : Board) {
  def movement(gs : GameState)(direction : Move) : Option[GameState] = {
    val beeNextMove = gs.bee.move(direction)
    val wasLocked = gs.boardState.isLocked(beeNextMove)
    val boardState = if (!wasLocked) gs.boardState else gs.boardState.update(gs.bee)

    if (!wasLocked && gs.previous.contains(beeNextMove.members.toSet)) {
      None // We have already been here
    }
    else if (wasLocked && gs.currentBee == gs.beez.length - 1) {
      None // Ok, we won
    }
    else {
      val (bee, newPrevious, currentBee) =
        if (!wasLocked) {
          (beeNextMove, gs.previous + beeNextMove.members.toSet, gs.currentBee)
        }
        else {
          println("New bee spawned")
          val tmpBee = gs.beez(gs.currentBee + 1)
          (tmpBee, Set(tmpBee.members.toSet), gs.currentBee + 1)
        }

      if (boardState.isLocked(bee)) {
        None
      }
      else {
        val (points, clearedLines) = Scorer.getScore(gs, GameState(boardState, bee, gs.beez, currentBee, newPrevious, 0, HZ))
        val cellsAround = boardState.cellsAround(bee).size
        val lastAction = if (wasLocked) LockedMove(linesCleared = clearedLines, cellsAround) else SimpleMove(cellsAround)
        Some(GameState(boardState, bee, gs.beez, currentBee, newPrevious, points + gs.score, lastAction))
      }
    }
  }
}

object Game {
  def loadGame(currentGame : String, currentSeed : Int) : (Game, GameState) = {
    val rawProblem = scala.io.Source.fromFile(s"problems/problem_$currentGame.json").mkString
    val parsedProblem = Parser.parseProblem(rawProblem)
    val seeds = parsedProblem.sourceSeeds
    val (board, filled, beezArray) = parsedProblem.getGameRules

    val beez = (beezArray zip seeds).find(_._2 == currentSeed) match {
      case Some((bees, _)) => bees
      case None => throw new RuntimeException("Go to hell!")
    }
    println(s"Game $currentGame ($currentSeed) loaded")
    println(s"# of figures: ${beez.length}")
    (new Game(board), GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez,
      currentBee = 0, previous = Set(beez.head.members.toSet), score = 0, lastAction = HZ))
  }
}

case class GameState(boardState : BoardState, bee : Bee, beez : Array[Bee],
                     currentBee : Int, previous : Set[Set[Cell]], score: Long, lastAction : GameAction) {
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
      |  "figures": ${beez.length - currentBee}
      |}
    """.stripMargin
  }
}
