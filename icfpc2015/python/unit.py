__author__ = 'pavel'

from icfpc2015.cell import Cell, dumps_cell
from icfpc2015.movement import Movement, BACKWARD_MAP


class Unit(object):
    def __init__(self, members, pivot):
        self.members = list(members)
        self.pivot = pivot

        self.movement = {
            Movement.W: self.move_w,
            Movement.E: self.move_e,
            Movement.SW: self.move_sw,
            Movement.SE: self.move_se,
            Movement.RC: self.rotate_clockwise,
            Movement.RCC: self.rotate_rotate_counter_clockwise,
            Movement.NONE: self.nothing,
        }

    def make_move(self, c):
        self.movement[BACKWARD_MAP[c]]()

    def move_w(self):
        return Unit(map(lambda member: Cell(member.x - 1, member.y), self.members), self.pivot)

    def move_e(self):
        return Unit(map(lambda member: Cell(member.x + 1, member.y), self.members), self.pivot)

    def move_sw(self):
        return Unit(map(lambda member: Cell(member.x - 1, member.y - 1), self.members), self.pivot)

    def move_se(self):
        return Unit(map(lambda member: Cell(member.x + 1, member.y - 1), self.members), self.pivot)

    def rotate_clockwise(self):
        pass

    def rotate_rotate_counter_clockwise(self):
        pass

    def nothing(self):
        return self

    def dumps(self):
        return "%s: [%s]" % (self.pivot, ", ".join(map(dumps_cell, self.members)))