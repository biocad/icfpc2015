__author__ = 'pavel'

import argparse
import json
import sys

from board import Board

def load_board(json_string):
    d = json.loads(json_string)
    board = Board(d["id"], d["height"], d["width"], d["filled"], d["sourceLength"])
    print(board.__dict__, file=sys.stderr)
    return board

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
        s = "".join(s)
        print(play(load_board(s)))


if __name__ == "__main__":
    main()