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


object Parser {

  def parseSoleCommand(c: String): Direction = {
    c match {
      case _ if c == "W" => Direction.West
      case _ if c == "E" => Direction.East
      case _ if c == "SW" => Direction.SouthWest
      case _ if c == "SE" => Direction.SouthEast
      case _ if c == "CW" => Direction.RotateClock
      case _ if c == "CCW" => Direction.RotateCounterClock
      case _ => throw new IllegalArgumentException("NO WAYS TO GO!!! GO HOME AND DIE!!!!")
    }
  }

  def parseString(c: String): Vector[Direction] = {
    c.split(" ").map(parseSoleCommand).toVector
  }

}
