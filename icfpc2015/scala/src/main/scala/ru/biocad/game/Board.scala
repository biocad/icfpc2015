package ru.biocad.game

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:17
 */
class Board(id : Int)(width : Int, height : Int)(filled : Iterable[Cell]) {
  def inBoard(cell : Cell) : Boolean =
    cell.x >= 0 && cell.x < width && cell.y >= 0 && cell.y < height
}
