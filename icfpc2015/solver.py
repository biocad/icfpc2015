__author__ = 'pavel'

import argparse
import json

def load_input():
    return {}


def play(input_dict):
    return "ctulhu"


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument('-f', dest='FILENAME', metavar='FILENAME', nargs='+', help='File containing JSON encoded input.')
    parser.add_argument('-t', dest='TIME', metavar='NUMBER', help='Time limit, in seconds, to produce output')
    parser.add_argument('-m', dest='MEMORY', metavar='NUMBER', help='Memory limit, in megabytes, to produce output')
    parser.add_argument('-p', dest='PHRASE', metavar='STRING', help='Phrase of power, as quoted string')
    return parser.parse_args()


def main():
    args = parse_args()
    print(args)


if __name__ == "__main__":
    main()