package ru.biocad.game

import ru.biocad.game.Direction.Direction

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:17
 */
class Board(id : Int)(val width : Int, val height : Int) {
  def inBoard(cell : Cell) : Boolean =
    cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height

  def cells: Vector[Cell] = {
    val r = for {
      x <- 0 to width
      y <- 0 to height
    } yield new Cell(x, y)

    r.toVector
  }
}

case class BoardState(filled : Vector[Cell])(board : Board) {
  private def intersects(cell : Cell) : Boolean =
    filled.contains(cell)

  def update(bee : Bee) : BoardState =
    if (bee.members.forall(a => board.inBoard(a) && !intersects(a))) {
      this
    }
    else {
      BoardState(filled = newField(bee))(board = board)
    }

  private def newField(bee : Bee) : Vector[Cell] = {
    (filled ++ bee.members).distinct.groupBy(_.y).flatMap {
      case (_, cells) =>
        Option(cells).filter(_.size < board.width)
    }.flatten.toVector
  }

  def initNewBee(bee: Bee): BoardState = {
    this.update(bee)
  }

  def getNextState(bee: Bee, command: Direction): BoardState = {
    this.update(bee.move(command))
  }

  def dump : String =
    s"""
      |{
      |  "width": ${board.width},
      |  "height": ${board.height},
      |  "colored": [
      |    ${filled.map(_.dumps).mkString(", ")}
      |  ]
      |}
    """.stripMargin
}