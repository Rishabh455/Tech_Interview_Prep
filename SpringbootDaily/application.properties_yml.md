### `application.properties` vs `application.yml` — 1-Minute Interview Answer

> **“Both are used to configure a Spring Boot application. The main difference is the syntax. `application.properties` uses flat key-value pairs like `server.port=8080`, whereas `application.yml` uses hierarchical, indentation-based structure. YAML is generally more readable for complex and nested configurations. Both support profiles and environment-specific configuration, so the choice is mainly based on readability and project conventions.”**

**Example:**

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/test
```

vs.

```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test
```

### Remember

**Properties → `key=value`**  
**YAML → hierarchical/nested**  
**Both → Spring Boot configuration + profiles**