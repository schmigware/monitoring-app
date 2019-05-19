from __future__ import print_function

import argparse
import datetime
import sys
import time
import uuid

from kafka import KafkaProducer
from json import dumps

############################################################
# event generation:
# python generator.py -b bootstrap.kafka:9092 -t source -n5
############################################################

def init_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument('--bootstrap-server', '-b', help='Kafka bootstrap server URI', dest="server", required=True)
    parser.add_argument('--topic', '-t', help='Kafka topic name', required=True)
    parser.add_argument('-n', help='Messages per second', dest="messages_per_second", type=int, required=True)
    parser.add_argument('-max', help='Max messages', dest="max_messages", type=int, default=float("inf"))
    return parser

parser = init_parser()
args = parser.parse_args()
bootstrap_server = args.server
topic_name = args.topic
messages_per_second = args.messages_per_second
max_messages = args.max_messages

if(messages_per_second < 1):
    print('Invalid message count {}'.format(messages_per_second))
    sys.exit()

print('Initialising producer...')
producer = KafkaProducer(bootstrap_servers=[bootstrap_server],
                         value_serializer=lambda x:
                         dumps(x).encode('utf-8'))
print('Done.')

sleep_time = 1.0 / messages_per_second

print('Sending messages', end='')
sys.stdout.flush()

count = 0
try:
    while count < max_messages:
            
        data = {
            'payload': 'payloadContent',
            'uuid': str(uuid.uuid4()),
            'timestamp': str(datetime.datetime.utcnow())
        }
        producer.send(topic_name, value=data)
        count = count + 1
        print('.', end="")
        sys.stdout.flush()
        time.sleep(sleep_time)
    print()

except KeyboardInterrupt:
    print()

print('Sent {} message(s).'.format(count))