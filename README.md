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
- JUnit 5
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

_Additional endpoints will be introduced as development progresses._

## Progress

Currently implementing ingredient and inventory management using REST APIs and temporary in-memory storage.

Implemented functionality includes ingredient creation and retrieval, stock replenishment, input validation, global exception handling and automated tests.

Planned next steps include stock consumption, PostgreSQL persistence and product and order management.

*Work in progress.*
