package ru.biocad.game

import ru.biocad.game.Direction.Direction


object Solver {

  def parseRun(c: String, bee: Bee, board: Board): Vector[BoardState] = {
    val commands = Parser.parseString(c)

    val initialState: BoardState = new BoardState(Vector())(board) initNewBee bee
    val nextStates: Vector[BoardState] = commands.map(c => initialState.getNextState(bee, c))

    initialState +: nextStates
  }

  def dump(states: Vector[BoardState]): String = {
    states.foldLeft("")((acc, bs) => acc + bs.dump + "\n")
  }

  def isActive(c: Cell, b: BoardState): String = {
    if (b.filled.contains(c)) "active"
    else "disabled"
  }

  def dumpStateForFront(state: BoardState, board: Board): String = {

    val res: Vector[String] = for {
      cell <- board.cells.map(c => (c, isActive(c, state)))
    } yield {
      s"""
         |{
         |"posX": ${cell._1.x}
         |"posY": ${cell._1.y}
         |"state" ${cell._2}
         |}
       """.stripMargin
    }

    res.foldLeft("")((acc, r) => acc + r + "\n")

  }

}
