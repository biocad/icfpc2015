package ru.biocad.game

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 13:54
 */
trait GameAction {
  def linesCleared : Int
  def score : Int
  def lockedAround : Int
}

case object HZ extends GameAction {
  override def linesCleared : Int = 0
  override def score : Int = 0
  override def lockedAround : Int = 0
}
case class SimpleMove(lockedAround : Int) extends GameAction {
  override def linesCleared : Int = 0
  override def score : Int = 1
}
case class LockedMove(linesCleared : Int, beeLockScore : Int, lockedAround : Int, penalty : Int) extends GameAction {
  override def score : Int = beeLockScore//linesCleared + beeLockScore + lockedAround - penalty
}
