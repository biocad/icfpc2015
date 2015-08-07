# -*- coding: utf-8 -*-
from icfpc2015 import util


class SearchProblem:
    """
    This class outlines the structure of a search problem, but doesn't implement
    any of the methods (in object-oriented terminology: an abstract class).

    You do not need to change anything in this class, ever.
    """

    def getStartState(self):
        """
        Returns the start state for the search problem.
        """
        raise NotImplemented

    def isGoalState(self, state):
        """
          state: Search state

        Returns True if and only if the state is a valid goal state.
        """
        raise NotImplemented

    def getSuccessors(self, state):
        """
          state: Search state

        For a given state, this should return a list of triples, (successor,
        action, stepCost), where 'successor' is a successor to the current
        state, 'action' is the action required to get there, and 'stepCost' is
        the incremental cost of expanding to that successor.
        """
        raise NotImplemented

    def getCostOfActions(self, actions):
        """
         actions: A list of actions to take

        This method returns the total cost of a particular sequence of actions.
        The sequence must be composed of legal moves.
        """
        raise NotImplemented


def depthFirstSearch(problem):
    """
    Search the deepest nodes in the search tree first.

    Your search algorithm needs to return a list of actions that reaches the
    goal. Make sure to implement a graph search algorithm.

    To get started, you might want to try some of these simple commands to
    understand the search problem that is being passed in:

    print "Start:", problem.getStartState()
    print "Is the start a goal?", problem.isGoalState(problem.getStartState())
    print "Start's successors:", problem.getSuccessors(problem.getStartState())
    """

    closed = []
    stack = util.Stack()
    stack.push((problem.getStartState(), []))

    while True:
        cell = stack.pop()
        if problem.isGoalState(cell[0]):
            return cell[1]
        if not cell[0] in closed:
            closed.append(cell[0])
            for item in problem.getSuccessors(cell[0]):
                stack.push((item[0], cell[1] + [item[1]]))


def breadthFirstSearch(problem):
    """Search the shallowest nodes in the search tree first."""
    closed = []
    queue = util.Queue()
    queue.push((problem.getStartState(), []))

    while True:
        cell = queue.pop()
        if problem.isGoalState(cell[0]):
            return cell[1]
        if not cell[0] in closed:
            closed.append(cell[0])
            for item in problem.getSuccessors(cell[0]):
                queue.push((item[0], cell[1] + [item[1]]))

def uniformCostSearch(problem):
    """Search the node of least total cost first."""
    closed = []
    queue = util.PriorityQueue()
    priority = 0
    queue.push((problem.getStartState(), [], 0), priority)

    while True:
        cell = queue.pop()
        if problem.isGoalState(cell[0]):
            return cell[1]
        if not cell[0] in closed:
            closed.append(cell[0])
            for item in problem.getSuccessors(cell[0]):
                cost = cell[2] + item[2]
                queue.push((item[0], cell[1] + [item[1]], cost), cost)

def nullHeuristic(state, problem=None):
    """
    A heuristic function estimates the cost from the current state to the nearest
    goal in the provided SearchProblem.  This heuristic is trivial.
    """
    return 0

def aStarSearch(problem, heuristic=nullHeuristic):
    """Search the node that has the lowest combined cost and heuristic first."""
    closed = []
    queue = util.PriorityQueue()
    priority = 0
    queue.push((problem.getStartState(), [], 0), priority)

    while True:
        cell = queue.pop()
        if problem.isGoalState(cell[0]):
            return cell[1]
        if not cell[0] in closed:
            closed.append(cell[0])
            for item in problem.getSuccessors(cell[0]):
                cost = cell[2] + item[2]
                queue.push((item[0], cell[1] + [item[1]], cost), cost + heuristic(item[0], problem))


# Abbreviations
bfs = breadthFirstSearch
dfs = depthFirstSearch
astar = aStarSearch
ucs = uniformCostSearch
