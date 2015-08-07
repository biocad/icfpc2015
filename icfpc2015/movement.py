__author__ = 'pavel'

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

BACKWARD_MAP = {v : key for v in value for key, value in FORWARD_MAP.items()}