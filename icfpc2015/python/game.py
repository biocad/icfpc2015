# -*- coding: utf-8 -*-

from icfpc2015.python.generator import Generator

class Game(object):
    def __init__(self, board, units):
        self.board = board
        self.units = units
        g = Generator()
        self.seqs = g.get(units, board.seeds, board.length)

    def update(self, c):
        return self

    def dumps(self):
        return "\n".join([self.board.dumps(), "\n".join(x.dumps() for x in self.units)])