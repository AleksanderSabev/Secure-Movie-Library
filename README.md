# Spring Boot Project (MovieLibraryAPI)

## Overview

MovieLibraryAPI is a Spring Boot REST API designed for managing users and a curated movie catalog. Users can register, browse movies, and their roles can be updated or accounts deleted by administrators. Administrators have full control to create, update, and delete movies, with ratings automatically fetched and updated asynchronously from the external OMDb API.

---

## ✨ Features

* **User Authentication**: Sign-up, login, and secure password storage using BCrypt.

* **Content Moderation**: Admin control for adding, updating, and deleting movies, as well as managing user accounts.
* **Asynchronous Enrichment**: Movie ratings are fetched and updated from the OMDb API in the background.
* **Swagger Integration**: Auto-generated interactive API documentation for easy testing and exploration.

---

## Technologies Used

* Java 17
* Spring Boot (REST endpoints)
* Spring Security (Authentication and role-based access control)
* BCrypt (Password hashing)
* Hibernate / JPA (Object-relational mapping)
* MariaDB (Relational database)
* springdoc-openapi / Swagger UI (Interactive API documentation)
* Gradle (Build automation)
* JUnit / Mockito (Unit testing)

---

## 🔒 Authentication & Authorization Implementation

The MovieLibraryAPI implements authentication and authorization using **Spring Security** and **JSON Web Tokens (JWTs)**, following a standard, stateless REST API approach. 

* **User Registration and Login:**
    * **Registration:** Users send their credentials (`LoginDto`/`RegisterDto`) to the `AuthController`. Passwords are secured using **BCrypt** hashing before storage.
    * **Login:** Upon successful login, the `JwtService` generates a **JWT**.
    * **Token Issuance:** The JWT is returned to the client, which stores it (e.g., in local storage). This token contains the user's identity and permissions (roles).

* **Authorization Flow:**
    * **`JwtAuthenticationFilter`:** This custom filter is the core of the security process. For every subsequent request (to `/api/movies`, `/api/admin`, etc.), this filter intercepts the request header to extract and validate the JWT.
    * **Token Validation:** The `JwtService` validates the token's signature, expiry, and integrity.
    * **Context Setup:** If the token is valid, Spring Security's context is populated with the user details (username and **Role**).
    * **Access Control:** The `SecurityConfig` then uses the role (e.g., `ROLE_ADMIN`, `ROLE_USER`) to enforce access rules on specific endpoints.

---

## 🔄 How Asynchronous Enrichment Works

The application uses **asynchronous processing** to enhance movie data by fetching external ratings without delaying the user's initial request. 

* **Trigger Mechanism:** When an administrator successfully adds a new movie via the `MovieController`, the `MovieService` is triggered.
* **Asynchronous Call:** Instead of immediately calling the external OMDb API, the `MovieService` delegates this task to the `MovieRatingEnrichmentService`. This service method is annotated with `@Async` (configured in the `AsyncConfig`), immediately returning control to the client.
* **Background Processing:** The task of fetching the rating is executed by a separate thread managed by Spring's built-in Task Executor.
* **External API Integration:** The `MovieRatingEnrichmentService` uses the `WebClient` (configured in `WebClientConfig`) to perform the non-blocking HTTP request to the OMDb API, retrieving the rating data (`OmdbResponse`).
* **Update:** Once the rating is successfully retrieved, the service updates the corresponding `Movie` entity in the database.

---

## 📐 Architectural Decisions or Trade-offs

| Decision/Component | Rationale and Benefit | Trade-off/Complexity |
| :--- | :--- | :--- |
| **Stateless Security (JWT)** | **Benefit:** Highly scalable and well-suited for REST APIs. Eliminates the need for server-side session management. | **Trade-off:** Requires careful handling of token expiration and revocation (though revocation isn't explicitly detailed, it adds complexity). |
| **Asynchronous Enrichment** | **Benefit:** Improves user experience by ensuring the movie creation endpoint is fast (low latency). Heavy, long-running tasks (external HTTP calls) do not block the main thread. | **Trade-off:** Increased complexity in error handling and debugging. Failures in the async thread must be logged or handled separately as they cannot return an error directly to the initial request. |
| **External Service Layer (Contracts/Impl)** | **Benefit:** Clear separation of business logic from framework details, making the code more modular and testable (e.g., `MovieService` vs. `MovieServiceImpl`). | **Trade-off:** Adds an extra layer of abstraction which can slightly increase boilerplate code, though this is considered good practice for enterprise applications. |
| **BCrypt for Passwords** | **Benefit:** Industry-standard, secure password hashing. | **Trade-off:** Hashing takes more CPU time than simpler methods, but this is an acceptable and necessary trade-off for security. |

## 🚀 Installation

### Prerequisites

* Java 17
* Gradle
* MariaDB
* Git

### Steps

Follow these steps to set up and run the application:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/AleksanderSabev/Secure-Movie-Library
    ```
2.  **Setup Database:** Create a MariaDB database and run the SQL scripts from the `db` folder.
3.  **Configure:** Update `application.properties` with your DB credentials:
    ```properties
    spring.datasource.url=jdbc:mariadb://localhost:3306/movierater
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    ```
4.  **Build the project:**
    ```bash
    ./gradlew build
    ```
5.  **Run the application:**
    ```bash
    ./gradlew bootRun
    ```
6.  **Access Documentation:** Open Swagger UI at: `http://localhost:8080/swagger-ui.html`

<br>

<br>

## Solution Structure

The solution is organized into the following core components, adhering to a standard layered architecture:

```
.
.
├── build.gradle
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── example
    │   │           └── movielibraryapi
    │   │               ├── config
    │   │               ├── controllers
    │   │               ├── enums
    │   │               ├── exceptions
    │   │               ├── helpers
    │   │               ├── models
    │   │               │   └── dtos
    │   │               │       └── auth
    │   │               ├── repositories
    │   │               ├── security
    │   │               └── services
    │   │                   ├── auth
    │   │                   ├── contracts
    │   │                   └── impl
    │   └── resources
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── org
                └── example
                    └── movielibraryapi
                        └── services
                            ├── auth
                            └── impl
```

* **`controllers`**: Contains the REST endpoints (`@RestController`) for authentication (`AuthController`) and movie management (`MovieController`).
* **`services`**: Holds the business logic and interfaces (e.g., `UserService`, `MovieService`) and their implementations.
* **`security`**: Contains the security-specific logic, including the JWT filter (`JwtAuthenticationFilter`) and `JwtService`.
* **`repositories`**: Holds the JPA/Hibernate interfaces for data access (e.g., `UserRepository`, `MovieRepository`).
* **`models`**: Contains the JPA Entity classes (`User`, `Movie`), DTOs, and the `Role` enum.
* **`config`**: Contains application configuration, including `SecurityConfig` (defining the filter chain and access rules).

---

## Contributors

For further information, please feel free to contact us:

| Author | Emails | GitHub |
| :--- | :--- | :--- |
| Aleksandar Sabev | aleksandars05@abv.bg | AleksanderSabev |

---