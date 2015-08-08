import ru.biocad.game.{BoardState, Game, GameState}
import ru.biocad.util.Parser

/**
 * User: pavel
 * Date: 07.08.15
 * Time: 19:14
 */
object Main extends App {

  override def main(args : Array[String]) = {

    val rawProblem = scala.io.Source.fromFile("problems/problem_0.json").mkString

    val parsedProblem = Parser.parseProblem(rawProblem)

    val (board, filled, beezArray) = parsedProblem.getGameRules

    beezArray.foreach {
      case beez =>
        val game = new Game(board)
        val initial = GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez, currentBee = 0)

        "45ppppp45ppppp".foldLeft(Option(initial)) {
          case (st, ch) =>
            st match {
              case Some(state) =>
                val newState = game.movement(state)(ch)
                print(newState.get.dumpJson)
                newState
              case None => None
            }
        }
    }

  }
}
