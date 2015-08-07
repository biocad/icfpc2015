import math
import re

class UnitScorer(object):
    def __init__(self):
        with open('power_phrases.txt') as fp:
            self.phrases = set(fp.read().splitlines())

    def _score_unit(self, size, ls, ls_old):
        """
        :param size: the number of cells in the unit
        :param ls: the number of lines cleared with the current unit
        :param ls_old: the number of lines cleared with the previous unit
        :return: unit score points
        """
        points = size + 100 * (1 + ls) * ls / 2
        if ls_old > 1:
            line_bonus = math.floor ((ls_old - 1) * points / 10)
        else:
            line_bonus = 0
        move_score = points + line_bonus
        return move_score

    def _score_path(self, path):
        score = 0
        for s in self.phrases:
            repsp = re.subn(s, '', path)[1]
            lenp = len(s)
            score += self._score_phrase(lenp, repsp)
        return score

    def _score_phrase(self, lenp, repsp):
        """
        :param lenp: the length of the phrase of power
        :param repsp: the number of times the phrase of power appears in the sequence of commands, .
        :return:
        """
        if repsp > 0:
            power_bonusp = 300
        else:
            power_bonusp = 0
        power_scorep = 2 * lenp * repsp + power_bonusp
        return power_scorep

    def score(self, unit, path, ls, ls_old):
        move_score = self._score_unit(unit.size, ls, ls_old)
        power_scores = self._score_path(path)
        return move_score + power_scores
