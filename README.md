# Enterprise System TDP024

## Overview

This project is an enterprise-level Service-Oriented Architecture (SOA) system designed for the TDP024 course. It integrates multiple microservices written in different languages to provide a comprehensive banking solution. The system includes:

- A Java-based core service for the main banking operations.
- A Kotlin-based API for handling bank-specific logic.
- A Python-based API for managing personal information.

The architecture is built using Docker to ensure easy deployment and scalability, and it utilizes Apache Kafka for asynchronous communication between services.

## Prerequisites

- ### Docker
  Docker is required to run this project. You will need either Docker Desktop or access to the Docker CLI to execute and manage the services.

- ### Java, Kotlin, Python
  The system requires Java (version 11 or higher), Kotlin (version 1.5 or higher), and Python (version 3.8 or higher) for respective service implementations.

- ### Maven
  Apache Maven is used for managing dependencies and building the Java service.

## Running the Project

To run this project, navigate to the `project/` directory, where you will run the Docker containers.

To start the project, run:

```bash
$ docker compose up --build
```

In some cases, if the Docker Compose doesn't start the project correctly, you may need to run:

```bash
$ docker compose down
```

and then rerun the previous command.

### Running Tests

To run the tests for the project, open a new terminal and head to the `project/` directory once again.

From there, execute:

```bash
$ docker exec -it project-java-1 mvn test
```

The tests will run, and the coverage files will be copied into the `test-logs` directory, which can be accessed from the `project/` directory.

### Common Errors

- **Kafka Startup Issues:**  
  If you encounter problems starting Kafka with the Docker script, it may be due to insufficient permissions for the `kafka-data` directory. You can resolve this by adjusting permissions with:

  ```bash
  $ chmod -R r+w kafka-data/
  ```

## Kafka Consumer

To run the Kafka consumer, navigate to the `project/kafka-consumer` directory:

```bash
$ cd project/kafka-consumer
```

Then activate the virtual environment and install the required dependencies:

```bash
$ source bin/activate
$ pip install -r requirements.txt
```

You are now ready to run the Kafka consumer with:

```bash
$ python kafkaconsumer.py
```

The Kafka consumer listens for messages from the banking system and processes user activities asynchronously.

## Project Structure

The project consists of several core components:

1. **Java Banking Service**  
   The main service that handles transactions, account management, and business logic.

2. **Kotlin Bank API**  
   A supporting API that interacts with the core banking service, providing specialized bank-related operations.

3. **Python Person API**  
   This service is responsible for managing user information and integrates seamlessly with the Java service to provide comprehensive user profiles.

4. **Kafka**  
   Kafka serves as the backbone for communication between services, enabling event-driven architecture and decoupling of microservices.

## API Documentation

### Java Banking Service
- **Base URL:** `:8080/account-restful`
- **Endpoints:**
  - **`POST account/create:`** Create a new bank account.
  - **`GET account/find/person?person={person}:`** Retrieve account details by person.
  - **`POST account/debit:`** Debit an account.
  - **`POST account/credit:`** Credit an account.
  - **`GET account/transactions?id={id}:`** Get account transactions.

### Kotlin Bank API
- **Base URL:** `:8070/`
- **Endpoints:**
  - **`GET /list:`**: Retrieve list of banks.
  - **`GET /find.name/?name={name}:`** Get id of a specific bank from the name.
  - **`GET /find.id/?id={id}:`** Get a specific bank with id.

### Python Person API
- **Base URL:** `:8060/`
- **Endpoints:**
  - **`'GET /list:`** Create a new user profile.
  - **`GET /find.name/?name={name}:`** Retrieve a list of users with the name.
  - **`GET /find.id/?id={id}:`** Retrieve a specfic person with id

## Development Notes

- Ensure Docker is installed and accessible from your command line before attempting to run the system.
- The services communicate asynchronously via Kafka, so make sure Kafka is running and healthy.
- All APIs are RESTful and adhere to consistent naming conventions and response formats.
- The project uses Maven as a build tool for the Java service, Gradle for Kotlin, and `pip` for Python dependencies.

## Troubleshooting

- **Service Timeout:** If a service fails to start, it could be due to a misconfiguration in Docker networking. Ensure each service has a unique name and appropriate network configuration.
- **Virtual Environment Errors:** When activating the virtual environment for the Kafka consumer, if you encounter `command not found`, ensure you are in the correct directory and that `bin/activate` is present.

## Additional Resources

- ### Course Materials
  Refer to the TDP024 course webpage for further reading on SOA principles and best practices.

- ### Docker Documentation
  For more information on Docker and Docker Compose, visit [Docker's official documentation](https://docs.docker.com/).

- ### Kafka Quickstart
  Apache Kafka documentation is available at [Kafka Documentation](https://kafka.apache.org/documentation/).

## Future Improvements

- **Monitoring**: Add monitoring capabilities using Prometheus and Grafana to observe service metrics in real time.
- **Scaling**: Implement Kubernetes for better scalability and orchestration of microservices.
- **API Gateway**: Consider adding an API gateway like Kong or Zuul for better routing and API management.

## Contributing

Feel free to fork this project and create a pull request. Any contributions that help improve the codebase or enhance functionality are welcome.
