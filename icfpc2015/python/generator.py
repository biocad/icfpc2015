# -*- coding: utf-8 -*-
class Generator(object):
    def __init__(self):
        self.modulus = 2**31
        self.multiplier = 1103515245
        self.increment = 12345

    def get(self, units, seeds, length):
        ulen = len(units)
        result = {}
        for seed in seeds:
            result[seed] = [units[0]]
            gseed = seed
            for i in range(length):
                gseed = (self.multiplier * gseed + self.increment) % self.modulus
                unit_inx = (gseed >> 16) & 0x7fff % ulen
                result[seed].append(units[unit_inx])
        return result
