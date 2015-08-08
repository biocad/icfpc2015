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

//  def rotate(pivot : Cell, dir : Rotation) : Cell = {
//    val isOddRow = pivot.r % 2 == 0
//    val offsetCell = toOffset(pivot)
//    println()
//    println(s"ABS -> OFF: Cell(q=$q, r=$r) -> Cell(q=${offsetCell.q}, r=${offsetCell.r})")
//    val cellCube = offsetCell.toCellCube(isOddRow)
//    println(s"OFF -> CUB: Cell(q=${offsetCell.q}, r=${offsetCell.r}) -> Cell(x=${cellCube.x}, y=${cellCube.y}, z=${cellCube.z})")
//    dir match {
//      case RotateClock =>
//        val roteatedCellCube = CellCube(-cellCube.z, -cellCube.x, -cellCube.y)
//        println(s"ROTATION:   Cube(x=${roteatedCellCube.x}, y=${roteatedCellCube.y}, z=${roteatedCellCube.z})")
//        val rotatedOffsetCell = roteatedCellCube.toCell(isOddRow)
//        println(s"CUB -> OFF: Cell(x=${roteatedCellCube.x}, y=${roteatedCellCube.y}, z=${roteatedCellCube.z}) -> Cell(q=${rotatedOffsetCell.q}, r=${rotatedOffsetCell.r})")
//        val rotatedCell = rotatedOffsetCell.fromOffset(pivot)
//        println(s"OFF -> ABS: Cell(q=${rotatedOffsetCell.q}, r=${rotatedOffsetCell.r}) -> Cell(q=${rotatedCell.q}, r=${rotatedCell.r})")
//        println()
//        rotatedCell
//      case RotateCounterClock =>
//        CellCube(-cellCube.y, -cellCube.z, -cellCube.x).toCell(isOddRow).fromOffset(pivot)
//    }
//  }

  def rotate(pivot : Cell, dir : Rotation) : Cell = {
    val isOddRow = pivot.r % 2 == 0
    val cellCube = toOffset(pivot).toCellCube(isOddRow)
    dir match {
      case RotateClock =>
        cellCube.rotateClock.toCell(isOddRow).fromOffset(pivot)
      case RotateCounterClock =>
        cellCube.rotateCounterClock.toCell(isOddRow).fromOffset(pivot)
    }
  }

  def toCellCube(isOddRow : Boolean) : CellCube = {
    val x = if (isOddRow) q - (r - (r&1)) / 2 else q - (r + (r&1)) / 2
    val z = r
    val y = -x - z

    CellCube(x, y, z)
  }

  def toOffset(pivot : Cell) : Cell = {
    Cell(q - pivot.q, r - pivot.r)
  }

  def fromOffset(pivot : Cell) : Cell = {
    Cell(q + pivot.q, r + pivot.r)
  }

  def dumps : String =
    s"""{"x": $q, "y": $r}"""
}

case class CellCube(x : Int, y : Int, z : Int) {
  def toCell(isOddRow : Boolean) : Cell = {
    val q = if (isOddRow) x + (z - (z&1)) / 2 else x + (z + (z&1)) / 2
    val r = z
    Cell(q, r)
  }

  def rotateClock : CellCube =
    CellCube(-z, -x, -y)

  def rotateCounterClock : CellCube =
    CellCube(-y, -z, -x)
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