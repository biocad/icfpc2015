package ru.biocad.util

import akka.actor.ActorSystem
import spray.json.DefaultJsonProtocol
import spray.routing.SimpleRoutingApp

case class Foo(a: String, b: Int)
case class Bar(c: Long, d: String)

object FooBarJsonProtocol extends DefaultJsonProtocol{
    implicit val fooFormat = jsonFormat2(Foo)
    implicit val barFormat = jsonFormat2(Bar)
}

object Server extends App with SimpleRoutingApp {
    implicit val system = ActorSystem("my-system")

    case class Foo(a: String, b: Int)
    case class Bar(c: Long, d: String)

    startServer(interface = "localhost", port = 8080) {
        get {
            path("foo") {
                complete {
                    "FOO HERE" //with the implicit in scope this will serialize as json
                }
            }
        } ~ post {
            path("bar") {
                complete("BAR HERE HAHAHA") //serialize bar to json (or whatever processing you want to do
            }
        }
    }
}