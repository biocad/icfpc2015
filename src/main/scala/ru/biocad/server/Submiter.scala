package ru.biocad.server

import java.io.{File, PrintWriter}

import argonaut.Argonaut._
import argonaut.CodecJson
import spray.http.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:51
 */
class Submiter(token: String = "RVm6OOelIARr4U0Vi39X/fjCcU1YOOmjbZGTFEBzZ98=", command: Int = 24) {
  private val _url = s"https://davar.icfpcontest.org/teams/$command/solutions"
  private val fileName = "cool_submitions.txt"

  case class Problem(task: Int, seed: Int)

  case class Solution(solution: String, score: Int)

  def submitIfCool(task: Int, seed: Int, solution: String, score: Int): Option[Int] = {
    if (score > taskCool(task, seed)) {
      updateCool(task, seed, solution, score)
      Some(submit(task, seed, solution, score))
    }
    else {
      None
    }
  }

  private def taskCool(task: Int, seed: Int): Int = {
    readFile.find(_._1 == Problem(task, seed)) match {
      case Some((_, solution)) =>
        solution.score
      case None =>
        0
    }
  }

  private def updateCool(task: Int, seed: Int, solution: String, score: Int): Unit = {
    val current = readFile
    writeFile(current + (Problem(task, seed) -> Solution(solution, score)))
  }

  private def readFile: Map[Problem, Solution] = {
    Source.fromFile(fileName).getLines().flatMap {
      case line =>
        line.split(';') match {
          case Array(task, seed, solution, score) =>
            Some(Problem(task.toInt, seed.toInt) -> Solution(solution, score.toInt))
          case _ =>
            None
        }
    }.toMap
  }

  private def writeFile(map: Map[Problem, Solution]): Unit = {
    val pw = new PrintWriter(new File(fileName))

    implicit val ordering = new Ordering[Problem] {
      override def compare(x: Problem, y: Problem): Int =
        if (x.task > y.task || (x.task == y.task && x.seed > y.seed)) 1 else if (x.task == y.task && x.seed == y.seed) 0 else -1
    }

    map.toList.sortBy(_._1).foreach {
      case (problem, solution) =>
        pw.println(s"${problem.task};${problem.seed};${solution.solution};${solution.score}")
    }
    pw.close()
  }

  import dispatch._

  private def submit(task: Int, seed: Int, solution: String, score: Int): Int = {
    val req = url(_url).POST
      .as_!(user = "", password = token)
      .setBody(formatPost(task, seed, solution, score))
      .addHeader("Content-type", "application/json")

    val result = Http(req OK as.String).either

    result() match {
      case Right(content) => 200 // println("Content: " + content); 200
      case Left(StatusCode(404)) => 404 // println("Not found"); 404
      case Left(StatusCode(code)) => code // println("Some other code: " + code.toString); code
      case Left(_) => 777 // println("Shit happend"); 777
    }
  }

  private def formatPost(task: Int, seed: Int, solution: String, score: Int): String =
    "[" + SubmitionPost(task, seed, solution, s"$task.$seed.$score.${DateTime.now.toString()}").asJson.pretty(nospace) + "]"
}

case class SubmitionPost(problemId: Int, seed: Int, solution: String, tag: String)

object SubmitionPost {
  implicit def SubmitionPostJsonCodec: CodecJson[SubmitionPost] =
    casecodec4(SubmitionPost.apply, SubmitionPost.unapply)("problemId", "seed", "solution", "tag")
}