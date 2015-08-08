package ru.biocad.game

import argonaut.Argonaut._
import argonaut.CodecJson

case class Bee(members: Vector[Cell], pivot: Cell) {
  def move : Move => Bee = {
    case RotateClock =>
      Bee(members = members.map(_.rotate(pivot, RotateClock)), pivot = pivot)
    case RotateCounterClock =>
      Bee(members = members.map(_.rotate(pivot, RotateCounterClock)), pivot = pivot)
    case mv : Direction =>
      Bee(members = members.map(_.move(mv)), pivot.move(mv))
  }

  def width : Int =
    (members.map(_.q).max - members.map(_.q).min) + 1

  def height : Int =
    (members.map(_.r).max - members.map(_.r).min) + 1
}

object Bee {
  implicit def beeCodecJson: CodecJson[Bee] =
    casecodec2(Bee.apply, Bee.unapply)("members", "pivot")
}
