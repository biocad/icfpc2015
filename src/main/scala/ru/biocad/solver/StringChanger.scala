package ru.biocad.solver

import ru.biocad.game.Move

import scala.util.Try

/**
 * User: pavel
 * Date: 10.08.15
 * Time: 1:02
 */
class StringChanger(powers : Iterator[String]) {
  val powerStrings = powers.toArray
  val powerMoves = powerStrings.map(string2cannonical)
  println(s"Length of power: ${powerMoves.length}")

  def apply(solution : String) : String = {
    val cannonical = string2cannonical(solution)
    cannonical
  }

  private def string2cannonical(power : String) : String =
    string2moves(power).map(_.symbols.head).mkString

  private def string2moves(power : String) : Array[Move] =
    power.flatMap(pp => Try{Move.apply(pp)}.toOption).toArray
}
