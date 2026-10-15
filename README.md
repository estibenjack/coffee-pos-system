# Coffee Shop POS and Inventory Management System

A Java and Spring Boot backend project for a coffee shop point-of-sale (POS) and inventory management system.

## About

I've previously covered some Spring Boot through online courses, but wanted to revisit the fundamentals and put what I've learned into practice through a project of my own, building on my existing Java and OOP experience.

I'm developing the application iteratively, starting with simple implementations to revisit core Spring Boot concepts before gradually introducing more complex functionality. The initial focus is ingredient and inventory management, with plans to expand into products and order processing.

Along the way, I'm looking to strengthen my understanding of REST APIs, application design, business logic and testing.

## Tech Stack

- Java 21
- Spring Boot
- Maven
- Jakarta Bean Validation
- JUnit 5 / MockMvc
- Springdoc OpenAPI / Swagger UI

## Design

The initial ERD outlines the proposed data model for the application and will evolve as development progresses.

![Coffee Shop POS ERD](docs/coffee_pos_erd.drawio.png)

## API Documentation

Interactive API documentation and testing are available through Swagger UI, using Springdoc OpenAPI.

To explore the API, run the Spring Boot application locally and navigate to:

http://localhost:8080/swagger-ui.html

![Swagger UI API Documentation](docs/swagger-ui.png)

### Current Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/ingredients` | Retrieve all ingredients |
| GET | `/api/ingredients/{id}` | Retrieve an ingredient by ID |
| POST | `/api/ingredients` | Create a new ingredient |
| POST | `/api/ingredients/{id}/replenish` | Replenish ingredient stock |
| POST | `/api/ingredients/{id}/consume` | Consume ingredient stock |

_Additional endpoints will be introduced as development progresses._

## Progress

The initial ingredient management functionality is complete, using REST APIs and temporary in-memory storage.

Implemented features include ingredient creation and retrieval, stock replenishment and consumption, business rule enforcement, request validation, centralised exception handling and automated testing.

Next steps include introducing PostgreSQL persistence with Spring Data JPA, followed by product, recipe and order management.

*Work in progress.*
