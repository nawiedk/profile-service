# Profile Service

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

## 📖 Overview

**Profile Service** is a microservice designed for managing personal and team member profiles. It is built using **Spring Boot** and follows the **Hexagonal Architecture** (Ports and Adapters) pattern to ensure a clean separation between domain logic and external infrastructure.

This service acts as a core component in a distributed system, handling dynamic and scalable portfolio and identity data.

## 🚀 Technologies

* **Language:** Java 17+
* **Framework:** Spring Boot
* **Architecture:** Hexagonal Architecture (Ports & Adapters)
* **Database Migration:** Flyway
* **Containerization:** Docker & Docker Compose
* **Testing:** Cucumber (Gherkin), JUnit

## 🏗 Architecture

This project strictly adheres to **Hexagonal Architecture**. The codebase is likely organized into three distinct layers:

1.  **Domain:** Contains the core business logic and entities. It has no dependencies on external frameworks.
2.  **Application:** Contains use cases and input ports (interfaces) that orchestrate the domain logic.
3.  **Infrastructure:** Contains the adapters (output ports) for external concerns such as:
    * **Persistence:** Database repositories (JPA/Hibernate).
    * **Web:** REST Controllers.
    * **Configuration:** Spring configuration beans.

## 🛠 Getting Started

### Prerequisites

Ensure you have the following installed on your machine:
* [Java Development Kit (JDK) 17](https://adoptium.net/) or higher
* [Docker](https://www.docker.com/products/docker-desktop) & Docker Compose
* [Maven](https://maven.apache.org/) (or use the included `mvnw` wrapper)

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/nawiedk/profile-service.git](https://github.com/nawiedk/profile-service.git)
    cd profile-service
    ```

2.  **Build the project:**
    Use the Maven wrapper to ensure you use the correct Maven version.
    ```bash
    ./mvnw clean install
    ```

### 🏃‍♂️ Running the Application

#### Option 1: Using Docker (Recommended)
This will start the application along with any dependent services (like the database) defined in `docker-compose.yml`.

```bash
docker-compose up -d --build
