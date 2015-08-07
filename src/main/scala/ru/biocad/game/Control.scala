package ru.biocad.game

import ru.biocad.game.Direction.Direction

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 1:33
 */
object Control {
  val forwardMap = Map(
    Direction.West -> "p'!.03",
    Direction.East -> "bcefy2",
    Direction.SouthWest -> "aghij4",
    Direction.SouthEast -> "lmno 5",

    Direction.RotateClock -> "dqrvz1",
    Direction.RotateCounterClock -> "kstuwx",

    Direction.Nothing -> "\t\r\n"
  )

  val backwardMap = forwardMap.flatMap {
    case (dir, str) =>
      str.flatMap(c => List(c -> dir, c.toUpper -> dir))
  }

  def apply : Char => Direction = backwardMap
}

/*

  {p, ', !, ., 0, 3}	move W
  {b, c, e, f, y, 2}	move E
  {a, g, h, i, j, 4}	move SW
  {l, m, n, o, space, 5}    	move SE
  {d, q, r, v, z, 1}	rotate clockwise
  {k, s, t, u, w, x}	rotate counter-clockwise
  \t, \n, \r	(ignored)

 */
