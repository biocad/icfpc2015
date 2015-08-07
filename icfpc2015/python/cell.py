__author__ = 'pavel'


from collections import namedtuple

Cell = namedtuple("Cell", ['x', 'y'])


def dumps_cell(cell):
    return "(%d, %d)" % (cell.x, cell.y)