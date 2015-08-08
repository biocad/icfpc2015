package ru.biocad.search

import ru.biocad.game._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Problem(val startCell: Cell, val goalCell: Cell, val bee: Bee, val boardState: BoardState) {
  def successors(state: Cell): Vector[(Cell, ArrayBuffer[Move])] = {
    getMoves(state)
  }

  def moveWithHistory(start: Cell, direction: Direction): (Cell, ArrayBuffer[Direction]) = {
    (start.move(direction), ArrayBuffer(direction))
  }

  def rotateWithHistory(start: Cell, direction: Rotation): (Cell, ArrayBuffer[Rotation]) = {
    (start.rotate(start, direction), ArrayBuffer(direction))
  }

  def getMoves(start: Cell): Vector[(Cell, ArrayBuffer[Move])] = {

    val possibleDirections: Vector[Direction] = Vector(West, East, SouthWest, SouthEast)

    def getNeighbours(cell: Cell): Vector[(Cell, Move)] = possibleDirections.map(d => (cell.move(d), d))

    // 1-deep search
    val deep1 = getNeighbours(start).map(cd => (cd._1, ArrayBuffer(cd._2)))
    val deep2 = deep1.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep3 = deep2.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep4 = deep3.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep5 = deep4.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep6 = deep5.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep7 = deep6.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep8 = deep7.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep9 = deep8.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }
    val deep10 = deep9.flatMap { case (c, history) => getNeighbours(c).map { case (n, m) => (n, history :+ m) } }

    deep10
  }


  //    function cube_reachable(start, movement):
  //    var visited = set()
  //    add start to visited
  //    var fringes = []
  //    fringes.append([start])
  //
  //    for each 1 < k ≤ movement:
  //      fringes.append([])
  //    for each cube in fringes[k-1]:
  //    for each 0 ≤ dir < 6:
  //    var neighbor = cube_neighbor(cube, dir)
  //    if neighbor not in visited, not blocked:
  //      add neighbor to visited
  //        fringes[k].append(neighbor)
  //
  //    return visited

  def getAllowedMoves(bee: Bee, boardState: BoardState): Array[(Cell, Move)] = {
    Move.all.map {
      case d: Direction =>
        (d, bee.pivot.move(d), boardState.isLocked(Bee(bee.members.map(_.move(d)), bee.pivot.move(d))))
      case r: Rotation =>
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
    implicit val ordering: Ordering[(Cell, ArrayBuffer[Move], Double)] = new Ordering[(Cell, ArrayBuffer[Move], Double)] {
      override def compare(x: (Cell, ArrayBuffer[Move], Double), y: (Cell, ArrayBuffer[Move], Double)): Int = {
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
