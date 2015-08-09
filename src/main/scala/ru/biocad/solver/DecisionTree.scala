package ru.biocad.solver

import ru.biocad.game.{Move, Scored}

/**
 * User: pavel
 * Date: 09.08.15
 * Time: 12:16
 */
case class DecisionTree(state : Scored, variants : Map[Move, DecisionTree])