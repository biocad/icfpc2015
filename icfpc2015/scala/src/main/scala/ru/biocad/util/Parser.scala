package ru.biocad.util


import scalaz._, Scalaz._
import argonaut._, Argonaut._

import ru.biocad.game.{Board, Direction, Cell}
import ru.biocad.game.Direction._

object Parser {

  def parseProblem(c: String) = {
    ???
  }

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


case class Problem(height: Int, width: Int, id: Int, filled: Vector[Cell])


object Problem {
  implicit def ProblemCodecJson: CodecJson[Problem] =
    casecodec4(Problem.apply, Problem.unapply)("height", "width", "id", "filled")
}


object ParserTest extends App {
  
  val input =
    """{
      |  "height": 10,
      |  "width": 10,
      |  "id": 0,
      |  "filled": [{"x":2,"y":4}]
      |}
    """.stripMargin

  val board = input.decodeOption[Board].getOrElse(Nil)
  val cells = input.decodeOption[Vector[Cell]].getOrElse(Nil)
  val problem = input.decodeOption[Problem].getOrElse(Nil)

  println(board)
  println(cells)
  println(problem)

}
