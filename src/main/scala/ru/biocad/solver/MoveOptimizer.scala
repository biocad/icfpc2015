package ru.biocad.solver

import java.util

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie
import ru.biocad.game.Move

import scala.util.Try

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 19:35
 */
class MoveOptimizer(powers : Iterator[String]) {
  val powerMoves = powers.map(string2moves).toArray
  println(s"Length of power: ${powerMoves.length}")

  def startingPowers(solution : String) : Iterable[String] = {
    val moves = string2moves(solution)

    ???
  }

  def optimize(solution : String) : String = {
    val moves = string2moves(solution)

    val map = new util.TreeMap[String, String]()
    powerMoves.zipWithIndex.foreach {
      case (power, i) =>
        map.put(i.toString, power)
    }
    val acdat = new AhoCorasickDoubleArrayTrie[String]()
    acdat.build(map)

    val results = acdat.parseText(moves)
    println(results)
    solution
  }

  private def string2moves(power : String) : String =
    power.flatMap(pp => Try{Move.apply(pp).symbols.head}.toOption).mkString
}
