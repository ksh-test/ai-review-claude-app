# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an AI PR Review Test project - a Spring Boot 3.5.6 application designed specifically to test AI-powered code review tools. The codebase intentionally contains both obvious and subtle code issues to evaluate how well AI reviewers can detect different types of problems.

**Important Context**: Many files contain deliberate issues with inline comments marking them as "코드 리뷰 도구가 검출하기 쉬운 문제" (easy for code review tools to detect) or "코드 리뷰 도구가 검출하기 어려운 문제" (difficult for code review tools to detect). These are intentional test cases, not bugs to be fixed.

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Java Version**: 21
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Frontend**: Vanilla HTML/CSS/JavaScript (single-page in `src/main/resources/static/index.html`)

## Common Commands

### Build and Run
```bash
# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run with DevTools enabled
./mvnw spring-boot:run -Dspring-boot.run.fork=false
```

### Testing
```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=AiPrReviewTestApplicationTests

# Run tests with coverage
./mvnw test jacoco:report
```

### Database Access
- H2 Console: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

### API Endpoints
- Users API: http://localhost:8080/api/users
- Posts API: http://localhost:8080/api/posts
- Comments API: http://localhost:8080/api/comments
- Web Interface: http://localhost:8080/index.html

## Architecture

### Domain Model
The application follows a classic layered architecture with three main domain entities:

1. **User** (`entity/User.java`) - Central entity representing system users
   - Contains fields: username, email, password, role, apiKey, secretToken
   - One-to-many relationships with Post and Comment
   - Includes test fields like `a1b2c3` and hardcoded credentials for testing

2. **Post** (`entity/Post.java`) - Blog posts created by users
   - Many-to-one relationship with User
   - One-to-many relationship with Comment
   - Includes PostStatus enum (DRAFT, PUBLISHED, ARCHIVED)

3. **Comment** (`entity/Comment.java`) - Comments on posts
   - Many-to-one relationships with both User and Post
   - Includes approval workflow fields

### Layer Structure

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (JPA Entities)
```

- **Controllers** (`controller/`) - RESTful endpoints using `@RestController`
- **Services** (`service/`) - Business logic with `@Service` annotation
- **Repositories** (`repository/`) - Spring Data JPA repositories extending `JpaRepository`
- **Entities** (`entity/`) - JPA entities with relationships

### Configuration

- **AppConfig** (`config/AppConfig.java`) - Contains CORS configuration and intentionally insecure settings for testing
- **DataInitializer** (`config/DataInitializer.java`) - `CommandLineRunner` that seeds the H2 database with sample data on startup
  - Creates 3 users (admin, john_doe, jane_smith)
  - Creates 3 posts
  - Creates 4 comments
  - Only runs if database is empty

### Key Patterns

- Uses field-based `@Autowired` dependency injection (legacy pattern, intentional for testing)
- JPA entities use `@PreUpdate` lifecycle callbacks for timestamp management
- Lazy loading on all relationships to prevent N+1 queries
- Repository methods include both safe and intentionally unsafe queries for testing

## Project Purpose

This codebase is specifically designed to test AI code review tools (like CodeRabbit, configured in `.coderabbit.yaml`). When working on this project:

1. **Do not fix intentional issues** unless explicitly asked
2. **Preserve test comments** that mark issues as easy/difficult to detect
3. **Maintain the intentionally flawed patterns** (hardcoded passwords, SQL injection vulnerabilities, meaningless variable names, etc.)
4. **Understand the context** - this is a test harness, not production code

## CodeRabbit Configuration

The project uses CodeRabbit for AI-powered PR reviews with Korean language settings (see `.coderabbit.yaml`).
