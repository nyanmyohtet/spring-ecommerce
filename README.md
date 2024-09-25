# Spring Boot E-Commerce API with Oracle Database

Spring Boot E-Commerce API with Oracle Database

[![wakatime](https://wakatime.com/badge/user/232239bd-b752-4918-a368-ffb08deb9822/project/d420c842-a68b-470f-966a-4c10b1e71feb.svg)](https://wakatime.com/badge/user/232239bd-b752-4918-a368-ffb08deb9822/project/d420c842-a68b-470f-966a-4c10b1e71feb)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nyanmyohtet_spring-ecommerce&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=nyanmyohtet_spring-ecommerce)

ref: https://container-registry.oracle.com/ords/f?p=113:4:7017560529758:::4:P4_REPOSITORY,AI_REPOSITORY,AI_REPOSITORY_NAME,P4_REPOSITORY_NAME,P4_EULA_ID,P4_BUSINESS_AREA_ID:803,803,Oracle%20Database%20Express%20Edition,Oracle%20Database%20Express%20Edition,1,0&cs=34F4ZUpbk6kueA-wlN8oJ0ojpiff2eJ0iWh6Hupmx6ifnJ7ZHV64JzzNnz-PIRwHGJrIeeOGhs1KgUD0GO6iFSg

## Tech Stack

Oracle Database Express:21.3.0-xe

## SpringFox - Swagger

- http://localhost:8080/v2/api-docs
- http://localhost:8080/swagger-ui/

ref: https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api

## ER Diagram Explanation:

The following key entities were modeled for the e-commerce platform:

1. **User**: Represents a registered customer with basic profile information like `username`, `password`, `email`, and `firstName`. Users can have multiple orders but only one active cart.

2. **Product**: Represents products available for purchase, with attributes like `name`, `description`, `price`, and `quantityInStock`. This entity is related to both `OrderItem` and `CartItem`.

3. **Cart**: Represents a user's shopping cart. Each user has one cart, and a cart can have multiple `CartItems`.

4. **CartItem**: Represents a specific product and its quantity added to the cart. It is linked to a `Product` and belongs to a `Cart`.

5. **Order**: Represents a completed purchase made by a user. An order is associated with multiple `OrderItems` and a single user. The order tracks total price and status (`PLACED`, `CANCELLED`, or `COMPLETED`).

6. **OrderItem**: Represents a specific product and its quantity in an order. It is linked to a `Product` and belongs to an `Order`.

### Entity Relationship:

User ↔ Cart: One-to-One. A user has one `cart` at a time.

User ↔ Order: One-to-Many. A user can have multiple `orders`.

Cart ↔ CartItem: One-to-Many. A cart can contain multiple `items`.

Order ↔ OrderItem: One-to-Many. An order can contain multiple `items`.

Product: Many-to-One with both `CartItem` and `OrderItem`.

![image](https://github.com/user-attachments/assets/1c4a845a-a264-4d58-9f3b-0570f4675993)

---

## 3. **Code Explanation**

### **Code Structure:**

1. **Entities (Model Layer)**:
   - The **entities** represent the database tables and are defined in the `model` package.
   - Entities include `User`, `Product`, `Cart`, `CartItem`, `Order`, and `OrderItem`.
   - Relationships between entities are defined using JPA annotations like `@OneToMany`, `@ManyToOne`, and `@OneToOne`.

2. **Repository Layer**:
   - The `repository` package contains interfaces that extend `JpaRepository`. Each repository corresponds to an entity (e.g., `UserRepository`, `ProductRepository`, `CartRepository`).
   - These repositories provide CRUD operations without the need to write boilerplate code.

3. **Service Layer**:
   - The `service` package contains service classes (e.g., `UserService`, `ProductService`, `CartService`, `OrderService`).
   - Each service implements the business logic and uses repositories to interact with the database.
   - **Constructor injection** is used in services with the help of **Lombok's `@RequiredArgsConstructor` annotation**, improving testability and reducing boilerplate code.

4. **Controller Layer**:
   - The `controller` package contains REST controllers that expose API endpoints to handle incoming HTTP requests.
   - Controllers like `UserController`, `ProductController`, `CartController`, and `OrderController` handle user registration, product management, cart management, and order processing.
   - Swagger UI documentation is automatically generated for each controller.

5. **Exception Handling**:
   - Custom exceptions are defined in the `exception` package.
   - A global exception handler is implemented using `@ControllerAdvice` to return proper HTTP responses when an error occurs (e.g., `ResourceNotFoundException` for missing entities).

6. **DTO (Data Transfer Objects)**:
   - The `dto` package contains data transfer objects used to send and receive data between client and server without exposing the internal entity structure.

7. **Security**:
   - Basic JWT-based security is implemented to handle user authentication and protect endpoints.
   - This can be expanded to include role-based access control (RBAC) if needed.

### Key Components:
- **Swagger Integration**: The `springdoc-openapi` library is used to automatically generate and serve API documentation. You can access it via `/swagger-ui.html` to test the API directly.
  
- **Database Configuration**: The database connection is set up in `application.properties`, which contains the Oracle DB credentials and Hibernate properties.

## How to Run the Project

### Prerequisites:

JDK: 11

Maven: 3.9.8

Oracle Database: See Oracle Database setup

Swagger: available at http://localhost:8080/swagger-ui/

Clone the repository.

Configure the database in application.properties.

Run `mvn clean install` to install dependencies.

Run `mvn spring-boot:run` to start the application.

Access the Swagger at http://localhost:8080/swagger-ui/.

## Setup Oracle DB using Docker

```shell

docker compose up -d

docker exec -it oracle-db sqlplus sys/password@XE as sysdba

SHOW PDBS;
    CON_ID CON_NAME                       OPEN MODE  RESTRICTED
---------- ------------------------------ ---------- ----------
         2 PDB$SEED                       READ ONLY  NO
         3 XEPDB1                         READ WRITE NO

ALTER SESSION SET CONTAINER = XEPDB1;

CREATE USER SPRINGBOOT IDENTIFIED BY password;

GRANT ALL PRIVILEGES TO SPRINGBOOT;
```
