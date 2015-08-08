import ru.biocad.game.{BoardState, Game, GameState}
import ru.biocad.server.ServiceHolder
import ru.biocad.util.Parser

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:14
 */
object Main extends App {

  override def main(args : Array[String]) = {

    val rawProblem = scala.io.Source.fromFile("problems/problem_6.json").mkString

    val parsedProblem = Parser.parseProblem(rawProblem)

    val (board, filled, beezArray) = parsedProblem.getGameRules

    val beez = beezArray.head
    val game = new Game(board)
    val initial = GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez, currentBee = 0)

    val sh = new ServiceHolder(game, initial)
  }
}
