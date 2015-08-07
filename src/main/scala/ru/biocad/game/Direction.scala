package ru.biocad.game

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:19
 */
object Direction extends Enumeration {
  type Direction = Value

  val West = Value("w")
  val East = Value("e")
  val SouthWest = Value("sw")
  val SouthEast = Value("se")

  val RotateClock = Value("rotate-cw")
  val RotateCounterClock = Value("rotate-ccw")

  val Nothing = Value("nothing")
}
