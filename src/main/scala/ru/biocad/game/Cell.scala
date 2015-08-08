package ru.biocad.game

import argonaut.Argonaut._
import argonaut.CodecJson

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:18
 */
case class Cell(q : Int, r : Int) {
  def move : Direction => Cell ={
    case West =>
      Cell(q - 1, r)
    case East =>
      Cell(q + 1, r)
    case SouthWest =>
      Cell(if (r % 2 == 0) q - 1 else q, r + 1)
    case SouthEast =>
      Cell(if (r % 2 == 0) q else q + 1, r + 1)
    case MoveNothing =>
      this
  }

  def rotate(pivot : Cell, dir : Rotation) : Cell = {
    val cellCube = toCellCube(pivot)
    dir match {
      case RotateClock =>
        CellCube(x = -cellCube.z, -cellCube.x, -cellCube.y).toCell(pivot)
      case RotateCounterClock =>
        CellCube(x = -cellCube.y, -cellCube.z, -cellCube.x).toCell(pivot)
    }
  }

  def toCellCube : CellCube =
    CellCube(q, -q-r, r)

  def toCellCube(pivot : Cell) : CellCube =
    Cell(q - pivot.q, r - pivot.r).toCellCube

  def dumps : String =
    s"""{"x": $q, "y": $r}"""
}

case class CellCube(x : Int, y : Int, z : Int) {
  def toCell : Cell = Cell(q = x, r = z)

  def toCell(pivot : Cell) : Cell = {
    val tc = toCell
    Cell(tc.q + pivot.q, tc.r + pivot.r)
  }
}


object Cell {
  implicit def CellCodecJson: CodecJson[Cell] =
    casecodec2(Cell.apply, Cell.unapply)("x", "y")
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