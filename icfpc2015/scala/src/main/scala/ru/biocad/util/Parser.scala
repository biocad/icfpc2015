package ru.biocad.util


import scalaz._, Scalaz._
import argonaut._, Argonaut._

import ru.biocad.game.{Bee, Board, Direction, Cell}
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


case class Problem(height: Int,
                   width: Int,
                   id: Int,
                   filled: Vector[Cell],
                   sourceSeeds: Vector[Int],
                   sourceLength: Int,
                   units: Vector[Bee])


object Problem {
  implicit def ProblemCodecJson: CodecJson[Problem] =
    casecodec7(Problem.apply, Problem.unapply)("height", "width", "id", "filled", "sourceSeeds", "sourceLength", "units")
}


object ParserTest extends App {
  
  val input =
    """{
      |  "height": 10,
      |  "width": 10,
      |  "id": 0,
      |  "filled": [{"x":2,"y":4}, {"x":3,"y":5}],
      |  "sourceSeeds": [0, 1],
      |  "sourceLength": 100,
      |  "units": [
      |    {
      |      "members": [
      |        {
      |          "x": 0,
      |          "y": 0
      |        }
      |      ],
      |      "pivot": {
      |        "x": 0,
      |        "y": 0
      |      }
      |    },
      |    {
      |      "members": [
      |        {
      |          "x": 0,
      |          "y": 0
      |        },
      |        {
      |          "x": 2,
      |          "y": 0
      |        }
      |      ],
      |      "pivot": {
      |        "x": 1,
      |        "y": 0
      |      }
      |    }
      |  ]
      |}
    """.stripMargin

  val board = input.decodeOption[Board].getOrElse(Nil)
  val cells = input.decodeOption[Vector[Cell]].getOrElse(Nil)
  val problem = input.decodeOption[Problem].getOrElse(Nil)

  println(board)
  println(cells)
  println(problem)

}
