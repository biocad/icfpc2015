__author__ = 'pavel'

from collections import namedtuple
from movement import Movement, BACKWARD_MAP


Memeber = namedtuple('Member', ['x', 'y'])


class Unit(object):
    def __init__(self, members, pivot):
        self.members = members
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
        pass

    def move_e(self):
        pass

    def move_sw(self):
        pass

    def move_se(self):
        pass

    def rotate_clockwise(self):
        pass

    def rotate_rotate_counter_clockwise(self):
        pass

    def nothing(self):
        pass