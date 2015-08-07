# -*- coding: utf-8 -*-
class Board(object):
    def __init__(self, id, height, width, filled, length, sourceSeeds):
        self.id = id
        self.height = height
        self.width = width
        self.filled = filled
        self.length = length
        self.seeds = sourceSeeds

    def dumps(self):
        return "id: %d (%dx%d) %d [%s] [%s]" % (self.id, self.height, self.width,
                                              self.length, " ".join(map(str, self.seeds)),
                                              " ".join(map(str, self.filled)))
