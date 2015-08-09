package ru.biocad.game

import ru.biocad.util.Parser

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:31
 */
class Game(board : Board) {
  def movement(gs : GameState)(direction : Move) : Either[GameState, EndState] = {
    val beeNextMove = gs.bee.move(direction)
    val wasLocked = gs.boardState.isLocked(beeNextMove)
    val boardState = if (!wasLocked) gs.boardState else gs.boardState.update(gs.bee)

    if (!wasLocked && gs.previous.contains(beeNextMove.members.toSet)) {
      Right(InvalidMove)// We have already been here
    }
    else if (wasLocked && gs.currentBee == gs.beez.length - 1) {
      Right(GameEnd)  // Ok, we won
    }
    else {
      val (bee, newPrevious, currentBee) =
        if (!wasLocked) {
          (beeNextMove, gs.previous + beeNextMove.members.toSet, gs.currentBee)
        }
        else {
          val tmpBee = gs.beez(gs.currentBee + 1)
          (tmpBee, Set(tmpBee.members.toSet), gs.currentBee + 1)
        }

      if (boardState.isLocked(bee)) {
        Right(CannotCreateBee)
      }
      else {
        val (points, clearedLines) = GameScorer.getScore(gs, GameState(boardState, bee, gs.beez, currentBee, newPrevious, 0, HZ))
        val lockedAround = boardState.cellsAround(bee).size
        val lastAction =
          if (wasLocked)
            LockedMove(linesCleared = clearedLines, lockedAround = lockedAround, beeLockScore = bee.beeLockScore, penalty = 5)
          else SimpleMove(lockedAround)
        Left(GameState(boardState, bee, gs.beez, currentBee, newPrevious, points + gs.gameScore, lastAction))
      }
    }
  }
}

object Game {
  def loadGame(currentGame : Int, currentSeed : Int) : (Game, GameState) = {
    val rawProblem = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"problems/problem_$currentGame.json")).mkString
    val parsedProblem = Parser.parseProblem(rawProblem)
    val seeds = parsedProblem.sourceSeeds
    val (board, filled, beezArray) = parsedProblem.getGameRules

    val beez = (beezArray zip seeds).find(_._2 == currentSeed) match {
      case Some((bees, _)) => bees
      case None => throw new RuntimeException("Go to hell!")
    }
    (new Game(board), GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez,
      currentBee = 0, previous = Set(beez.head.members.toSet), gameScore = 0, lastAction = HZ))
  }

  def loadProblems : Map[Int, Vector[Int]] = {
    val res = (0 to 24).map {
      case i =>
        val rawProblem = scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"problems/problem_$i.json")).mkString
        val parsedProblem = Parser.parseProblem(rawProblem)
        i -> parsedProblem.sourceSeeds
    }.toMap
    res
  }
}

case class GameState(boardState : BoardState, bee : Bee, beez : Array[Bee],
                     currentBee : Int, previous : Set[Set[Cell]], gameScore: Long, lastAction : GameAction) extends Scored {
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
      |  "score": $gameScore,
      |  "figures": ${beez.length - currentBee}
      |}
    """.stripMargin
  }

  override def score : Int = lastAction.score
}
