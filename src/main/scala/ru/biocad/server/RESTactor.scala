package ru.biocad.server

import akka.actor.Actor
import spray.routing._

/**
 * User: pavel
 * Date: 08.08.15
 * Time: 13:24
 */
class RESTactor extends Actor with HttpService {
  override def actorRefFactory = context

  override def receive = runRoute(myRoute)

  val myRoute =
    path("field") {
      get {
        complete {
          "hello"
        }
      }
    } ~
    path("move" / Segment) {
      move =>
        get {
          complete {
            s"move: $move"
          }
        }
    }
}