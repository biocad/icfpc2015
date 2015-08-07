# -*- coding: utf-8 -*-
from enum import Enum

# {p, ', !, ., 0, 3}	move W
# {b, c, e, f, y, 2}	move E
# {a, g, h, i, j, 4}	move SW
# {l, m, n, o, space, 5}    	move SE
# {d, q, r, v, z, 1}	rotate clockwise
# {k, s, t, u, w, x}	rotate counter-clockwise
# \t, \n, \r	(ignored)

class Movement(Enum):
    W = 0
    E = 1
    SW = 2
    SE = 4
    RC = 5
    RCC = 6
    NONE = 7


FORWARD_MAP = {
    Movement.W: "p'!.03",
    Movement.E: "bcefy2",
    Movement.SW: "aghij4",
    Movement.SE: "lmno 5",
    Movement.RC: "dqrvz1",
    Movement.RCC: "kstuwx",
    Movement.NONE: "\t\n\r"
}

BACKWARD_MAP = {'\t': Movement.NONE,
                '\n': Movement.NONE,
                '\r': Movement.NONE,
                ' ': Movement.SE,
                '!': Movement.W,
                "'": Movement.W,
                '.': Movement.W,
                '0': Movement.W,
                '1': Movement.RC,
                '2': Movement.E,
                '3': Movement.W,
                '4': Movement.SW,
                '5': Movement.SE,
                'A': Movement.SW,
                'B': Movement.E,
                'C': Movement.E,
                'D': Movement.RC,
                'E': Movement.E,
                'F': Movement.E,
                'G': Movement.SW,
                'H': Movement.SW,
                'I': Movement.SW,
                'J': Movement.SW,
                'K': Movement.RCC,
                'L': Movement.SE,
                'M': Movement.SE,
                'N': Movement.SE,
                'O': Movement.SE,
                'P': Movement.W,
                'Q': Movement.RC,
                'R': Movement.RC,
                'S': Movement.RCC,
                'T': Movement.RCC,
                'U': Movement.RCC,
                'V': Movement.RC,
                'W': Movement.RCC,
                'X': Movement.RCC,
                'Y': Movement.E,
                'Z': Movement.RC,
                'a': Movement.SW,
                'b': Movement.E,
                'c': Movement.E,
                'd': Movement.RC,
                'e': Movement.E,
                'f': Movement.E,
                'g': Movement.SW,
                'h': Movement.SW,
                'i': Movement.SW,
                'j': Movement.SW,
                'k': Movement.RCC,
                'l': Movement.SE,
                'm': Movement.SE,
                'n': Movement.SE,
                'o': Movement.SE,
                'p': Movement.W,
                'q': Movement.RC,
                'r': Movement.RC,
                's': Movement.RCC,
                't': Movement.RCC,
                'u': Movement.RCC,
                'v': Movement.RC,
                'w': Movement.RCC,
                'x': Movement.RCC,
                'y': Movement.E,
                'z': Movement.RC}
