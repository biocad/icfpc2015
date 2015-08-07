package ru.biocad.search

import ru.biocad.game.Cell

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Problem(val startCell: Cell, val goalCell: Cell) {
  def successors(state: Cell): Vector[(Cell, ArrayBuffer[Cell])] = {
    ???
  }
}


object Search {

  def dfs(problem: Problem): ArrayBuffer[Cell] = {

    val closed = new ArrayBuffer[Cell]()
    val stack = new mutable.Stack[(Cell, ArrayBuffer[Cell])]()
    val solution = new ArrayBuffer[Cell]()

    stack.push((problem.startCell, solution))

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Cell])): ArrayBuffer[Cell] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => stack.push((c, s :+ c))
        }
        go(stack.pop())
      }
    }

    go(stack.pop())
  }
}
