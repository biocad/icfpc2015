import ru.biocad.game._
import ru.biocad.server.{ServiceHolder, Submiter}
import ru.biocad.solver.{StringChanger, Weights}

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:14
 */
object Main extends App {

  override def main(args: Array[String]) = {
    val sh = new ServiceHolder
    //    val (game, initial) = Game.loadGame("0", 0)
    //    val solver = new TreeSolver(game)
    //    val solution = solver.playGame(initial, 4, 1)
    //    println(solution)
    //    println(solution.map(c => Move(c).name).mkString(" "))
  }
}


object Gambler extends App {
  def playGame(problem: Int, seed: Int, depth: Int, stringOptimizer : StringChanger): (Int, String) = {
    val weights = Weights()
    val scorer = new ru.biocad.solver.Scorer(weights)
    val gp = new GamePlayer(scorer, depth)

    val gameState = gp.startNewGame(problem, seed)

    @annotation.tailrec
    def playState(prev: GameState, next: Option[GameState], scoreAcc: Int): (Int, String) =
      next match {
        case None => (scoreAcc, gp.solution) // prev.lastAction.score
        case Some(state) => playState(state, gp.moveItPlease(), scoreAcc + state.score)
      }

    val (finalScore, solution) = playState(gp.state, gp.moveItPlease(), 0)

    (finalScore, stringOptimizer(solution))
  }

  override def main(args: Array[String]) {

    val stringOptimizer =
      new StringChanger(scala.io.Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(s"power_phrases.txt")).getLines())

    val problems = Game.loadProblems

    val problem = 1
    val attempt = 4
    val seeds = problems(problem)

    val bestOfTheBest = seeds.map { case seed =>
      val games = (0 until attempt).par.map(i => {
        playGame(problem, seed, 4, stringOptimizer)
      })
      val (score, solution) = games.maxBy(g => g._1)
      println(s"Problem: $problem, Seed: $seed, Score: $score | Solution: $solution")
      (seed, solution, score)
    }

    for {
      g <- bestOfTheBest
    } yield {
      val submitter = new Submiter()
      val seed = g._1
      val solution = g._2
      val score = g._3
      submitter.submitIfCool(problem, seed, solution, score) match {
        case Some(response) =>
          println(s"Submiter answer: $response")
          println(s"Submitted problem #$problem seed: $seed, score: $score")
        case None =>
          println(s"Ignored problem #$problem seed: $seed, score: $score")
      }
    }
  }
}
