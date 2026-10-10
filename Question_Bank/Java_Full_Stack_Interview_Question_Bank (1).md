git config --global user.name "rishabh455"
git config --global user.email "rishabhchourasia858@gmail.com"# Java Full Stack Interview Question Bank

**Version:** 1.1  |  **Initialized:** 10 October 2026  |  **Last updated:** 10 October 2026

## How to keep this file updated
- Treat this as the primary, cumulative question bank. When new LinkedIn posts/questions are supplied, add each question to the appropriate topic. Keep all SQL, database, JPA, connectivity, and database-performance questions in this same master file.
- Merge exact or near-duplicate questions instead of repeating them; preserve distinct scenario-based versions when they test different troubleshooting skills.
- Keep the IDs stable. Assign the next unused ID within the matching prefix when adding a genuinely new question.
- Add the date/source in the change log. If a source question is incomplete or OCR-corrupted, flag it rather than silently guessing the missing text.
- This bank contains questions only; answers have not been added.

## Topic index
- Project, Production Support & Behavioral — 12 questions
- Core Java, OOP & Language Fundamentals — 16 questions
- Java Collections, Equality & Immutability — 32 questions
- Multithreading & Concurrency — 12 questions
- JVM, Memory, GC & Production Troubleshooting — 15 questions
- Spring Framework & Spring Boot — 14 questions
- REST APIs, Authentication & API Security — 11 questions
- Microservices & Distributed Systems — 9 questions
- Messaging, Kafka & Reliable Processing — 5 questions
- SQL, Databases, JPA & Transactions — 13 questions
- Frontend: Angular & Micro Frontends — 3 questions
- System Design, Caching & Scaling — 3 questions
- Cloud, DevOps, Deployment & Git — 4 questions
- Coding Problems — 2 questions
- EJB / Enterprise Java — 4 questions

---

## Project, Production Support & Behavioral

- **PROJ-001** — Tell me about yourself.
- **PROJ-002** — Explain your current project and its architecture.
- **PROJ-003** — What are your role and responsibilities in the project?
- **PROJ-004** — Explain a production issue you handled.
- **PROJ-005** — How do you debug a production issue?
- **PROJ-006** — How do you handle a production incident?
- **PROJ-007** — How do you troubleshoot application performance issues?
- **PROJ-008** — Explain the complete request flow from Postman/browser to the service response.
- **PROJ-009** — How do you manage conflicts in a project?
- **PROJ-010** — How would you handle a disagreement with colleagues about a technical solution?
- **PROJ-011** — Why do you want to join this company when you already have other offers?
- **PROJ-012** — Are you comfortable working with a new technology stack?

## Core Java, OOP & Language Fundamentals

- **CORE-001** — What are the four pillars of OOP?
- **CORE-002** — Explain encapsulation, abstraction, inheritance and polymorphism with a real-world example.
- **CORE-003** — What is the difference between method overloading and method overriding?
- **CORE-004** — What is the difference between compile-time and runtime polymorphism?
- **CORE-005** — What is the difference between an abstract class and an interface?
- **CORE-006** — Can static methods be overridden? What is method hiding?
- **CORE-007** — What is the difference between aggregation and composition?
- **CORE-008** — Explain the SOLID principles with examples.
- **CORE-009** — What is the Dependency Inversion Principle?
- **CORE-010** — What is the difference between Dependency Inversion and Dependency Injection?
- **CORE-011** — What is the difference between this and super?
- **CORE-012** — Can the main() method be overloaded? Which main() method does the JVM execute?
- **CORE-013** — What are the key features introduced in Java 8?
- **CORE-014** — What is the difference between a functional interface and a marker interface?
- **CORE-015** — What is Reflection in Java?
- **CORE-016** — How do you create and handle custom exceptions?

## Java Collections, Equality & Immutability

- **COLL-001** — Explain the Java Collections Framework.
- **COLL-002** — How does HashMap work internally?
- **COLL-003** — How are HashMap collisions handled?
- **COLL-004** — When does a HashMap bucket become a Red-Black tree?
- **COLL-005** — What is the HashMap load factor?
- **COLL-006** — What happens when a HashMap resizes?
- **COLL-007** — Why does HashMap allow one null key?
- **COLL-008** — What is the difference between HashMap and ConcurrentHashMap?
- **COLL-009** — Why does ConcurrentHashMap not allow null keys or values?
- **COLL-010** — How does ConcurrentHashMap achieve thread safety?
- **COLL-011** — What changed in ConcurrentHashMap between Java 7 and Java 8+?
- **COLL-012** — What is the difference between HashMap, Hashtable and ConcurrentHashMap?
- **COLL-013** — What is the difference between ArrayList and LinkedList?
- **COLL-014** — When would you prefer LinkedList over ArrayList?
- **COLL-015** — What is the difference between ArrayList and Vector?
- **COLL-016** — What is the difference between HashSet, LinkedHashSet and TreeSet?
- **COLL-017** — What is the difference between HashMap, LinkedHashMap and TreeMap?
- **COLL-018** — What is TreeSet?
- **COLL-019** — What is the difference between List and LinkedList?
- **COLL-020** — What is the difference between == and equals()?
- **COLL-021** — Why should hashCode() be overridden when equals() is overridden?
- **COLL-022** — What is the equals() and hashCode() contract?
- **COLL-023** — How does HashMap use hashCode() and equals()?
- **COLL-024** — What happens when two objects have the same hash code?
- **COLL-025** — What happens if two equal objects have different hash codes?
- **COLL-026** — How do you create an immutable class?
- **COLL-027** — Why should an immutable class generally be final?
- **COLL-028** — What is a defensive copy, and why is it needed?
- **COLL-029** — What is the final keyword used for?
- **COLL-030** — What is the transient keyword?
- **COLL-031** — What is the difference between Java transient and JPA @Transient?
- **COLL-032** — What is the difference between final, finally and finalize()?

## Multithreading & Concurrency

- **CONC-001** — Explain multithreading and concurrency in Java.
- **CONC-002** — What is the difference between synchronized, volatile and locks?
- **CONC-003** — What is the difference between synchronized and ReentrantLock?
- **CONC-004** — What is the difference between Runnable and Callable?
- **CONC-005** — What is the difference between start() and run()?
- **CONC-006** — What is the difference between wait() and sleep()?
- **CONC-007** — What is synchronization in Java?
- **CONC-008** — Multiple threads are updating shared data incorrectly. How would you fix it?
- **CONC-009** — How would you handle high concurrency safely?
- **CONC-010** — A thread is stuck in BLOCKED state. How would you identify and fix it?
- **CONC-011** — You detect a deadlock in your system. How would you detect and resolve it?
- **CONC-012** — Your thread pool gets exhausted under load. What is your approach?

## JVM, Memory, GC & Production Troubleshooting

- **JVM-001** — What is a memory leak in Java?
- **JVM-002** — How do you troubleshoot an OutOfMemoryError?
- **JVM-003** — What is the difference between a heap dump and a thread dump?
- **JVM-004** — Explain Java memory areas: Heap, Stack, Method Area and Program Counter.
- **JVM-005** — What is garbage collection, and what was the purpose of finalize()?
- **JVM-006** — Your Java application suddenly throws OutOfMemoryError. How will you debug it?
- **JVM-007** — You see high CPU usage but low traffic. What could be the reason?
- **JVM-008** — Your application slows down after running for a few hours. What will you check?
- **JVM-009** — You are facing frequent GC pauses. How will you optimize the application?
- **JVM-010** — A HashMap causes performance issues under heavy load. What could be the reason?
- **JVM-011** — Your API works locally but fails in production. What will you investigate?
- **JVM-012** — You suspect a memory leak. How will you confirm it?
- **JVM-013** — A service becomes unresponsive randomly. What could be happening?
- **JVM-014** — Your logs show inconsistent behavior across requests. Why might this happen?
- **JVM-015** — Your application crashes without a clear error. How will you debug it?

## Spring Framework & Spring Boot

- **SPR-001** — What are stereotype annotations in Spring?
- **SPR-002** — What is @PostConstruct?
- **SPR-003** — What is Dependency Injection?
- **SPR-004** — How does Spring create and manage beans?
- **SPR-005** — Explain Spring Boot auto-configuration.
- **SPR-006** — What are the main Spring Boot features you use in day-to-day work?
- **SPR-007** — How do you implement robust exception handling in Spring Boot?
- **SPR-008** — What is the difference between @ControllerAdvice and @RestControllerAdvice?
- **SPR-009** — How would you handle HttpClientErrorException?
- **SPR-010** — How do you implement a health check?
- **SPR-011** — What is Spring Boot Actuator?
- **SPR-012** — How do you secure REST APIs in Spring Boot?
- **SPR-013** — How would you schedule a batch job, and why would a backend application need one?
- **SPR-014** — Which dependency injection style do you prefer—field, constructor or setter—and why?

## REST APIs, Authentication & API Security

- **API-001** — Explain REST API development using Spring Boot.
- **API-002** — How does one API communicate with another API?
- **API-003** — What is idempotency in REST APIs?
- **API-004** — What is an idempotency key, and how can it prevent duplicate payment requests from the same client?
- **API-005** — How would you handle a third-party API failing while your application receives multiple requests?
- **API-006** — Design a login authentication controller. What security measures and HTTP headers would you use?
- **API-007** — Explain OAuth architecture.
- **API-008** — What is JWT? Is it stateful or stateless?
- **API-009** — What happens if someone modifies a JWT claim, such as its expiry?
- **API-010** — How do you handle browser sessions?
- **API-011** — An API must interact with a client/server. What phases would you consider when designing the system?

## SQL, Databases, JPA & Transactions

### JPA, database connectivity & transactions

- **JPA-001** — What is JPA?
- **JPA-002** — How do you define a primary key using JPA?
- **JPA-003** — How do you connect a Spring Boot application to a database?
- **JPA-004** — What configurations are required for database connectivity?
- **JPA-005** — What is @Transactional?

### SQL query writing & ranking

- **SQL-001** — Write an SQL query to rank employees based on their experience.
- **SQL-002** — What is the difference between RANK(), DENSE_RANK() and ROW_NUMBER()?
- **SQL-003** — Explain the order of execution of SQL query clauses.

### Query optimization & database performance

- **SQL-004** — How do you optimize a slow SQL query?
- **SQL-005** — How do indexes improve query performance?
- **SQL-006** — Explain joins and their use cases.
- **SQL-007** — How would you troubleshoot a database performance issue?
- **SQL-008** — A database call is slowing down your Java service. How would you optimize it?

## Microservices & Distributed Systems

- **MS-001** — Explain your microservices architecture.
- **MS-002** — How do microservices communicate with each other?
- **MS-003** — How do you handle failures between microservices?
- **MS-004** — How do you handle multiple dependent services and service failures?
- **MS-005** — How do you achieve idempotency in a distributed system?
- **MS-006** — Explain transactions in microservices.
- **MS-007** — How do you handle distributed transactions?
- **MS-008** — Explain Saga architecture.
- **MS-009** — When would you use synchronous versus asynchronous communication?

## Messaging, Kafka & Reliable Processing

- **MSG-001** — What messaging technologies have you worked with?
- **MSG-002** — What is the difference between Kafka, RabbitMQ and IBM MQ?
- **MSG-003** — How do you handle message failures?
- **MSG-004** — How do you ensure reliable message processing?
- **MSG-005** — Kafka consumer lag: the consumer is slower than the producer and lag keeps increasing. How would you troubleshoot it?

## Frontend: Angular & Micro Frontends

- **FE-001** — Explain Angular Pipes and component-to-component communication.
- **FE-002** — What is the difference between Micro Frontend and Mini Frontend?
- **FE-003** — Can Micro Frontends use multiple technology stacks?

## System Design, Caching & Scaling

- **SYS-001** — A cache is returning stale data. How would you fix it?
- **SYS-002** — Caching versus pagination: why use pagination if caching can also store data?
- **SYS-003** — Design an LRU Cache.

## Cloud, DevOps, Deployment & Git

- **DEV-001** — How do you resolve Git merge conflicts?
- **DEV-002** — Explain the application packaging and deployment process.
- **DEV-003** — Which AWS services have you used, including S3 and Lambda?
- **DEV-004** — What Kubernetes deployment and rollout commands do you know?

## Coding Problems

- **CODE-001** — Write Java 8 code using Streams to solve a given problem.
- **CODE-002** — Find the minimum-length subarray whose sum equals a given target.

## EJB / Enterprise Java

- **EJB-001** — What is EJB?
- **EJB-002** — What are the differences between the types of EJBs?
- **EJB-003** — Where would EJB be used in an enterprise application?
- **EJB-004** — How is EJB different from Spring-managed components?

## Source-cleanup notes

- Repeated Deloitte interview posts and repeated production-troubleshooting lists were deduplicated.
- The source contains an incomplete question beginning “Your application is not scaling even after adding ...”. It was not completed by inference; recover the full wording from the original image/post before adding it.
- One OCR-rendered line says a thread is stuck in “RLOCKED” state. Because another line clearly says “BLOCKED state,” the normalized question uses BLOCKED; confirm against the image/original if “RLOCKED” was intended to mean something else.
- Screenshot UI text, hashtags, and unrelated fragments were excluded.

## New-question intake template

Copy this block into a message or notes when collecting a new batch; questions can then be classified and merged into the bank.

```text
Date collected:
Source/post/link (if available):
Raw question(s):
Notes / exact wording uncertainty:
```

## Change log

- **2026-10-10** — Created initial topic-wise bank from the supplied text; merged duplicates, normalized obvious formatting/OCR issues, and kept SQL, database connectivity, JPA, and transaction questions together in this master file.
- **2026-10-10** — Consolidated SQL query, database performance, JPA, database connectivity, and transaction questions into one combined section in the master bank; the separate SQL file is no longer the maintained source.
