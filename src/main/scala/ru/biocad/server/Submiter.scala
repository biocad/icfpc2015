package ru.biocad.server

import java.io.{File, PrintWriter}

import scala.io.Source
import scalaj.http.Http

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 22:51
 */
class Submiter(token : String = "RVm6OOelIARr4U0Vi39X/fjCcU1YOOmjbZGTFEBzZ98=", command : Int = 24) {
  private val url = s"https://davar.icfpcontest.org/teams/$command/solutions"
  private val fileName = "cool_submitions.txt"

  case class Problem(task : Int, seed : Int)
  case class Solution(solution : String, score : Int)

  def submitIfCool(task : Int, seed : Int, solution : String, score : Int) : Option[Int] = {
    if (score > taskCool(task, seed)) {
      updateCool(task, seed, solution, score)
      Some(submit(task, seed, solution, score))
    }
    else {
      None
    }
  }

  private def taskCool(task : Int, seed : Int) : Int = {
    readFile.find(_._1 == Problem(task, seed)) match {
      case Some((_, solution)) =>
        solution.score
      case None =>
        0
    }
  }

  private def updateCool(task : Int, seed : Int, solution : String, score : Int) : Unit = {
    val current = readFile
    writeFile(current + (Problem(task, seed) -> Solution(solution, score)))
  }

  private def readFile : Map[Problem, Solution] = {
    Source.fromFile(fileName).getLines().flatMap {
      case line =>
        line.split(';') match {
          case Array(task, seed, solution, score) =>
            Some(Problem(task.toInt, seed.toInt) -> Solution(solution,score.toInt))
          case _ =>
            None
        }
    }.toMap
  }

  private def writeFile(map : Map[Problem, Solution]) : Unit = {
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

  private def submit(task : Int, seed : Int, solution : String, score : Int) : Int = {
    Http(url)
      .auth(user = "", password = token)
      .header("Content-Type", "application/json")
      .postData(formatPost(task, seed, solution, score))
    println("Something happend")
    201
  }

  private def formatPost(task : Int, seed : Int, solution : String, score : Int) : String =
    s"""
      |[
      |  { "problemId": $task
      |  , "seed":      $seed
      |  , "tag":       "$task.$seed.${score}_${System.currentTimeMillis()}"
      |  , "solution":  "$solution"
      |  }
      |]
    """.stripMargin

  implicit def problem2double(p : Problem) : Double =
    p.task + p.seed / 1000000
}