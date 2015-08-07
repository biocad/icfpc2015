package ru.biocad.game

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:17
 */
class Board(id : Int)(val width : Int, val height : Int) {
  def inBoard(cell : Cell) : Boolean =
    cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height
}

case class BoardState(filled : Vector[Cell])(board : Board) {
  def intersects(cell : Cell) : Boolean =
    filled.contains(cell)

  def update(bee : Bee) : BoardState =
    if (bee.members.forall(a => board.inBoard(a) && !intersects(a))) {
      this
    }
    else {
      BoardState(filled = newField(bee))(board = board)
    }

  def newField(bee : Bee) : Vector[Cell] = {
    (filled ++ bee.members).distinct.groupBy(_.y).flatMap {
      case (_, cells) =>
        Option(cells).filter(_.size < board.width)
    }.flatten.toVector
  }

  def dumps : String =
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