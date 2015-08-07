package ru.biocad.util

import ru.biocad.game.Direction
import ru.biocad.game.Direction._

object Parser {

  def parseSoleCommand(c: String): Direction = {
    c match {
      case _ if c == "W" => Direction.West
      case _ if c == "E" => Direction.East
      case _ if c == "SW" => Direction.SouthWest
      case _ if c == "SE" => Direction.SouthEast
      case _ if c == "CW" => Direction.RotateClock
      case _ if c == "CCW" => Direction.RotateCounterClock
      case _ => throw new IllegalArgumentException("NO WAYS TO GO!!! GO HOME AND DIE!!!!")
    }
  }

  def parseString(c: String): Vector[Direction] = {
    c.split(" ").map(parseSoleCommand).toVector
  }

}
