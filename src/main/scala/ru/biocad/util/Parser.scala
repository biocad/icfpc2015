package ru.biocad.util


import argonaut.Argonaut._
import argonaut._
import ru.biocad.game._

object Parser {

  def parseProblem(c: String): Problem = {
    val problem = c.decodeOption[Problem] // c.decodeOption[Problem].getOrElse(Nil)
    problem.get
  }

  def parseSoleCommand(c: String): Move = {
    c match {
      case _ if c == "W" => West
      case _ if c == "E" => East
      case _ if c == "SW" => SouthWest
      case _ if c == "SE" => SouthEast
      case _ if c == "CW" => RotateClock
      case _ if c == "CCW" => RotateCounterClock
      case _ => throw new IllegalArgumentException("NO WAYS TO GO!!! GO HOME AND DIE!!!!")
    }
  }

  def parseString(c: String): Vector[Move] = {
    c.split(" ").map(parseSoleCommand).toVector
  }

}


case class Problem(height: Int,
                   width: Int,
                   id: Int,
                   filled: Vector[Cell],
                   sourceSeeds: Vector[Int],
                   sourceLength: Int,
                   units: Vector[Bee]) {

  def getGameRules = {
    val board = Board(id, width, height)
    val unitWaves = new AbsBeeGenerator(board).generate(units.toArray, sourceSeeds.toArray, sourceLength)

    (board, filled, unitWaves)
  }

}


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
