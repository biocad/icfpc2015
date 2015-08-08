import ru.biocad.game.{BoardState, Game, GameState}
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

    beezArray.take(1).foreach {
      case beez =>
        val game = new Game(board)
        val initial = GameState(boardState = BoardState(filled)(board), bee = beez.head, beez = beez, currentBee = 0)

        var i = 0
        "iiiiiiimimiiiiiimmimiiiimimimmimimimimmeemmimimiimmmmimmimiimimimmimmimeee\nmmmimimmimeeemiimiimimimiiiipimiimimmmmeemimeemimimimmmmemimmimmmiiimmmiii\npiimiiippiimmmeemimiipimmimmipppimmimeemeemimiieemimmmm".foldLeft(Option(initial)) {
          case (st, ch) =>
            st match {
              case Some(state) =>
                i += 1
                val newState = game.movement(state)(ch)
                newState
              case None =>
                println(i)
                None
            }
        }
    }

  }
}
