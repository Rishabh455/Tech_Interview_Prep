Yes. You have **7 minutes**, so don't study these as 20 separate textbook answers. Learn the **core answer + one follow-up** for each. The question bank itself groups these into Spring/Spring Core, AOP, JDBC/JPA, and Microservices/Architecture. Pasted markdown

# NOMURA — 7-MINUTE RAPID REVISION

## 17. Why choose Spring Boot over Spring MVC?

### Say this

> “Spring MVC is mainly a web framework for building web applications and REST APIs, while Spring Boot is built on top of Spring and simplifies the overall application setup.  
> Spring Boot provides auto-configuration, starter dependencies, embedded servers and production-ready features, so we can build and deploy applications much faster with less manual configuration.
>
> In my project, I used Spring Boot because it reduced configuration overhead and made it easier to build REST-based microservices.”

### Follow-up: “Can Spring MVC be used without Spring Boot?”

> “Yes. Spring MVC can be configured independently, but it requires more manual configuration.”

### Trap
Don't say **“Spring Boot replaces Spring MVC.”**

Correct:

> “Spring Boot can use Spring MVC underneath.”

---

# 18. JDBC or Hibernate — which do you prefer?

### Say this

> “For an enterprise application like my onboarding project, I prefer JPA with Hibernate for most database operations because it provides ORM, entity mapping, transaction integration and reduces JDBC boilerplate.
>
> I would use JDBC when I need very fine-grained control over SQL, database-specific operations, or a use case where ORM is not appropriate.”

### Follow-up: “Is Hibernate faster than JDBC?”

> “Not inherently. JDBC gives lower-level control and can be more efficient for specific SQL-heavy operations, while Hibernate improves developer productivity and maintainability.”

### Remember

**JDBC = low level**

**Hibernate = ORM**

---

# 19. How are beans created in Spring Boot?

### Say this

> “Spring Boot creates and manages beans through the Spring IoC container.
>
> Beans can be discovered through component scanning using annotations such as `@Component`, `@Service` and `@Repository`, or explicitly created using `@Bean` methods inside a `@Configuration` class.
>
> Spring creates the objects, manages their lifecycle and injects their dependencies.”

### Follow-up: “How does dependency injection happen?”

> “The container resolves the required dependency and injects it, preferably through constructor injection.”

### Very important

```java
@Service
class OnboardingService {
}
```

Spring detects it → creates bean → manages lifecycle → injects wherever required.

---

# 20. @Component vs @Service vs @Repository

### Say this

> “All three are Spring stereotype annotations and allow Spring to detect the class as a bean.
>
> `@Component` is the generic stereotype.
>
> `@Service` is used for business or service-layer logic.
>
> `@Repository` is used for the persistence or data-access layer and also participates in Spring's exception translation mechanism.”

### Memory

```text
@Component  → generic
@Service    → business logic
@Repository → database layer
```

### Follow-up: “Can I use @Component instead of @Service?”

> “Technically yes for bean registration, but `@Service` communicates the intended architectural role more clearly.”

---

# 21. What is Singleton Design Pattern?

### Say this

> “Singleton is a creational design pattern that ensures only one instance of a class exists and provides a global access point to that instance.”

### Project connection

> “In a backend application, a singleton-style object can be useful for stateless shared configuration or reusable infrastructure components, although in Spring I would normally let the Spring container manage singleton beans.”

### VERY IMPORTANT FOLLOW-UP

**“Are Spring beans singleton?”**

> “By default, Spring beans have singleton scope within the Spring ApplicationContext.”

Don't confuse **Spring singleton scope** with the **Singleton design pattern**.

---

# 22–23. How do you create Singleton? Give example.

For interview, use this simple version:

```java
public class Singleton {

    private static final Singleton INSTANCE = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

### Explain

> “The constructor is private so external classes cannot create objects directly. The class creates one static instance and exposes it through `getInstance()`.”

### Follow-up: “Is this thread-safe?”

> “Yes, this eager initialization approach is thread-safe because class initialization is handled safely by the JVM.”

---

# 24. Constructor private or public?

### Answer

> “Private.”

### Why?

> “Because if the constructor is public, any class can create another object and the singleton guarantee is broken.”

### Trap

If interviewer asks:

**“Can Singleton constructor be protected?”**

> “It technically changes the accessibility, but for the standard Singleton pattern we keep it private to prevent external instantiation.”

---

# 25. Have you worked on Spring Security?

### Your project-specific answer

> “Yes. In my banking onboarding project, Spring Security was used for securing backend APIs.
>
> Authentication validates the identity of the caller, while authorization checks whether the caller has permission to perform a particular operation.
>
> Requests pass through the security filter chain, authentication information is validated, and then role or authority-based authorization is applied before the business logic executes.
>
> For protected APIs, we also use token-based authentication such as JWT.”

### Follow-up: “Authentication vs authorization?”

> “Authentication is **who are you?** Authorization is **what are you allowed to do?**”

### Follow-up: “401 vs 403?”

> “401 means unauthenticated. 403 means authenticated but not authorized.”

---

# 26. Have you implemented transaction management?

### Your answer

> “Yes. In Spring Boot, transaction management can be handled using `@Transactional`.
>
> It ensures that a group of related database operations is treated as one transactional unit. If the transaction succeeds, the changes are committed; if an appropriate failure occurs, the transaction can be rolled back.
>
> In my onboarding workflow, this is important when multiple database changes together represent one business operation.”

### Follow-up: “What happens if an exception occurs?”

> “By default, Spring rolls back for unchecked exceptions, while checked-exception rollback behavior can be configured explicitly.”

### Strong cross-question

**“Would you keep a transaction open while calling an external service?”**

> “Generally I avoid long-running database transactions around external calls because they hold database resources unnecessarily. I prefer separating persistence transactions from external communication and using state management, retry and idempotency where needed.”

---

# 27–28. What is Spring AOP?

### Say this

> “AOP stands for Aspect-Oriented Programming. It helps separate cross-cutting concerns from core business logic.
>
> Typical examples are logging, auditing, security, metrics and transaction-related concerns.
>
> Instead of writing the same logic in every service method, we can define it in an aspect and apply it to selected methods.”

This matches the interview bank's AOP section. Pasted markdown

---

# 29. What are the different AOP advices?

### Memorize this order

```text
1. Before
2. After
3. AfterReturning
4. AfterThrowing
5. Around
```

### One-line meanings

> **Before** → before method execution.

> **After** → after method execution, whether success or exception.

> **AfterReturning** → only after successful execution.

> **AfterThrowing** → when method throws exception.

> **Around** → wraps the complete method execution.

---

# 30. Explain Around Advice

### Say this

> “Around advice wraps the target method execution. It can execute code before and after the method and controls whether the target method is actually invoked through `ProceedingJoinPoint.proceed()`.
>
> It can be used for logging, performance measurement, auditing or similar cross-cutting concerns.”

### Code to remember

```java
@Around("execution(* com.example.service..*(..))")
public Object logExecution(ProceedingJoinPoint pjp)
        throws Throwable {

    long start = System.currentTimeMillis();

    Object result = pjp.proceed();

    long end = System.currentTimeMillis();

    System.out.println(end - start);

    return result;
}
```

### Follow-up: “What happens if you don't call `proceed()`?”

> “The target method will not execute.”

**This is a very good AOP trap.**

---

# 31. Around vs AfterThrowing?

### Answer

> “Around advice wraps the entire execution and can execute before and after the method. It also controls whether `proceed()` is called.
>
> AfterThrowing advice executes specifically when the target method throws an exception.”

### Memory

```text
Around
Before → Method → After

AfterThrowing
Method → Exception → Advice
```

---

# 32. Are you comfortable with JDBC?

### Say this

> “Yes. My recent project mainly uses JPA/Hibernate, but I am comfortable with JDBC fundamentals.
>
> JDBC works at a lower level using `Connection`, `PreparedStatement` and `ResultSet`.
>
> I understand how JDBC provides direct SQL control, while JPA/Hibernate provides ORM and reduces boilerplate.”

### Follow-up: “Why PreparedStatement?”

> “It supports parameterized queries and helps prevent SQL injection.”

---

# 33. Walk me through your microservices architecture.

### IMPORTANT: Don't answer with Visitor Management.

For **your Saudi Bank onboarding project**, say:

> “We followed a microservices-based architecture for the Digital Customer Onboarding and KYC platform.
>
> The onboarding service acted as the orchestration layer for the customer journey. It coordinated steps such as identity verification, OTP verification, document processing, KYC validation and AML/compliance checks.
>
> These capabilities were separated into services because they had different responsibilities and external dependencies.
>
> The onboarding workflow maintained the overall application state and allowed the journey to move through controlled business states such as initiated, verified, pending, approved or rejected.”

### Follow-up: “Why microservices?”

> “Independent business capabilities, independent scaling and deployment, reduced coupling, and isolation of external dependencies.”

### Follow-up: “Why not one monolith?”

> “A monolith would tightly couple unrelated capabilities and make independent scaling or deployment harder.”

---

# 34. How are those services communicating?

### Answer

> “For synchronous interactions, we use REST APIs over HTTP/HTTPS.
>
> For asynchronous, decoupled processing where immediate response is not required, messaging such as Kafka can be used.
>
> The choice depends on whether the caller needs an immediate response or whether eventual processing is acceptable.”

### Follow-up: “REST or Kafka — when would you use which?”

> “REST when I need an immediate request-response interaction. Kafka when I want asynchronous communication, decoupling or event-driven processing.”

### Strong banking follow-up

**“What if Kafka publishes the same event twice?”**

> “The consumer must be idempotent. We use an event/message identifier and persist processing state so duplicate delivery does not create duplicate business effects.”

---

# 35. Have you built REST APIs?

### Your answer

> “Yes. In the onboarding project, I worked on REST APIs for operations such as initiating onboarding, validating customer information, triggering verification steps and retrieving application status.
>
> I worked with request/response DTOs, validation, appropriate HTTP methods and status codes, exception handling and security through Spring Security.”

### Follow-up: “What makes a REST API good?”

> “Clear resource-oriented URLs, correct HTTP methods, meaningful status codes, statelessness, validation, consistent error responses and proper security.”

---

# 36. Heavy load — how do you optimize performance and identify bottlenecks?

This is one of the **most important questions** in the whole set. The source explicitly includes heavy-load optimization and bottleneck identification. Pasted markdown

### Say this in 40 seconds

> “I first identify where the bottleneck actually is instead of optimizing blindly.
>
> I look at API latency, CPU and memory usage, database query performance, thread-pool utilization, external-service latency and application logs/metrics.
>
> If the database is the bottleneck, I analyze slow queries and indexes. If the application layer is the bottleneck, I look at inefficient processing, thread pools or unnecessary object creation. For downstream dependencies, I consider timeouts, retries and circuit breakers.
>
> Finally, I measure the improvement using metrics and load testing.”

### Follow-up: “How would you optimize database performance?”

> “Analyze slow queries, use appropriate indexes, avoid unnecessary joins/data fetching, use pagination, optimize queries and verify the execution plan.”

### Follow-up: “How do you scale the application?”

> “Prefer horizontal scaling for stateless services by running multiple instances behind a load balancer. Shared state should be externalized to databases, caches or other shared infrastructure.”

---

# 🔥 7-MINUTE FINAL MEMORIZATION

Read only this immediately before the interview:

```text
SPRING BOOT
Boot = Spring + auto-config + starters + embedded server + production features.

JDBC vs HIBERNATE
JDBC = low-level SQL control.
Hibernate = ORM, less boilerplate.

BEANS
@Component / @Service / @Repository / @Bean
→ Spring IoC container creates + manages them.

COMPONENT
generic.

SERVICE
business logic.

REPOSITORY
data access + exception translation.

SINGLETON
one instance.
private constructor.
static instance.
getInstance().

SPRING SECURITY
Authentication = WHO
Authorization = WHAT
401 = unauthenticated
403 = unauthorized.

TRANSACTION
@Transactional
atomic unit
commit / rollback.

AOP
cross-cutting concerns.
Before
After
AfterReturning
AfterThrowing
Around.

AROUND
proceed() controls target execution.

JDBC
Connection → PreparedStatement → ResultSet.

MICROSERVICES
Onboarding = orchestrator.
Identity / OTP / Document / AML = separate capabilities.

COMMUNICATION
REST = synchronous
Kafka = asynchronous/event-driven.

REST
resource-oriented APIs.
validation + DTO + status codes + security.

PERFORMANCE
Measure → identify bottleneck → DB/app/network → optimize → measure again.
```

## The 5 answers I'd memorize word-for-word

**1. Your architecture:**  
> “Onboarding service acted as the orchestration layer and coordinated identity, OTP, document/KYC and AML capabilities.”

**2. Why microservices:**  
> “Independent responsibilities, scaling, deployment and external dependency isolation.”

**3. Security:**  
> “Spring Security handles authentication and authorization through the security filter chain.”

**4. Transactions:**  
> “`@Transactional` groups related database operations into one atomic unit.”

**5. Performance:**  
> “I first measure and identify the actual bottleneck—API, database, JVM, thread pool or downstream service—then optimize and validate the improvement.”

That is the **highest-yield 7-minute version** of questions 17–36. The source question bank confirms these are the exact Spring, AOP, JDBC/JPA and architecture areas the interviewers tested. Pasted markdown