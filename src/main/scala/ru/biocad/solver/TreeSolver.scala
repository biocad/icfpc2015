package ru.biocad.solver

import ru.biocad.game._

import scala.util.Random

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 12:14
 */
class TreeSolver(game : Game, scorer : Scorer) {
  def playGame(initial : GameState, depth : Int, depthStep : Int) : String = {
    getTree(initial, depth) match {
      case Some(tree) =>
        println("Initial tree created")
        playTree(tree, depthStep)
      case None =>
        println("Cannot create tree")
        ""
    }
  }

  def playTree(initial : DecisionTree, depthStep : Int) : String =
    makeDecision(initial, depthStep) match {
      case Some((move, tree)) =>
        move.symbols.head + playTree(tree, depthStep)
      case None =>
        ""
    }

  def getTree(state : Scored, depth : Int) : Option[DecisionTree] = {
    state match {
      case InvalidMove =>
        None
      case _ : EndState =>
        Some(DecisionTree(state = state, variants = Map.empty[Move, DecisionTree]))
      case _ : GameState if depth == 0 =>
        Some(DecisionTree(state = state, variants = Map.empty[Move, DecisionTree]))
      case gs : GameState =>
        Some(DecisionTree(state = gs, variants = Move.all.flatMap {
          case move => getTree(game.movement(gs)(move).merge, depth - 1).map(move -> _)
        }.toMap))
    }
  }

  def makeDecision(tree : DecisionTree, depthStep : Int) : Option[(Move, DecisionTree)] = {
    if (tree.variants.isEmpty) {
      None
    }
    else {
      tree.variants.map {
        case (move, child) =>
          (move, child, scoreOfTree(child) + Random.nextDouble())
      }.maxBy(_._3) match {
        case (move, child, _) =>
          Some((move, updateTree(child, depthStep)))
      }
    }
  }

  def updateTree(tree : DecisionTree, depthStep : Int) : DecisionTree = {
    if (tree.variants.isEmpty) {
      getTree(tree.state, depthStep) match {
        case Some(newtree) => newtree
        case None => tree
      }
    }
    else {
      DecisionTree(tree.state, variants = tree.variants.map {
        case (move, child) =>
          move -> updateTree(child, depthStep)
      })
    }
  }

  def scoreOfTree(tree : DecisionTree) : Double =
    if (tree.variants.isEmpty) {
      scorer(scored2either(tree.state))
    }
    else {
      scorer(scored2either(tree.state)) + tree.variants.par.map { case (_, child) => scoreOfTree(child) }.max
    }

  def scored2either : Scored => Either[GameState, EndState] = {
    case gs : GameState =>
      Left(gs)
    case es : EndState =>
      Right(es)
  }
}
