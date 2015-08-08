package ru.biocad.solver

import java.util

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie
import ru.biocad.game.{MoveNothing, Move}

import scala.util.Try

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 19:35
 */
class MoveOptimizer(powers : Iterator[String]) {
  val powerStrings = powers.toArray
  val powerMoves = powerStrings.map(string2cannonical)
  println(s"Length of power: ${powerMoves.length}")

  def startingPowers(solution : String) : Array[String] = {
    val moves = string2cannonical(solution)

    (powerStrings zip powerMoves).map {
      case (str, power) =>
        var maxIndex = power.indices.reverse.takeWhile {
          case i =>
            val substring = power.substring(0, i)
            !moves.endsWith(substring)
        }.lastOption.getOrElse(0)

        if (maxIndex == power.length) {
          maxIndex = 0
        }

        println()
        println(s"Solution is $moves")
        println(s"For string $str max prefix is ${power.take(maxIndex)}")
        println(s"Max index is $maxIndex")
        println()

        (str zip power.map(Move.apply)).zipWithIndex.map{
          case ((c, m), i) =>
            s"$c(${m.name}${if (i == maxIndex) "*" else ""})"
        }.mkString
    }
  }

  def optimize(solution : String) : String = {
    val moves = string2cannonical(solution)

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

  private def string2cannonical(power : String) : String =
    string2moves(power).map(_.symbols.head).mkString

  private def string2moves(power : String) : Array[Move] =
    power.flatMap(pp => Try{Move.apply(pp)}.toOption).toArray
}
