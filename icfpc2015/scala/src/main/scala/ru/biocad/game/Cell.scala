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
    case Direction.RotateConterClock =>
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
    val dx = x - cell.x
    val dy = y - cell.y

    if (dy == 0 && dx == 0) {
      List.empty[CellsRelation]
    }
    else {
      if (dy == 0) { // W or E
        if (dx < 0) { // W
          List(CellsRelation.W)
        }
        else { // E
          List(CellsRelation.E)
        }
      }
      else if ((dy > 0) && (dy % 2 == 1 && dy == 2 * dx + 1) || (dy % 2 == 0 && dy == 2 * dx)) { // SE
        List(CellsRelation.SE)
      }
      else if ((dy < 0) && (dy % 2 == 1 && dy == 2 * dx - 1) || (dy % 2 == 0 && dy == 2 * dx)) { // NW
        List(CellsRelation.NW)
      }
      else if ((dy > 0) && (dy % 2 == 1 && dy == -dx * 2 - 1) || (dy % 2 == 0 && dy == -dx * 2)) { // SW
        List(CellsRelation.SW)
      }
      else if ((dy < 0) && (dy % 2 == 1 && dy == -dx * 2 + 1) || (dy % 2 == 0 && dy == -dx * 2)) { // NE
        List(CellsRelation.NE)
      }
      else {
        ???
      }
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