package ru.biocad.game

import argonaut.Argonaut._
import argonaut.CodecJson

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:17
 */
case class Board(id: Int, width: Int, height: Int) {

  def inBoard(cell: Cell): Boolean =
    cell.q >= 0 && cell.q < width && cell.r >= 0 && cell.r < height

  def cells: Vector[Cell] = {
    val r = for {
      x <- 0 to width
      y <- 0 to height
    } yield new Cell(x, y)

    r.toVector
  }
}


object Board {
  implicit def BoardCodecJson: CodecJson[Board] =
    casecodec3(Board.apply, Board.unapply)("id", "width", "height")
}


case class BoardState(filled: Vector[Cell])(board: Board) {
  private def intersects(cell: Cell): Boolean =
    filled.contains(cell)

  def getBoard : Board = board

  def isLocked(bee : Bee) : Boolean =
    if (bee.members.forall(a => board.inBoard(a) && !intersects(a))) {
      false
    }
    else true

  def update(bee: Bee): BoardState =
    BoardState(filled = newField(bee))(board = board)

  private def newField(bee: Bee): Vector[Cell] = {
    (filled ++ bee.members).distinct.groupBy(_.r).flatMap {
      case (_, cells) =>
        Option(cells).filter(_.size < board.width)
    }.flatten.toVector
  }

  def dump: String =
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