### Link to Game service : 
https://github.com/amirkpatna1/gameService

# Leaderboard Service

## Overview
The Leaderboard Service is designed to manage player scores and ranks for various games. It leverages Redis for fast score retrieval and ranking, Kafka for real-time score updates, and a combination of custom ranking strategies to handle different ranking requirements.

## Features
- Real-time score updates using Kafka.
- Storage and retrieval of player scores using Redis.
- Customizable ranking strategies.
- RESTful API for interacting with the leaderboard.

## Technologies Used
- Spring Boot
- Spring Data Redis
- Spring Kafka
- Hibernate/JPA
- MySQL
- Gson
- Lombok

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven
- Redis
- Kafka
- MySQL

### Configuration
Configure the following properties in `application.properties`:

```properties
# Kafka properties
spring.kafka.bootstrap-servers=your_kafka_bootstrap_server
spring.kafka.consumer.group-id=your_consumer_group
kafka.score-topic=your_score_topic

# Redis properties
spring.redis.host=localhost
spring.redis.port=6379

# Database properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password

# Auth properties
auth.secret-key=your_secret_key

# Strategy properties
strategy.tie-break-strategy=defaultTieBreakStrategy
strategy.ranking-strategy=scoreRankingStrategy
```
Building and Running

1.  Clone the repository:
        git clone https://github.com/your-repo/leaderboard_service.git
        cd leaderboard_service

2.	Build the project:
        mvn clean install
3.	Run the application:
        mvn spring-boot:run
API Endpoints

Get Top K Scores

Retrieve the top K scores for a specific game.

	•	URL: /leaderboard/v1/score/{gameId}
	•	Method: GET
	•	Parameters:
	•	gameId (path): The ID of the game.
	•	k (query): The number of top scores to retrieve.
	•	Response:
	•	200 OK: A list of top K scores.

Error Handling

The service uses a global exception handler to manage errors and provide meaningful messages to the clients.

Code Structure

	•	config: Configuration classes for Kafka, Redis, and other beans.
	•	controller: REST controllers to handle API requests.
	•	dto: Data Transfer Objects.
	•	entity: JPA entities.
	•	exception: Custom exceptions.
	•	interceptor: Interceptors for request handling.
	•	repository: JPA repositories.
	•	service: Business logic services.
	•	strategy: Ranking and tie-break strategies.

Running Tests

Unit tests are located in the src/test/java directory. To run the tests, use:
mvn test

Extending the Service

Adding a New Ranking Strategy

	1.	Implement the RankingStrategy interface.
	2.	Define the new strategy as a Spring bean in StrategyConfig.
	3.	Update application.properties to use the new strategy.

Adding a New Tie-Break Strategy

	1.	Implement the TieBreakStrategy interface.
	2.	Define the new strategy as a Spring bean in StrategyConfig.
	3.	Update application.properties to use the new strategy.

Contributing

Feel free to fork the repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.



Contact

For any inquiries, please contact amirkpatna@gmail.com .
