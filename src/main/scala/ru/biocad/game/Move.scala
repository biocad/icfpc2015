package ru.biocad.game

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:19
 */
trait Move {
  def name : String
  def symbols : String
}

trait Direction extends Move

object West extends Direction {
  override def name : String = "W"
  override def symbols : String = "p'!.03"
}
object East extends Direction {
  override def name : String = "E"
  override def symbols : String = "bcefy2"
}
object SouthWest extends Direction {
  override def name : String = "SW"
  override def symbols : String = "aghij4"
}
object SouthEast extends Direction {
  override def name : String = "SE"
  override def symbols : String = "lmno 5"
}
object MoveNothing extends Direction {
  override def name : String = "N"
  override def symbols : String = "\t\r\n"
}

trait Rotation extends Move

object RotateClock extends Rotation {
  override def name : String = "RC"
  override def symbols : String = "dqrvz1"
}
object RotateCounterClock extends Rotation {
  override def name : String = "RCC"
  override def symbols : String = "kstuwx"
}

object Move {
  val all : List[Move] = List(West, East, SouthWest, SouthEast, MoveNothing, RotateClock, RotateCounterClock)
  val decode = all.flatMap(mv => mv.symbols.flatMap(s => List(s -> mv, s.toUpper -> mv))).toMap

  def apply(ch : Char) : Move = decode(ch)
}

object Direction {
  val all : List[Move] = List(West, East, SouthWest, SouthEast, MoveNothing)
}

object Rotation {
  val all : List[Move] = List(RotateClock, RotateCounterClock)
}