import ru.biocad.game._
import ru.biocad.server.ServiceHolder
import ru.biocad.solver.{TreeSolver, DecisionTree}
import ru.biocad.util.Parser

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:14
 */
object Main extends App {

  override def main(args : Array[String]) = {
    val sh = new ServiceHolder
//    val (game, initial) = loadGame("0", 0)
//    val solver = new TreeSolver(game)
//    val solution = solver.playGame(initial, 4, 1)
//    println(solution)
//    println(solution.map(c => Move(c).name).mkString(" "))
  }

  def loadGame(game : String, seed : Int) : (Game, GameState) = {
    val rawProblem = scala.io.Source.fromFile(s"problems/problem_$game.json").mkString
    val parsedProblem = Parser.parseProblem(rawProblem)
    val seeds = parsedProblem.sourceSeeds
    val (board, filled, beezArray) = parsedProblem.getGameRules

    val beez = (beezArray zip seeds).find(_._2 == seed) match {
      case Some((bees, _)) => bees
      case None => throw new RuntimeException("Go to hell!")
    }
    println(s"Game $game ($seed) loaded")
    println(s"# of figures: ${beez.length}")
    (new Game(board), GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez,
      currentBee = 0, previous = Set.empty[Set[Cell]], score = 0, lastAction = HZ))
  }
}
