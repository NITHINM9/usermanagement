# User Management API

A **Spring Boot** application providing secure user registration, authentication, and admin operations with:
- **Java + Spring Boot**
- **JUnit & Cucumber** for testing
- **Swagger** for documentation
- **Docker** for containerized deployment

---

## 📌 Features

1. **User Registration**
   - Stores name, email, gender, password.
   - Enforces unique email addresses.
   - Captures **IP address** via [ipify.org](https://www.ipify.org/).
   - Captures **Country** via [ip-api.com](http://ip-api.com/).

2. **User Validation**
   - Validate email & password.

3. **Security**
   - Basic Authentication for all APIs.
   - Role-based access control (`ADMIN`, `USER`).

4. **Admin-only APIs**
   - View all registered users.
   - Delete a user by email.

5. **Testing**
   - **JUnit** for service layer unit tests.
   - **Cucumber** for BDD-style API tests.

6. **API Documentation**
   - Swagger UI for testing & documentation.
   - <img width="1896" height="867" alt="image" src="https://github.com/user-attachments/assets/ea8f0b1f-99ff-4c79-b191-7db7f73d286a" />

   - <img width="1896" height="877" alt="image" src="https://github.com/user-attachments/assets/92cdf322-1386-41e9-a105-442ca6fef8e7" />

   -<img width="1901" height="864" alt="image" src="https://github.com/user-attachments/assets/e756747d-cfe1-43a9-a549-0b3c73eebbb9" />
   

7. **Docker Support**
   - Easily build & run in a container.

---

## 🛠 Tech Stack

- **Java 17+**
- **Spring Boot 3+**
- **Maven**
- **JUnit 5**
- **Cucumber**
- **Swagger (springdoc-openapi)**
- **Docker**

---

## ⚡ Getting Started

### Prerequisites

- Java 17+
- [Maven](https://maven.apache.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recommended)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (for container run)

---

# Build project
mvn clean install

# Run Spring Boot app
mvn spring-boot:run

Access API at:
http://localhost:8080

Swagger UI:
http://localhost:8080/swagger-ui.html

🐳 Run with Docker

1️⃣ Build Docker Image

docker build -t usermanagement .

2️⃣ Run Container

docker run -p 8080:8080 usermanagement
