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

## 📂 Project Structure
src/
├── main/
│ ├── java/... # Source code
│ ├── resources/
│ ├── application.properties
│ └── static / templates (if any)
├── test/
│ ├── java/... # JUnit & Cucumber tests
│ └── resources/


---

## ⚡ Getting Started

### Prerequisites

- [Java 17+](https://adoptium.net/)
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
