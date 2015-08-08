package ru.biocad.search

import ru.biocad.game._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Problem(val startCell: Cell, val goalCell: Cell, val bee: Bee, val boardState: BoardState) {
  def successors(state: Cell): Vector[(Cell, ArrayBuffer[Move])] = {
    val allowed = getAllowedMoves(bee, boardState)
    allowed.toVector
  }

  def getAllowedMoves(bee: Bee, boardState: BoardState): Array[(Cell, Move)] = {
    Move.all.map{
      case d : Direction =>
        (d, bee.pivot.move(d), boardState.isLocked(Bee(bee.members.map(_.move(d)), bee.pivot.move(d))))
      case r : Rotation =>
        (r, bee.pivot, boardState.isLocked(Bee(bee.members.map(_.rotate(bee.pivot, r)), bee.pivot.rotate(bee.pivot, r))))
    }.filter(_._3 == false).map(o => (o._2, o._1)).toArray
  }
}


object Search {

  def bfs(problem: Problem): ArrayBuffer[Move] = {
    val closed = new ArrayBuffer[Cell]()
    val stack = new mutable.Queue[(Cell, ArrayBuffer[Move])]()
    val solution = new ArrayBuffer[Move]()

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Move])): ArrayBuffer[Move] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => stack.enqueue((c, solution ++ s))
        }
        go(stack.dequeue())
      }
    }

    go((problem.startCell, solution))
  }

  def dfs(problem: Problem): ArrayBuffer[Move] = {

    val closed = new ArrayBuffer[Cell]()
    val stack = new mutable.Stack[(Cell, ArrayBuffer[Move])]()
    val solution = new ArrayBuffer[Move]()

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Move])): ArrayBuffer[Move] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => stack.push((c, solution ++ s))
        }
        go(stack.pop())
      }
    }

    go((problem.startCell, solution))
  }

  def uniformCostSearch(problem: Problem): ArrayBuffer[Move] = {
    implicit val ordering: Ordering[(Cell, ArrayBuffer[Cell], Double)] = new Ordering[(Cell, ArrayBuffer[Cell], Double)] {
      override def compare(x: (Cell, ArrayBuffer[Cell], Double), y: (Cell, ArrayBuffer[Cell], Double)): Int = {
        x._3 compareTo y._3
      }
    }

    val closed = new ArrayBuffer[Cell]()
    val queue = new mutable.PriorityQueue[(Cell, ArrayBuffer[Move], Double)]()
    val solution = new ArrayBuffer[Move]()

    @annotation.tailrec
    def go(state: (Cell, ArrayBuffer[Move], Double)): ArrayBuffer[Move] = {
      if (state._1 == problem.goalCell) {
        state._2
      }
      else {
        if (!closed.contains(state._1)) closed.append(state._1)
        problem.successors(state._1).foreach {
          case (c, s) => queue.enqueue((c, solution ++ s, 0))
        }
        go(queue.dequeue())
      }
    }

    go((problem.startCell, solution, 0))
  }
}
