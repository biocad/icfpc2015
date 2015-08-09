package ru.biocad.game

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 13:54
 */
trait GameAction {
  def linesCleared : Int
  def score : Int
}

case object HZ extends GameAction {
  override def linesCleared : Int = 0
  override def score : Int = 0
}
case object SimpleMove extends GameAction {
  override def linesCleared : Int = 0
  override def score : Int = 1
}
case class LockedMove(linesCleared : Int) extends GameAction {
  override def score : Int = 1 + 10 * linesCleared
}
