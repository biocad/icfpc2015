package ru.biocad.game

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 20:03
 */
object CellsRelation extends Enumeration {
  type CellsRelation = Value

  val W  = Value(1)
  val NW = Value(2)
  val NE = Value(3)
  val E  = Value(4)
  val SE = Value(5)
  val SW = Value(6)

  def next : CellsRelation => CellsRelation = {
    case W => NW
    case NW => NE
    case NE => E
    case E  => SE
    case SE => SW
    case SW => W
  }

  def pred : CellsRelation => CellsRelation = {
    case W => SW
    case NW => W
    case NE => NW
    case E  => NE
    case SE => E
    case SW => SE
  }
}