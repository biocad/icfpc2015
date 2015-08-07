package ru.biocad.game

import argonaut.Argonaut._
import argonaut.CodecJson
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

object Bee {
  implicit def beeCodecJson: CodecJson[Bee] =
    casecodec2(Bee.apply, Bee.unapply)("members", "pivot")
}
