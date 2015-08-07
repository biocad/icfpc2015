package ru.biocad.game

import ru.biocad.game.CellsRelation.CellsRelation
import ru.biocad.game.Direction.Direction

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:18
 */
case class Cell(x : Int, y : Int) {
  def move : Direction => Cell ={
    case Direction.West =>
      Cell(x - 1, y)
    case Direction.East =>
      Cell(x + 1, y)
    case Direction.SouthWest =>
      Cell(x - 1, y - 1)
    case Direction.SouthEast =>
      Cell(x + 1, y - 1)
  }

  def rotate(pivot : Cell) : Direction => Cell = {
    case Direction.RotateClock =>
      myRelationFrom(pivot).foldRight(this) {
        case (rel, acc) =>
          acc.goTo(CellsRelation.next(rel))
      }
    case Direction.RotateCounterClock =>
      myRelationFrom(pivot).foldRight(this) {
        case (rel, acc) =>
          acc.goTo(CellsRelation.pred(rel))
      }
  }

  def dumps : String =
    s"""{"x": $x, "y": $y}"""

  def goTo : CellsRelation => Cell = {
    case CellsRelation.W  => Cell(x - 1, y)
    case CellsRelation.NW => Cell(if (y % 2 == 0) x - 1 else x, y - 1)
    case CellsRelation.NE => Cell(if (y % 2 == 0) x else x - 1, y - 1)
    case CellsRelation.E  => Cell(x + 1, y)
    case CellsRelation.SE => Cell(if (y % 2 == 0) x else x + 1, y + 1)
    case CellsRelation.SW => Cell(if (y % 2 == 0) x - 1 else x, y + 1)
  }

  def myRelationFrom(cell : Cell) : List[CellsRelation] = {
    def foo(cell : Cell) : List[CellsRelation] = {
      val dx = x - cell.x

      meOnDiagFrom(cell) match {
        case Some(a) => List(a)
        case None =>
          if (dx < 0) {
            CellsRelation.W :: goTo(CellsRelation.E).myRelationFrom(cell)
          }
          else {
            CellsRelation.E :: goTo(CellsRelation.W).myRelationFrom(cell)
          }
      }
    }

    foo(cell).distinct.filter(_ != CellsRelation.Stop)
  }
  
  private def meOnDiagFrom(cell : Cell) : Option[CellsRelation] = {
    val dx = x - cell.x
    val dy = y - cell.y

    if (dy == 0 && dx == 0) {
      Some(CellsRelation.Stop)
    }
    else {
      if (dy == 0) {
        // W or E
        if (dx < 0) {
          // W
          Some(CellsRelation.W)
        }
        else {
          // E
          Some(CellsRelation.E)
        }
      }
      else if ((dy > 0) && (dy % 2 == 1 && dy == 2 * dx + 1) || (dy % 2 == 0 && dy == 2 * dx)) {
        // SE
        Some(CellsRelation.SE)
      }
      else if ((dy < 0) && (dy % 2 == 1 && dy == 2 * dx - 1) || (dy % 2 == 0 && dy == 2 * dx)) {
        // NW
        Some(CellsRelation.NW)
      }
      else if ((dy > 0) && (dy % 2 == 1 && dy == -dx * 2 - 1) || (dy % 2 == 0 && dy == -dx * 2)) {
        // SW
        Some(CellsRelation.SW)
      }
      else if ((dy < 0) && (dy % 2 == 1 && dy == -dx * 2 + 1) || (dy % 2 == 0 && dy == -dx * 2)) {
        // NE
        Some(CellsRelation.NE)
      }
      else None
    }
  }
}

/*
  x y  dx dy

  SE
  0 0  -- --
  0 1   0  1   dy = 2dx + 1  dy % 2 == 1
  1 2   1  2   dy = 2dx      dy % 2 == 0
  1 3   1  3   dy = 2dx + 1  dy % 2 == 1
  2 4   2  4   dy = 2dx
  2 5   2  5   dy = 2dx + 1
  3 6   3  6   dy = 2dx
  3 7   3  7
  4 8   4  8
  4 9   4  9

  NW
  4 9
  4 8   0 -1   dy = 2dx - 1  dy % 2 == 1
  3 7  -1 -2   dy = 2dx      dy % 2 == 0
  3 6  -1 -3   dy = 2dx - 1  dy % 2 == 1
  2 5
  2 4

  SW
  4 0
  3 1  -1 1    dy = -2dx - 1 dy % 2 == 1
  3 2  -1 2    dy = -2dx     dy % 2 == 0
  2 3  -2 3    dy = -2dx - 1

  NE
  2 3
  3 2   1 -1   dy = -2dx + 1 dy % 2 == 1
  3 1   1 -2   dy = -2dx     dy % 2 == 0
  4 0   2 -3   dy = -2dx + 1
 */