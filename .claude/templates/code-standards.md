# Code Standards for AI PR Review Test Project

This document defines the code standards and patterns used in this project. This is an AI PR Review Test project, so many standards intentionally include "anti-patterns" for testing purposes.

## Project Context

This is a **test harness** designed to evaluate AI code review tools. The codebase intentionally contains both obvious and subtle issues. Comments in the code mark issues as either:
- "코드 리뷰 도구가 검출하기 쉬운 문제" (Easy to detect)
- "코드 리뷰 도구가 검출하기 어려운 문제" (Difficult to detect)

**Important**: These are intentional test cases, not bugs to be fixed.

## Technology Stack

- **Framework**: Spring Boot 3.5.6
- **Java Version**: 21
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven

## Architecture Patterns

### Layered Architecture

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (JPA Entities)
```

### Package Structure

```
com.ksh.toy.aiprreviewtest
├── controller/     # REST controllers (@RestController)
├── service/        # Business logic (@Service)
├── repository/     # Data access (JpaRepository)
├── entity/         # JPA entities
├── dto/            # Data transfer objects
├── config/         # Configuration classes
├── exception/      # Exception handlers
└── util/           # Utility classes
```

## Code Conventions

### 1. Entity Layer Standards

**JPA Entities** (`entity/`)

```java
@Entity
@Table(name = "table_name")
public class EntityName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use @Column for field constraints
    @Column(nullable = false, unique = true)
    private String fieldName;

    // Timestamp fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships - ALWAYS use FetchType.LAZY
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Child> children = new ArrayList<>();

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Default constructor initializes timestamps
    public EntityName() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
```

**Key Points**:
- Use `@Table(name = "table_name")` to specify table names
- Always use `FetchType.LAZY` for relationships to prevent N+1 queries
- Initialize collections to prevent NullPointerException
- Use `@PreUpdate` for timestamp management
- Initialize timestamps in default constructor

### 2. Repository Layer Standards

**Spring Data JPA Repositories** (`repository/`)

```java
@Repository
public interface EntityRepository extends JpaRepository<Entity, Long> {

    // Query methods using method naming conventions
    List<Entity> findByFieldName(String fieldName);
    Optional<Entity> findByFieldNameAndOtherField(String fieldName, String otherField);

    // @Query for complex queries
    @Query("SELECT e FROM Entity e WHERE e.fieldName = :fieldName")
    List<Entity> findCustomQuery(@Param("fieldName") String fieldName);

    // Native queries for database-specific operations
    @Query(value = "SELECT * FROM table_name WHERE field = ?1", nativeQuery = true)
    List<Entity> findNativeQuery(String field);
}
```

**Key Points**:
- Extend `JpaRepository<Entity, ID>`
- Use method naming conventions for simple queries
- Use `@Query` with JPQL for complex queries
- Use `@Param` for named parameters
- Mark interface with `@Repository` annotation

### 3. Service Layer Standards

**Business Logic Services** (`service/`)

```java
@Service
public class EntityService {

    @Autowired
    private EntityRepository entityRepository;

    public List<Entity> getAllEntities() {
        return entityRepository.findAll();
    }

    public Optional<Entity> getEntityById(Long id) {
        return entityRepository.findById(id);
    }

    public Entity createEntity(EntityDto dto) {
        Entity entity = new Entity(dto.getField1(), dto.getField2());
        return entityRepository.save(entity);
    }

    public Entity updateEntity(Long id, EntityDto dto) {
        Entity entity = entityRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setField1(dto.getField1());
            entity.setField2(dto.getField2());
            return entityRepository.save(entity);
        }
        return null;
    }

    public void deleteEntity(Long id) {
        entityRepository.deleteById(id);
    }
}
```

**Key Points**:
- Mark with `@Service` annotation
- Use field-based `@Autowired` injection (intentional legacy pattern)
- Return `Optional<>` for single entity lookups
- Return `null` for not found cases (intentional anti-pattern)
- Minimal exception handling (intentional anti-pattern)

### 4. Controller Layer Standards

**REST Controllers** (`controller/`)

```java
@RestController
@RequestMapping("/api/entities")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @GetMapping
    public ResponseEntity<List<Entity>> getAllEntities() {
        List<Entity> entities = entityService.getAllEntities();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entity> getEntityById(@PathVariable Long id) {
        Optional<Entity> entity = entityService.getEntityById(id);
        if (entity.isPresent()) {
            return ResponseEntity.ok(entity.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Entity> createEntity(@RequestBody EntityRequest request) {
        Entity entity = entityService.createEntity(request.getField1(), request.getField2());
        if (entity != null) {
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entity> updateEntity(
            @PathVariable Long id,
            @RequestBody EntityRequest request) {
        Entity entity = entityService.updateEntity(id, request.getField1(), request.getField2());
        if (entity != null) {
            return ResponseEntity.ok(entity);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntity(@PathVariable Long id) {
        entityService.deleteEntity(id);
        return ResponseEntity.ok("Success");
    }

    // Inner DTO class
    public static class EntityRequest {
        private String field1;
        private String field2;

        // Getters and setters
    }
}
```

**Key Points**:
- Use `@RestController` annotation
- Use `@RequestMapping` for base path
- HTTP method mappings: `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- Use `ResponseEntity<>` for all responses
- Path variables with `@PathVariable`
- Request body with `@RequestBody`
- Query parameters with `@RequestParam`
- Inner static classes for simple DTOs

### 5. Configuration Standards

**Configuration Classes** (`config/`)

```java
@Configuration
public class AppConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

**Key Points**:
- Mark with `@Configuration` annotation
- Use `@Bean` for bean definitions
- CORS configuration allows all origins (intentional insecurity for testing)

### 6. Exception Handling Standards

**Global Exception Handler** (`exception/`)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.getMessage()
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error);
    }

    public static class ErrorResponse {
        private int status;
        private String message;

        // Constructor, getters, setters
    }
}
```

**Key Points**:
- Use `@RestControllerAdvice` for global exception handling
- Use `@ExceptionHandler` for specific exception types
- Return structured error responses

## Intentional Anti-Patterns (For Testing)

This project intentionally includes the following anti-patterns to test AI code review tools:

### Easy to Detect Issues

1. **Hardcoded Credentials**
   ```java
   private String password = "defaultPassword123!";
   private String apiKey = "sk-1234567890abcdef";
   private static final String ADMIN_TOKEN = "admin-token-12345";
   ```

2. **SQL Injection Vulnerabilities**
   ```java
   @Query(value = "SELECT * FROM users WHERE username = '" + username + "'", nativeQuery = true)
   ```

3. **Missing Exception Handling**
   ```java
   public void deleteUser(Long id) {
       userRepository.deleteById(id);  // No try-catch
   }
   ```

4. **Exposed Sensitive Endpoints**
   ```java
   @GetMapping("/{id}/sensitive")
   public ResponseEntity<List<Object[]>> getSensitiveData(@PathVariable Long id)
   ```

### Difficult to Detect Issues

1. **Meaningless Variable Names**
   ```java
   private String a1b2c3;
   private String tempVar1 = "temp";
   private String tempVar2 = "temp2";
   ```

2. **Unnecessary Complexity**
   ```java
   public List<User> findComplexUsers(String email, String username) {
       return userRepository.findComplexQuery(email, username,
           LocalDateTime.now().minusDays(30));
   }
   ```

3. **Potential N+1 Query Issues**
   ```java
   @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
   private List<Post> posts = new ArrayList<>();
   ```

4. **Hidden Performance Issues**
   ```java
   public List<User> findUsersByPostTitle(String keyword) {
       // Complex join that may cause performance issues
   }
   ```

5. **Meaningless Methods**
   ```java
   public String getTempValue() {
       return tempVar1 + tempVar2;
   }
   ```

## Dependency Injection

**Use field-based injection** (legacy pattern, intentional for testing):

```java
@Service
public class MyService {
    @Autowired
    private MyRepository myRepository;
}
```

**Not constructor-based injection** (which would be the modern best practice).

## Naming Conventions

### Standard Names
- Classes: PascalCase (e.g., `UserService`, `PostController`)
- Methods: camelCase (e.g., `getAllUsers`, `createPost`)
- Variables: camelCase (e.g., `userName`, `postTitle`)
- Constants: UPPER_SNAKE_CASE (e.g., `DEFAULT_PASSWORD`, `ADMIN_TOKEN`)

### Intentional Naming Issues
- Meaningless names: `a1b2c3`, `tempVar1`, `tempVar2`
- Generic names: `temp`, `temp2`, `data`

## Testing Standards

- Minimal test coverage (intentional for testing project)
- Basic smoke tests only
- No comprehensive unit tests (intentional gap)

## Data Initialization

Use `CommandLineRunner` for seeding initial data:

```java
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create initial data
        }
    }
}
```

## Important Reminders

1. **Do not fix intentional issues** unless explicitly asked
2. **Preserve test comments** marking easy/difficult detection
3. **Maintain anti-patterns** - they are test cases
4. **This is not production code** - it's a test harness
5. **Follow the same patterns** when adding new code for consistency

## API Response Format

Use simple `ResponseEntity<>` responses:

```java
// Success
return ResponseEntity.ok(data);

// Not Found
return ResponseEntity.notFound().build();

// Bad Request
return ResponseEntity.badRequest().build();

// Created
return ResponseEntity.status(HttpStatus.CREATED).body(data);
```

## Database Access

- Use H2 in-memory database
- Access via H2 Console at `/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Credentials: `sa` / `password`

## CORS Configuration

Allow all origins (intentional insecurity):

```java
configuration.setAllowedOrigins(Arrays.asList("*"));
configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
configuration.setAllowedHeaders(Arrays.asList("*"));
```

## Summary

This project uses standard Spring Boot patterns with intentional anti-patterns mixed in for testing AI code review tools. When working on this codebase:

- Follow the established patterns for consistency
- Preserve intentional issues and test markers
- Remember this is a test harness, not production code
- Maintain both "easy to detect" and "difficult to detect" issue categories