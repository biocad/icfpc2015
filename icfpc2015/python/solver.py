# -*- coding: utf-8 -*-
import argparse
import json
import sys

import parser

from icfpc2015.python.game import Game

def play(board):
    return "ctulhu"


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('-f', dest='FILENAME', required=True, metavar='FILENAME', nargs='+', help='File containing JSON encoded input.')
    parser.add_argument('-t', dest='TIME', metavar='NUMBER', help='Time limit, in seconds, to produce output')
    parser.add_argument('-m', dest='MEMORY', metavar='NUMBER', help='Memory limit, in megabytes, to produce output')
    parser.add_argument('-p', dest='PHRASE', metavar='STRING', help='Phrase of power, as quoted string')
    return parser.parse_args()


def main():
    args = parse_args()
    print(args, file=sys.stderr)
    for filename in args.FILENAME:
        with open(filename, "rt") as fd:
            s = fd.readlines()
        json_data = json.loads("".join(s))
        board = parser.parse_board(json_data)
        units = parser.parse_units(json_data)
        game = Game(board=board, units=units)
        print(game.dumps())


if __name__ == "__main__":
    main()