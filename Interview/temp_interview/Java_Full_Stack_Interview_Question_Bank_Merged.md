# Java Full Stack Interview Question Bank



**Version:** 1.1  |  **Initialized:** 10 October 2026  |  **Last updated:** 10 October 2026



## How to keep this file updated**

- Treat this as the primary, cumulative question bank. When new LinkedIn posts/questions are supplied, add each question to the appropriate topic. Keep all SQL, database, JPA, connectivity, and database-performance questions in this same master file.

- Merge exact or near-duplicate questions instead of repeating them; preserve distinct scenario-based versions when they test different troubleshooting skills.

- Keep the IDs stable. Assign the next unused ID within the matching prefix when adding a genuinely new question.

- Add the date/source in the change log. If a source question is incomplete or OCR-corrupted, flag it rather than silently guessing the missing text.

- This bank contains questions only; answers have not been added.



## Topic index**

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



## Project, Production Support & Behavioral**



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



## Core Java, OOP & Language Fundamentals**



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



## Java Collections, Equality & Immutability**



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



## Multithreading & Concurrency**



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



## JVM, Memory, GC & Production Troubleshooting**



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



## Spring Framework & Spring Boot**



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



## REST APIs, Authentication & API Security**



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



## SQL, Databases, JPA & Transactions**



### JPA, database connectivity & transactions**



- **JPA-001** — What is JPA?

- **JPA-002** — How do you define a primary key using JPA?

- **JPA-003** — How do you connect a Spring Boot application to a database?

- **JPA-004** — What configurations are required for database connectivity?

- **JPA-005** — What is @Transactional?



### SQL query writing & ranking**



- **SQL-001** — Write an SQL query to rank employees based on their experience.

- **SQL-002** — What is the difference between RANK(), DENSE_RANK() and ROW_NUMBER()?

- **SQL-003** — Explain the order of execution of SQL query clauses.



### Query optimization & database performance**



- **SQL-004** — How do you optimize a slow SQL query?

- **SQL-005** — How do indexes improve query performance?

- **SQL-006** — Explain joins and their use cases.

- **SQL-007** — How would you troubleshoot a database performance issue?

- **SQL-008** — A database call is slowing down your Java service. How would you optimize it?



## Microservices & Distributed Systems**



- **MS-001** — Explain your microservices architecture.

- **MS-002** — How do microservices communicate with each other?

- **MS-003** — How do you handle failures between microservices?

- **MS-004** — How do you handle multiple dependent services and service failures?

- **MS-005** — How do you achieve idempotency in a distributed system?

- **MS-006** — Explain transactions in microservices.

- **MS-007** — How do you handle distributed transactions?

- **MS-008** — Explain Saga architecture.

- **MS-009** — When would you use synchronous versus asynchronous communication?



## Messaging, Kafka & Reliable Processing**



- **MSG-001** — What messaging technologies have you worked with?

- **MSG-002** — What is the difference between Kafka, RabbitMQ and IBM MQ?

- **MSG-003** — How do you handle message failures?

- **MSG-004** — How do you ensure reliable message processing?

- **MSG-005** — Kafka consumer lag: the consumer is slower than the producer and lag keeps increasing. How would you troubleshoot it?



## Frontend: Angular & Micro Frontends**



- **FE-001** — Explain Angular Pipes and component-to-component communication.

- **FE-002** — What is the difference between Micro Frontend and Mini Frontend?

- **FE-003** — Can Micro Frontends use multiple technology stacks?



## System Design, Caching & Scaling**



- **SYS-001** — A cache is returning stale data. How would you fix it?

- **SYS-002** — Caching versus pagination: why use pagination if caching can also store data?

- **SYS-003** — Design an LRU Cache.



## Cloud, DevOps, Deployment & Git**



- **DEV-001** — How do you resolve Git merge conflicts?

- **DEV-002** — Explain the application packaging and deployment process.

- **DEV-003** — Which AWS services have you used, including S3 and Lambda?

- **DEV-004** — What Kubernetes deployment and rollout commands do you know?



## Coding Problems**



- **CODE-001** — Write Java 8 code using Streams to solve a given problem.

- **CODE-002** — Find the minimum-length subarray whose sum equals a given target.



## EJB / Enterprise Java**



- **EJB-001** — What is EJB?

- **EJB-002** — What are the differences between the types of EJBs?

- **EJB-003** — Where would EJB be used in an enterprise application?

- **EJB-004** — How is EJB different from Spring-managed components?



## Source-cleanup notes**



- Repeated Deloitte interview posts and repeated production-troubleshooting lists were deduplicated.

- The source contains an incomplete question beginning “Your application is not scaling even after adding ...”. It was not completed by inference; recover the full wording from the original image/post before adding it.

- One OCR-rendered line says a thread is stuck in “RLOCKED” state. Because another line clearly says “BLOCKED state,” the normalized question uses BLOCKED; confirm against the image/original if “RLOCKED” was intended to mean something else.

- Screenshot UI text, hashtags, and unrelated fragments were excluded.



## New-question intake template**



Copy this block into a message or notes when collecting a new batch; questions can then be classified and merged into the bank.



\`\`\`text

Date collected:

Source/post/link (if available):

Raw question(s):

Notes / exact wording uncertainty:

\`\`\`



## Change log**



- **2026-10-10** — Created initial topic-wise bank from the supplied text; merged duplicates, normalized obvious formatting/OCR issues, and kept SQL, database connectivity, JPA, and transaction questions together in this master file.

- **2026-10-10** — Consolidated SQL query, database performance, JPA, database connectivity, and transaction questions into one combined section in the master bank; the separate SQL file is no longer the maintained source.

---

## Merged Interview Transcript Questions — New Questions Only

The questions below were taken from the first interview-question file and added after checking against the existing master bank. Exact and near-duplicates already covered in the master bank were not repeated. Follow-up probes that test a distinct detail are retained. Existing question IDs have not been renumbered.

### Core Java, OOP, Strings & Streams

- **CORE-017** — Which types of inheritance does Java not support directly, and why?
- **CORE-018** — What is the diamond problem in multiple inheritance, and how does Java handle it?
- **STR-001** — What is the String Constant Pool in Java?
- **STR-002** — What is the difference between creating a String with a string literal and using the `new` keyword?
- **STR-003** — What is the difference between `StringBuffer` and `StringBuilder`?
- **STREAM-001** — What is the difference between `map()` and `flatMap()` in Java Streams?
- **STREAM-002** — Write a `flatMap()` example for a list of lists.
- **STREAM-003** — How would you demonstrate `flatMap()` with a practical Java Streams example?

### Multithreading & Concurrency

- **CONC-013** — What are the different ways to create or define multithreading in Java?

### Spring Framework, Spring Boot, AOP & Persistence

- **SPR-015** — Why would you choose Spring Boot over Spring MVC, and what problems does Spring Boot solve?
- **SPR-016** — What is the difference between JDBC and Hibernate, and when would you prefer each?
- **SPR-017** — How do you create a Singleton, and should its constructor be private or public?
- **AOP-001** — What is Spring AOP, and why is it used?
- **AOP-002** — What are the different types of advice in Spring AOP?
- **AOP-003** — Explain `@Around` advice and how it differs from `@AfterThrowing` advice.
- **SEC-001** — Have you implemented Spring Security? Explain what you used it for.
- **JPA-006** — Have you implemented transaction management? Explain where and how it was used.
- **JPA-007** — How comfortable are you with JDBC when your project primarily uses JPA?

### Microservices, REST APIs & Architecture

- **MS-010** — Walk through the architecture of your Visitor Management System, including the microservices layer and the Spring Boot components used.
- **MS-011** — Which protocols or communication mechanisms do your services use, and why?
- **API-012** — What is the difference between a path variable and a query parameter in a REST API?
- **API-013** — Which HTTP methods are idempotent, and what does idempotency mean for an API?

### SQL, Database Concepts & PL/SQL

- **DB-001** — Which database technologies have you worked with, and what was your experience with PostgreSQL?
- **DB-002** — What is PL/SQL, and have you worked with it?
- **DB-003** — What is the difference between `DELETE`, `TRUNCATE` and `DROP`?
- **DB-004** — What are database views, and when would you use them?
- **DB-005** — What is the difference between `MINUS` and `INTERSECT`?
- **DB-006** — What database constraints do you know?
- **DB-007** — Can a primary key contain `NULL`? Can a primary key be composite?
- **DB-008** — Why do databases support non-clustered indexes if a clustered index can be faster for some queries?
- **DB-009** — What is the difference between a stored procedure and a function?
- **DB-010** — How can a stored procedure return output?
- **DB-011** — What is a database cursor, and when might it be used?
- **DB-012** — What is the difference between a cursor and a trigger?
- **DB-013** — How would you read, understand or debug an unfamiliar stored procedure?
- **DB-014** — What are ODS and Oracle RDS in the context of the database/service terminology mentioned in the interview? Clarify the intended meaning before answering because the source wording may be ambiguous.

### Production Support, Security & Troubleshooting

- **PROJ-013** — How do you investigate a production incident from detection through resolution?
- **PROJ-014** — How do you ensure security compliance in a project, and which tools or checks do you use?
- **PROJ-015** — How do you identify and remediate vulnerabilities in third-party libraries?
- **PROJ-016** — A user cannot upload financial data through an Excel interface, but the backend has no error logs. How would you troubleshoot end to end?
- **PROJ-017** — If another user or support-team member reproduces the same Excel upload issue, how would that change your investigation?
- **SYS-004** — Under heavy load, how do you identify application bottlenecks and decide what to optimize?

### CI/CD, Build & Deployment

- **DEV-005** — Walk through database migrations, deployment and rollback in a CI/CD pipeline.
- **DEV-006** — What have you implemented in a CI/CD pipeline, and how did it reduce release-cycle time?
- **DEV-007** — Have you worked with Groovy scripts or Gradle?
- **DEV-008** — How would you modify a pipeline to address a security vulnerability or fix a broken build/deployment step?
- **DEV-009** — Why might an application be deployed to an externally managed Tomcat instead of using Spring Boot's embedded Tomcat?
- **DEV-010** — Why might a team deploy a packaged application to an application server instead of running it as an executable Spring Boot JAR?
- **DEV-011** — Why might a project avoid containerization?

### Testing & Quality

- **TEST-001** — What manual work did automated regression tests using JUnit and Mockito remove or reduce?
- **TEST-002** — Did you design and implement the test automation yourself? Explain your contribution.
- **TEST-003** — What is Test-Driven Development (TDD), and have you used it in practice?

### Angular & Frontend

- **FE-004** — What are Angular decorators, and what are the main types?
- **FE-005** — What are annotations, and how are they related to Angular decorators?
- **FE-006** — What is the purpose of Angular pipes, and how would you write a custom pipe?
- **FE-007** — What is the difference between eager loading and lazy loading in Angular?
- **FE-008** — What are the main compilation modes in Angular, and how do they differ?
- **FE-009** — What is an Angular module, and what can it contain?
- **FE-010** — How can an Angular service be made available throughout an application?
- **FE-011** — What is transpiling in Angular?
- **FE-012** — Explain the structure of an Angular application, including components and services.
- **FE-013** — How can you prevent the URL path from changing when navigating in an Angular application?
- **FE-014** — What is the difference between an Observable and a Promise?

### Logging

- **LOG-001** — Which logging levels have you used in your projects?
- **LOG-002** — Rank common logging levels from most severe to least severe.

### Project, Deployment & Hardware Integration

- **PROJ-018** — How was the face-recognition hardware integrated with the software in the project?
- **PROJ-019** — How was the face-recognition model or its training data prepared?
- **PROJ-020** — If the application uses Spring Boot, why might Tomcat still be installed and configured on the servers?
- **PROJ-021** — Was the application designed to process requests concurrently or mainly on a single thread? Explain the actual design.

### Agile, Collaboration & Stakeholders

- **PROJ-022** — What Agile practices have you actually followed or implemented?
- **PROJ-023** — How long are your sprints, and how does sprint length relate to production releases?
- **PROJ-024** — Are changes released immediately after a sprint, or are releases grouped into a later release cycle? Explain your team's process.
- **PROJ-025** — How do you explain technical concepts to non-technical stakeholders?
- **PROJ-026** — How do you collaborate with teams working across regions or time zones?
- **PROJ-027** — How independently do you work, and when do you seek guidance from a senior or manager?
- **PROJ-028** — What is one change you made that you learned a lot from and are proud of?
- **PROJ-029** — How do you demonstrate that you can adapt to a new technology or unfamiliar task?
- **PROJ-030** — What was your contribution to the project architecture and design, and how did senior team members guide that work?
- **PROJ-031** — How large was the team, how many users used the application, and how frequently were releases made?
- **PROJ-032** — How do you describe your day-to-day responsibilities and your role across the projects you have worked on?
- **PROJ-033** — How would you rate your Java and database skills, and what evidence supports that assessment?

### Work Context & Interview Closing

- **PROJ-034** — Which company do you work for, and is your work delivered directly for a client or for your employer's internal team?
- **PROJ-035** — What are the office-attendance expectations, and are you able to attend in person when required?
- **PROJ-036** — Are you comfortable learning and working with an Excel add-in/VSTO or a C# frontend alongside an Oracle database and Java/Spring Boot backend?
- **PROJ-037** — Are you currently using AI tools in your development workflow? If so, how?
- **PROJ-038** — What questions would you ask the interviewer about the project, role, expectations or team?
- **PROJ-039** — What feedback would you ask for after an interview, and how would you use it to improve?

### Source wording to verify

- The source refers to “ODS” and “RDS Oracle — RESTful database service.” This wording may conflate different Oracle/database terms, so it has been preserved as a clarification question rather than silently corrected.
- Some transcript prompts were interviewer follow-ups (for example, “Why?”, “Can you elaborate?”, “Anything else?”) rather than independent technical questions. These have been merged into the relevant parent question or omitted where they added no distinct preparation value.

---

## Change log

- **2026-10-17** — Merged the first interview-transcript question list into this master bank, retained the existing question bank and IDs, added non-duplicate questions by topic, removed repeated/rephrased questions, and cleaned the stray Git configuration text and Markdown formatting at the top of the file.
