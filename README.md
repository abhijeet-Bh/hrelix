![Banner](docs/assets/HRelix-banner.png)

# HRelix - HR Management System

![Spring Boot Badge](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff&style=flat-square)
![Spring Security Badge](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=fff&style=flat-square)
![Spring Badge](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=fff&style=flat-square)
![OpenJDK Badge](https://img.shields.io/badge/OpenJDK-000?logo=openjdk&logoColor=fff&style=flat-square)
![PostgreSQL Badge](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=fff&style=flat-square)
![Docker Badge](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff&style=flat-square)

## Overview

This project is a backend service built using `Spring Boot` and `Java`. It exposes RESTful API endpoints for managing
resources in a `PostgreSQL database`. The application is containerized using `Docker`, enabling easy deployment and
scaling.

For Security of endpoints and app, This project implements `spring-security`. All endpoints (if required) are secured to
be accessed by only authentic users.

### System Architecture

![Banner](docs/assets/hrelix-arch.png)

### Documentation & Deployed Link

> Check out all the documentations of the endpoints from this link below :)

[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/38347451/2sAXqy4fXM)

> This project is hosted on AWS you can test it from this link below :)

[![Firefox](https://img.shields.io/badge/Firefox-FF7139?style=for-the-badge&logo=Firefox-Browser&logoColor=white)](http://35.154.121.96:8080/)

### Project Dependencies

- **Java**: `opne jdk: v22.0`.
- **Spring Boot**: `v3.3.3`.
- **PostgreSQL**: `v16.0`.
- **Docker**: with `docker-compose v3.0`.
- **Maven**: `v4.0.0`.

### Features

- RESTful API endpoints for CRUD operations.
- User authentication and authorization.
- Integration with PostgreSQL for persistent storage.
- Dockerized application for easy deployment.

## Getting Started

### 1. Clone this repository

```shell
https://github.com/abhijeet-Bh/hrelix.git
```

### 2. Build the application

   ```shell
   ./mvnw clean install
   ```

### 3. Running the Application

You can run the application using `Maven` or `Docker`.

#### A. Running with Maven:

1. **Run the Spring Boot application:**

   ```shell
   ./mvnw spring-boot:run
   ```

   The application will start on `http://localhost:8080`.


2. **Connect to the PostgreSQL database:**

   Ensure PostgreSQL is running and configured as per `application.properties`.

   ```.properties
   spring.config.import=optional:file:.env[.properties]
   spring.application.name=app
   spring.datasource.url=${PRODUCTION_DATABASE_URL}
   spring.datasource.username=${PRODUCTION_DATABASE_USERNAME}
   spring.datasource.password=${PRODUCTION_DATABASE_PASSWORD}
   spring.datasource.driver-class-name=org.postgresql.Driver
   
   ## Hibernate (JPA) Properties
   
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   spring.thymeleaf.prefix=classpath:/templates/
   spring.thymeleaf.suffix=.html
   spring.thymeleaf.mode=HTML
   spring.thymeleaf.encoding=UTF-8
   spring.thymeleaf.cache=false
   ```

---

#### B. Running with Docker:

1. **Build and run the Docker containers:**

   To run with docker-compose, you just need to setup `.env` file in the root directory as given below

   ```.env
   POSTGRES_DB=hrelix
   POSTGRES_USER=<db-user-name>
   POSTGRES_PASSWORD=<db-password>
   SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/hrelix
   SPRING_DATASOURCE_USERNAME=<db-user-name>
   SPRING_DATASOURCE_PASSWORD=<db-password>
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   
   #Production database
   PRODUCTION_DATABASE_URL=<db-url>
   PRODUCTION_DATABASE_USERNAME=<db-user-name>
   PRODUCTION_DATABASE_PASSWORD=<db-password>
   ```

   > *Now, you can run below command to run `HRelix` app :)*

   ```bash
   docker-compose up --build
   ```

This command will start the Spring Boot application and PostgreSQL database in Docker containers.

> **Note** :- *Since, all endpoints are secured. You'll need to create `admin` when you run this app for the first time.
To do that go to the `api/v1/admin/register` endpoint and create `Admin` first :)*

![Banner](docs/assets/home-screen.png)
> `http://localhost:8080/` will show you this screen

## API Documentation

### Endpoints

Here’s a list of all **API endpoints** for the **HR Management System**. These endpoints cover the main functionalities
for managing employees, leave requests, payroll, performance reviews, and attendance tracking.

### **1. Public Endpoints**

These endpoints handle public-related operations such as `health-check` and `login`.

| HTTP Method | Endpoint             | Description                                | Access Role |
|-------------|----------------------|--------------------------------------------|-------------|
| **GET**     | `/api/v1/healthz`    | Check running status of the backend        | OPEN        |
| **GET**     | `/api/v1/auth/login` | Login employee with `email` and `password` | OPEN        |

### **2. Admin Endpoints**

These endpoints handle admin-related operations such as `create-new-employee`, `change-employee-role` or
`delete-an-employee` etc.

| HTTP Method | Endpoint                                      | Description                                                                           | Access Role |
|-------------|-----------------------------------------------|---------------------------------------------------------------------------------------|-------------|
| **POST**    | `/api/v1/admin/register`                      | This is to create `admin` when running this for the first time.                       | OPEN        |
| **POST**    | `/api/v1/admin/create-new-employee-with-role` | This is to create `new-employee` with different roles  like `HR`, `EMPLOYEE`, `ADMIN` | ADMIN       |
| **POST**    | `/api/v1/admin/delete-employee/{id}`          | This is to delete and employee.                                                       | ADMIN       |
| **POST**    | `/api/v1/admin/update-role/{id}`              | This is to update roles of the employee.                                              | ADMIN       |

### **3. Employee Management Endpoints**

These endpoints handle employee-related operations such as creating, reading, updating, and deleting employee records.

| HTTP Method | Endpoint                     | Description                                                                                                         | Access Role |
|-------------|------------------------------|---------------------------------------------------------------------------------------------------------------------|-------------|
| **POST**    | `/api/v1/employees/register` | Create a new employee                                                                                               | ADMIN, HR   |
| **GET**     | `/api/v1/employees`          | Get a list of all employees *(this endpoint is open to all for testing but needs access toke, so login before use)* | OPEN        |
| **GET**     | `/api/v1/employees/{id}`     | Get a employee detail by employee id                                                                                | ADMIN, HR   |

> *This project is still under development and more endpoints and features will be updated.*

Thanks :)