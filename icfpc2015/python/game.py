# -*- coding: utf-8 -*-
class Game(object):
    def __init__(self, board, units):
        self.board = board
        self.units = units

    def update(self, c):
        return self

    def dumps(self):
        return "\n".join([self.board.dumps(), "\n".join(x.dumps() for x in self.units)])