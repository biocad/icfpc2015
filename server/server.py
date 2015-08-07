from datetime import timedelta
from functools import update_wrapper
import json
import random

from flask import Flask
from flask import jsonify
from flask import request
from flask import make_response
from flask import current_app

from werkzeug.contrib.fixers import ProxyFix

app = Flask(__name__)
app.wsgi_app = ProxyFix(app.wsgi_app)
app.config['PROPAGATE_EXCEPTIONS'] = True
game = None

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):
    basestring = (str, bytes)

    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, basestring):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, basestring):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods
        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp

            h = resp.headers

            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp

        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator


@app.route('/field', methods=['GET'])
@crossdomain(origin='*')
def get_field():
    with open('/Users/roman/Projects/Biocad/biocad-icfpc/icfpc2015/problems/problem_0.json') as fp:
        json_data = json.load(fp)
        board = parse_board(json_data)
        units = parse_units(json_data)
        game = Game(board=board, units=units)

    return jsonify({'height': 20,
                    'width': 10,
                    'colored': []
                    })

@app.route('/move', methods=['GET'])
@crossdomain(origin='*')
def get_move():
    code = request.args.get('code')
    with open('/Users/roman/Projects/Biocad/biocad-icfpc/icfpc2015/problems/problem_0.json') as fp:
        json_data = json.load(fp)
        board = parse_board(json_data)
        units = parse_units(json_data)
        game = Game(board=board, units=units)
    rand_cells = [{'posX': random.randint(0, 10),
                   'posY': random.randint(0, 20),
                   'state': 'active' if bool(random.getrandbits(1)) else 'disabled'}
                  for _ in range(20)]
    response = {'height': 20,
                    'width': 10,
                    'colored': rand_cells,
                    'score': random.randint(1, 10000000)
                    }
    end = {'end': True}
    result = end if random.randint(1, 100) == 1 else response
    return jsonify(result)

if __name__ == '__main__':
    app.run(debug=True)
