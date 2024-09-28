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

### Documentation
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://documenter.getpostman.com/view/38347451/2sAXqy4fXM)


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
   mvn clean install
   ```

### 3. Running the Application

You can run the application using `Maven` or `Docker`.

#### A. Running with Maven:

1. **Run the Spring Boot application:**

   ```shell
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`.

2. **Connect to the PostgreSQL database:**

   Ensure PostgreSQL is running and configured as per `application.properties`.

#### B. Running with Docker:

1. **Build and run the Docker containers:**

   ```bash
   docker-compose up --build
   ```

   This command will start the Spring Boot application and PostgreSQL database in Docker containers.


## API Documentation

### Endpoints
Here’s a list of all **API endpoints** for the **HR Management System**. These endpoints cover the main functionalities for managing employees, leave requests, payroll, performance reviews, and attendance tracking.

### **1. Public Endpoints**

These endpoints handle public-related operations such as `health-check` and `login`.

| HTTP Method | Endpoint             | Description                                | Access Role |
|-------------|----------------------|--------------------------------------------|-------------|
| **GET**     | `/api/v1/healthz`    | Check running status of the backend        | OPEN        |
| **GET**     | `/api/v1/auth/login` | Login employee with `email` and `password` | OPEN        |


### **2. Employee Management Endpoints**

These endpoints handle employee-related operations such as creating, reading, updating, and deleting employee records.

| HTTP Method | Endpoint                     | Description                                  | Access Role                |
|-------------|------------------------------|----------------------------------------------|----------------------------|
| **POST**    | `/api/v1/employees/register` | Create a new employee                        | ADMIN, HR                  |
| **GET**     | `/api/v1/employees`          | Get a list of all employees                  | ADMIN, HR                  |

> This project is still under development and more endpoints and features will be updated.

Thanks :)