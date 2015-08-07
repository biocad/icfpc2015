package ru.biocad.game

import ru.biocad.game.Direction.Direction

case class Bee(members: Vector[Cell], pivot: Cell) {
  def move : Direction => Bee = {
    case Direction.RotateClock =>
      Bee(members = members.map(_.rotate(pivot)(Direction.RotateClock)), pivot = pivot)
    case Direction.RotateCounterClock =>
      Bee(members = members.map(_.rotate(pivot)(Direction.RotateCounterClock)), pivot = pivot)
    case mv =>
      Bee(members = members.map(_.move(mv)), pivot.move(mv))
  }
}
