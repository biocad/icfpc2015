package ru.biocad.game

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 18:47
 */
trait EndState extends Scored {
  def name : String
}

case object GameEnd extends EndState {
  override def name : String = "Game end"
  override def score : Int = 100500
}
case object InvalidMove extends EndState {
  override def name : String = "Invalid move"
  override def score : Int = -100500
}
case object CannotCreateBee extends EndState {
  override def name : String = "Cannot create bee"
  override def score : Int = -50
}
