package ru.biocad.search

import ru.biocad.game.{Bee, Cell}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Problem(val startCell: Cell, val goalCell: Cell, val bee: Bee) {
  def successors(state: Cell): Vector[(Cell, ArrayBuffer[Cell])] = {
    ???
  }
}


object Search {

  def bfs(problem: Problem): ArrayBuffer[Cell] = {
    val closed = new ArrayBuffer[Cell]()
    val stack = new mutable.Queue[(Cell, ArrayBuffer[Cell])]()
    val solution = new ArrayBuffer[Cell]()

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Cell])): ArrayBuffer[Cell] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => stack.enqueue((c, s :+ c))
        }
        go(stack.dequeue())
      }
    }

    go((problem.startCell, solution))
  }

  def dfs(problem: Problem): ArrayBuffer[Cell] = {

    val closed = new ArrayBuffer[Cell]()
    val stack = new mutable.Stack[(Cell, ArrayBuffer[Cell])]()
    val solution = new ArrayBuffer[Cell]()

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

    go((problem.startCell, solution))
  }

  def uniformCostSearch(problem: Problem): ArrayBuffer[Cell] = {

    val closed = new ArrayBuffer[Cell]()
    val queue = new mutable.PriorityQueue[(Cell, ArrayBuffer[Cell], Double)]()
    val solution = new ArrayBuffer[Cell]()

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Cell], Double)): ArrayBuffer[Cell] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => queue.enqueue((c, s :+c, 0))
        }
        go(queue.dequeue())
      }
    }

    go((problem.startCell, solution, 0))
  }
}
