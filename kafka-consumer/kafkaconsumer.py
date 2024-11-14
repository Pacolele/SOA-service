import os
from confluent_kafka import Consumer, KafkaException, KafkaError

def kafka_consumer(topic, group_id="kafka_consumer"):
    config = {
        'bootstrap.servers': "localhost:9092",
        'group.id': group_id,
        'auto.offset.reset': 'earliest'
    }
    consumer = Consumer(config)
    consumer.subscribe([topic])
    flag = True
    print(f"Kafka listening to topic: {topic}")
    try:
        while True:
            msg = consumer.poll(20.0)

            if msg is None:
                print("No message received within the timeout period. Returning to topic selection...")
                flag = False
                break
            elif msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    print("Reached end of partition.")
                else:
                    raise KafkaException(msg.error())
            else:
                print(f"Received message from topic '{msg.topic()}': {msg.value().decode('utf-8')}")
    except KeyboardInterrupt:
        print("Consumer interrupted, closing...")
    finally:
        consumer.close()

if __name__ == '__main__':
    topics = ['transactions_topic', 'rest_request_topic', 'api_topic']

    while True:
        print("\nSelect a topic to consume:")
        for i, topic in enumerate(topics, 1):
            print(f"{i}. {topic}")
        print("4. Exit")

        choice = input("Enter the number of the topic you want to consume: ")

        if choice == '4':
            print("Exiting...")
            break

        try:
            choice = int(choice)
            print(choice)
            if 1 <= choice <= 3:
                kafka_consumer(topics[choice - 1])
            else:
                print("Invalid choice, please enter a number between 1 and 4.")
        except ValueError:
            print("Invalid input, please enter a valid number.")
        except Exception:
            print("Something went wrong with kafka consume")
