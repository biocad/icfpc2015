package ru.biocad.game

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
      ???
    case Direction.RotateConterClock =>
      ???
  }
}
