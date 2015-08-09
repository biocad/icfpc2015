package ru.biocad.solver

import ru.biocad.game.{Move, GameState}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 12:16
 */
case class DecisionTree(gameState : GameState, variants : Map[Move, DecisionTree])