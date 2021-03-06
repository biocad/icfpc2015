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

  def cellsAround(bee : Bee) : Iterable[Cell] =
    filled.filter {
      case cell =>
        bee.members.exists(_.isNeighbor(cell))
    }

  def numberOfUnreachableBelow(from : Cell, row : Int) : Int = {
    (0 until board.width).map {
      case q =>
        (row + 1 until board.height).map {
          case r =>
            val cell = Cell(q, r)
            if (!filled.contains(cell) && !isReachable(cell, from)) 1 else 0
        }.sum
    }.sum
  }

  def isReachable(from : Cell, to : Cell, was : Set[Cell] = Set.empty[Cell]) : Boolean = {
    if (!board.inBoard(from) || !board.inBoard(to) || filled.contains(from)) {
      false
    }
    else if (from == to) {
      true
    }
    else {
      from.neighbors.find {
        case cell =>
          if (!was.contains(cell)) {
            isReachable(cell, to, was + cell)
          }
          else {
            false
          }
      } match {
        case Some(_) => true
        case None => false
      }
    }
  }

  def update(bee: Bee): BoardState =
    BoardState(filled = newField(bee))(board = board)

  private def newField(bee: Bee): Vector[Cell] = {
    var rows = List.empty[Int]
    val preResult = (filled ++ bee.members).distinct.groupBy(_.r).flatMap {
      case (i, cells) =>
        Option(cells).filter(_.size < board.width) match {
          case Some(c) =>
            Some(c)
          case None =>
            rows = i :: rows
            None
        }
    }.flatten.toVector

    rows.sortBy(-_).foldRight(preResult) {
      case (row, pred) =>
        pred.map(cell => if (cell.r < row) cell.copy(r = cell.r + 1) else cell)
    }
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