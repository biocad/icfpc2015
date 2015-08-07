from icfpc2015.python.parser import parse_units
import json

class Generator(object):
    def __init__(self):
        self.modulus = 2**31
        self.multiplier = 1103515245
        self.increment = 12345

    def get(self, units, sourceSeeds, sourceLength):
        ulen = len(units)
        result = {}
        for seed in sourceSeeds:
            result[seed] = [units[0]]
            gseed = seed
            for i in range(sourceLength):
                gseed = (self.multiplier * gseed + self.increment) % self.modulus
                unit_inx = (gseed >> 16) & 0x7fff % ulen
                result[seed].append(units[unit_inx])
        return result

if __name__ == "__main__":
    with open('../problems/problem_11.json', "rt") as fd:
        s = fd.readlines()
    json_data = json.loads("".join(s))
    units = parse_units(json_data)
    g = Generator()
    seqs = g.get(units, [17], 10)
