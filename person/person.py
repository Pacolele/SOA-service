import random
import string
import uuid
from flask import Flask, jsonify, request
from flask_restful import Api, Resource
import redis
import json
import os
from datetime import datetime
from confluent_kafka import Producer

app = Flask(__name__)
api = Api(app)
redis_host = os.getenv('REDIS_HOST', 'localhost')
redis_port = int(os.getenv('REDIS_PORT', 6379))
redis_server = redis.StrictRedis(host=redis_host, port=redis_port)

#KAFKA
kafka_topic = 'api_topic'
kafka_producer = Producer({
    'bootstrap.servers': 'kafka:9094',
    'group.id': 'python_producer'
})

class FindByKey(Resource):
    def get(self):
        key = request.args.get('key')
        dbKey = redis_server.hget(key, 'name')
        if dbKey != None:
            format_kafka_message("Succesfully executed /find.key?key=key, found person")
            return "Person found", 200
        else:
            format_kafka_message("Succesfully executed /find.key?key=key, no person found")
            return None, 200

class FindByName(Resource):
    def get(self):
        name = request.args.get('name')
        persons = get_persons_from_redis(name=name)
        format_kafka_message("Succesfully called python /find.name")
        return persons, 200

class Persons(Resource):
    def get(self):
        persons = get_persons_from_redis()
        format_kafka_message("Succesfully called python /list")
        return persons, 200


def get_persons_from_redis(name=None):
    persons = []
    keys = redis_server.keys('*')
    for key in keys:
        person_name = redis_server.hget(key, 'name').decode('utf-8')
        if name is None or person_name == name:
            persons.append({'key': key.decode('utf-8'), 'name': person_name})
    return persons

def format_kafka_message(message):
    now = datetime.now()
    time = now.strftime("%Y-%m-%d %H:%M:%S")
    key = f"[PYTHON]"
    value = f"[{time}] <KAFKA INFO> ${message}"
    kafka_producer.produce(kafka_topic, key=key, value=value)
    kafka_producer.flush()


def get_unique_key():
    return int(redis_server.dbsize() +1 )

def setup_redis():
    """Function to populate Redis with initial data at startup."""
    redis_server.flushdb()
    persons = [
        {'name': 'Jakob Pogulis'},
        {'name': 'Xena'},
        {'name': 'Xena'},
        {'name': 'Marcus Bendtsen'},
        {'name': 'Zorro'},
        {'name': 'Q'},
        {'name': 'Q'}
    ]
    # Storing the data in Redis
    for person in persons:
        # Stores person with unique key
        redis_server.hset(get_unique_key(), 'name', person['name'])
        # Allows multiple users with same name

api.add_resource(Persons, '/list')
api.add_resource(FindByName, '/find.name')
api.add_resource(FindByKey, '/find.key')

if __name__ == '__main__':
    setup_redis()
    app.run(host="0.0.0.0", port="8060")
