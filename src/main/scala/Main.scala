import breeze.linalg.{norm, DenseVector}
import breeze.optimize.{LBFGS, DiffFunction}
import ru.biocad.game._
import ru.biocad.server.{Submiter, ServiceHolder}
import ru.biocad.solver.Weights

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

  val submitter = new Submiter()

  def playGame(problem: Int, seed: Int, depth: Int, depthStep: Int): (Int, String) = {
    val weights = new Weights(-1, 2) // new Weights(-1, 2)
    val scorer = new ru.biocad.solver.Scorer(weights)
    val gp = new GamePlayer(scorer)
    val gameState = gp.startNewGame(problem, seed)


    @annotation.tailrec
    def playState(prev: GameState, next: Option[GameState], scoreAcc: Int): (Int, String) =
      next match {
        case None => (scoreAcc, gp.solution) // prev.lastAction.score
        case Some(state) => playState(state, gp.moveItPlease(), scoreAcc + state.score)
      }

    val (finalScore, solution) = playState(gp.state, gp.moveItPlease(), 0)
    // println(s"Solution: ${solution}")
    // println(s"Game score: ${finalScore}")

    (finalScore, solution)
  }

  override def main (args: Array[String]) {

    val problem = 2
    val seeds = List(0, 679, 13639, 13948, 29639, 15385, 16783, 23862, 25221, 23027)

    val bestOfTheBest = seeds.map { case seed =>
      val games = (0 to 10).par.map(i => {
        playGame(0, 0, 6, 6)
      })
      val bestGame = games.maxBy(g => g._1)
      println(s"Problem: $problem, Seed: $seed, Score: ${bestGame._1} | Solution: ${bestGame._2}")
      (seed, bestGame._2, bestGame._1)
    }

    for {
      g <- bestOfTheBest
    } yield {
      submitter.submitIfCool(problem, g._1, g._2, g._1)
    }
  }

  def optmize = {

    val f = new DiffFunction[DenseVector[Double]] {
      def calculate(x: DenseVector[Double]) = {
        (norm((x - 3d) :^ 2d, 1d), (x * 2d) - 6d)
      }
    }

    val lbfgs = new LBFGS[DenseVector[Double]](maxIter = 100, m = 3)
    lbfgs.minimize(f, DenseVector(0, 0, 0))
  }
}
