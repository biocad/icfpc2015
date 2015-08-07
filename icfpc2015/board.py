__author__ = 'pavel'


class Board(object):
    def __init__(self, id, height, width, filled, length):
        self.id = id
        self.height = height
        self.width = width
        self.filled = filled
        self.length = length