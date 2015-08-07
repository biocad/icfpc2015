__author__ = 'pavel'

import sys
from cell import Cell
from board import Board
from unit import Unit

def parse_cell(json_cell):
    return Cell(x=json_cell['x'], y=json_cell['y'])


def parse_board(json_data):
    board = Board(json_data["id"], json_data["height"],
                  json_data["width"], json_data["filled"],
                  json_data["sourceLength"], json_data["sourceSeeds"])
    print(board.__dict__, file=sys.stderr)
    return board


def parse_unit(json_unit):
    return Unit(list(map(parse_cell, json_unit["members"])), parse_cell(json_unit["pivot"]))


def parse_units(json_data):
    return list(map(parse_unit, json_data["units"]))