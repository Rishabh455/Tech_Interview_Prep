भाई, अब हमें preparation को दोबारा zero से शुरू नहीं करना है। हमें अगले 1–2 दिनों में Infosys के Second Round के लिए resume-based targeted revision करनी है — खासकर उन questions की जो तुम्हारे resume के किसी keyword, project experience या technical claim से naturally निकलते हैं।

मैंने तुम्हारा uploaded resume और हमारी पहले की interview preparation को ध्यान में रखकर list बनाई है। तुम्हारे resume में Java 8+, Spring Boot, Spring Security, JPA/Hibernate, Angular, PostgreSQL, AWS (EC2, Route 53, IAM, CloudWatch), Jenkins, Elasticsearch/Kibana, JUnit 5, Mockito और GitHub Copilot हैं। साथ ही end-to-end banking onboarding, external integrations और production support भी हैं। 
Rishabh_SNW.pdf
 Rishabh_SNW.pdf

1. पहले समझो: Infosys interview में किस पर सबसे ज्यादा focus करना है?

1. Core Java + Multithreading
Very high priority
Collections, HashMap internals, Java 8 Streams, concurrency, ExecutorService, equals/hashCode, exception handling, JVM basics.




2. Spring Boot + Spring Security + JPA
Very high priority
Dependency injection, REST APIs, JWT, transactions, lazy loading, N+1 problem, exception handling और API design.




3. AWS + Deployment + Troubleshooting
Resume-specific priority
EC2, IAM, Route 53, CloudWatch, Jenkins deployment, application logs, server down होने पर debugging। तुम्हारे resume में AWS hands-on exposure लिखा है, इसलिए actual usage पर follow-up संभव है।




4. Angular + TypeScript
High priority
Components, services, dependency injection, RxJS, Observable vs Promise, HTTP interceptor, routing, forms और API integration.




5. SQL + Project Scenarios + Coding
High priority
Joins, indexes, query optimization, production incidents, external API failures, unit testing और practical coding.



यह Infosys के actual interview questions का verified paper नहीं है; यह तुम्हारे resume, 3+ years experience level और senior-developer role के आधार पर probability-ranked preparation list है।
हमारी preparation के हिसाब से labels
- PREPARED
   — इस topic पर हम पहले detail में काम कर चुके हैं।
- REVISE
   — topic पहले पढ़ा है, लेकिन interview में बोलने की practice जरूरी है।
- NEW / VERIFY
   — resume से निकलने वाला संभावित follow-up, जिसे पहले से तैयार मानकर नहीं चलना चाहिए।
किसी topic पर पहले चर्चा हो चुकी है, इसका मतलब यह नहीं कि उसका हर possible follow-up भी तैयार है।
2. Master list — 100 probable interview questions
A. Core Java + Advanced Java — Questions 1–20
Priority: P0 — पहले इन्हें complete करो।
#	Interview question	Status
1	Explain the four OOP principles with real project examples.	PREPARED
2	How does HashMap work internally in Java 8? Explain hashing, collisions, buckets and treeification.	PREPARED
3	What is the contract between equals() and hashCode()? What happens if you override only one?	PREPARED
4	Difference between ArrayList, LinkedList, HashSet and HashMap.	PREPARED
5	Difference between String, StringBuilder and StringBuffer. Why is String immutable?	PREPARED
6	Difference between == and equals() for Strings and objects.	PREPARED
7	Explain checked vs unchecked exceptions. When would you create a custom exception?	PREPARED
8	Explain Java 8 Stream API, intermediate vs terminal operations and lazy evaluation.	PREPARED
9	Difference between map() and flatMap() in Streams.	REVISE
10	Difference between filter(), map(), reduce() and collect().	REVISE
11	How do you handle duplicate elements, grouping and sorting using Streams?	REVISE
12	What are functional interfaces, lambda expressions and method references?	REVISE
13	Difference between synchronized, volatile, AtomicInteger and Lock.	PREPARED
14	What is a race condition? How do you prevent it?	PREPARED
15	Explain ExecutorService, thread pools, Callable, Future and CompletableFuture.	PREPARED
16	How would you process 1,000 concurrent tasks safely? How do you select the pool size?	REVISE
17	What is deadlock? How can you detect and prevent it?	REVISE
18	Explain JVM memory areas, heap vs stack, garbage collection and memory leaks.	REVISE
19	What are Java 8 features, and what changes in Java 17/21 matter to backend development?	REVISE
20	What is the difference between Comparable and Comparator?	REVISE

Highest priority within this section: 2, 3, 8, 13, 15 and 16. Your concurrency and multithreading preparation is particularly relevant to a backend role.
B. Spring Boot + Spring Security + REST APIs — Questions 21–35
#	Interview question	Status
21	Explain the Spring Boot request lifecycle, from controller to database and back.	REVISE
22	Difference between @Component, @Service, @Repository and @Controller.	REVISE
23	What is dependency injection? Constructor injection vs field injection?	REVISE
24	How does Spring Boot auto-configuration work? What does @SpringBootApplication do?	REVISE
25	How do you implement global exception handling using @ControllerAdvice?	PREPARED
26	How do you validate API requests using Bean Validation annotations?	PREPARED
27	Explain JWT authentication and authorization in Spring Security.	PREPARED
28	How does the Spring Security filter chain work? Where would a JWT filter fit?	REVISE
29	Difference between authentication and authorization. How would you implement role-based access?	PREPARED
30	Explain @Transactional, transaction propagation and rollback behaviour.	PREPARED
31	What happens when you call a @Transactional method from another method in the same class?	NEW / VERIFY
32	Why use DTOs instead of exposing JPA entities directly?	REVISE
33	How do you design REST APIs with proper HTTP methods, status codes and idempotency?	PREPARED
34	How would you secure an API against unauthorized access, invalid input and sensitive-data exposure?	REVISE
35	How do you handle timeouts, retries, circuit breakers and failures when calling external REST APIs?	PREPARED

Most likely follow-ups: 27–31 and 35. Be ready to explain not just annotations, but exactly what happens at runtime.
C. Hibernate + JPA + PostgreSQL / SQL — Questions 36–45
#	Interview question	Status
36	Difference between JPA, Hibernate and JDBC.	PREPARED
37	Explain lazy vs eager fetching and the N+1 query problem.	PREPARED
38	Difference between first-level and second-level Hibernate cache.	REVISE
39	Difference between optimistic and pessimistic locking.	PREPARED
40	Explain ACID properties and transaction isolation levels.	REVISE
41	What is an index? How do composite indexes improve SQL performance?	PREPARED
42	How do you read EXPLAIN ANALYZE and investigate a slow PostgreSQL query?	PREPARED
43	Write a SQL query to find the second-highest salary.	REVISE
44	Find duplicate records, the highest salary in each department, or customers without matching records.	REVISE
45	Explain offset pagination vs keyset pagination. When would you use each?	PREPARED

Your advantage: You have already prepared a concrete SQL optimization story involving EXPLAIN ANALYZE, a sequential scan, and a composite index on (status, created_at DESC). Revise the before/after reasoning and don't claim a performance improvement unless you can substantiate it.
D. Angular + TypeScript — Questions 46–55
#	Interview question	Status
46	Explain Angular components, modules, services and dependency injection.	REVISE
47	Difference between Promise and Observable.	PREPARED
48	What is an Angular HTTP interceptor? How would you attach an authorization token?	PREPARED
49	Explain Angular routing, route parameters, guards and lazy loading.	REVISE
50	Reactive forms vs template-driven forms. How do you validate a form?	REVISE
51	How do you call Spring Boot APIs from Angular and handle errors?	REVISE
52	Explain switchMap, mergeMap, concatMap and exhaustMap.	REVISE
53	What causes memory leaks in Angular subscriptions? How do you prevent them?	NEW / VERIFY
54	Explain change detection, OnPush and performance optimization.	NEW / VERIFY
55	How do you prevent duplicate API calls and manage loading/error states in a multi-step onboarding form?	NEW / VERIFY

Angular is a real resume keyword, so don't ignore it even if you are targeting a backend-heavy role. Prepare at least one example of a form, an API integration, and error handling from your actual work.
E. AWS + Cloud + Deployment — Questions 56–70
Priority: P0 — this is the area to strengthen most deliberately because AWS is explicitly listed in the job profile and resume.
#	Interview question	Status
56	Explain your actual AWS project architecture. Where was the application hosted and how did requests reach it?	NEW / VERIFY
57	What is Amazon EC2? How do you deploy a Java/Spring Boot application on it?	REVISE
58	What is IAM? Explain users, roles, policies and least-privilege access.	REVISE
59	What is Route 53? Explain DNS resolution and how a domain points to an application.	REVISE
60	What is Amazon CloudWatch? Explain metrics, logs, alarms and troubleshooting.	REVISE
61	What is a Security Group? How is it different from a network ACL?	NEW / VERIFY
62	How would you securely expose a Spring Boot application hosted on EC2?	NEW / VERIFY
63	How would you troubleshoot an EC2 instance that is running but the API is not accessible?	NEW / VERIFY
64	How do you configure environment variables, database credentials and secrets for a cloud deployment?	NEW / VERIFY
65	Explain load balancing, health checks and high availability in AWS.	REVISE
66	What is the difference between horizontal and vertical scaling?	REVISE
67	Explain an end-to-end Jenkins CI/CD pipeline for deploying a Java application to EC2.	REVISE
68	How would you investigate an application that worked before deployment but fails after a new release?	PREPARED
69	How do you monitor CPU utilization, application errors and instance health?	REVISE
70	What is the difference between CloudWatch and Elasticsearch/Kibana? When would you use each?	PREPARED

Important: Questions 56–64 are the ones I would prioritize most. Your resume specifically mentions EC2 hosting, Route 53, IAM, CloudWatch and AWS-based Jenkins deployments. 
Rishabh_SNW.pdf

For AWS, prepare an honest explanation of what you personally configured, what you used, and what you only observed or supported. Expect follow-ups such as “Which port?”, “Where did the logs go?”, “How was access controlled?” and “What did you do when it failed?”
F. Banking onboarding project + System design + Production support — Questions 71–85
Priority: P0 — your project explanation can generate multiple follow-up questions.
#	Interview question	Status
71	Explain your banking onboarding project architecture end to end.	PREPARED
72	What exactly was your individual contribution, and which modules did you implement?	REVISE
73	Explain the complete onboarding flow from the Angular UI to Spring Boot, database and external services.	PREPARED
74	How did you integrate OTP/SMS, identity verification, OCR and national address verification services?	PREPARED
75	What happens if an external API times out, returns an error or responds slowly?	PREPARED
76	What is idempotency? How did you prevent duplicate processing in onboarding?	PREPARED
77	What is the Outbox pattern? Why can a database update and message publication fail independently?	PREPARED
78	Explain the Saga pattern. What is the difference between orchestration and choreography?	PREPARED
79	How would you handle concurrent requests updating the same customer application?	PREPARED
80	How do you troubleshoot a production incident using logs, SQL queries and application debugging?	PREPARED
81	Explain your SQL performance optimization incident, including the root cause and fix.	PREPARED
82	How would you diagnose an LDAP/authentication error or an IAM integration failure?	REVISE
83	How would you design retries without creating duplicate OTP requests or duplicate customer records?	PREPARED
84	How do you handle partial failures across multiple services in a customer onboarding workflow?	PREPARED
85	How do you ensure sensitive customer data is not exposed in logs or API responses?	REVISE

For this section, use the actual project stories we have worked on: duplicate processing and idempotency, SQL optimization, OCR date normalization, and production troubleshooting. These can demonstrate practical problem-solving rather than only theoretical knowledge.
G. Practical coding — Questions 86–95
Priority: P0 — practise writing code, not just explaining the solution.
#	Coding question	Status
86	Find duplicate characters in a String using Java 8 Streams.	REVISE
87	Find the first non-repeating character in a String.	REVISE
88	Find duplicate elements in a List using Streams or a Set.	REVISE
89	Find the second-highest number in a List using Streams.	REVISE
90	Count the frequency of each word or character using Collectors.groupingBy().	REVISE
91	Sort employees by salary, then by name, using Java 8.	REVISE
92	Given a list of employees, group them by department and find the highest-paid employee in each department.	NEW / VERIFY
93	Implement a thread-safe counter for multiple concurrent threads.	REVISE
94	Merge two sorted arrays or find a missing number in an array.	REVISE
95	Write a SQL query using joins and aggregation to find the highest-paid employee per department.	REVISE

Don't spend equal time on every coding problem. For this round, focus on Streams, Collections, HashMap, basic array/string problems, and explaining time and space complexity.
H. Testing + CI/CD + GitHub Copilot + Engineering practices — Questions 96–100
#	Interview question	Status
96	Difference between unit testing, integration testing and end-to-end testing.	REVISE
97	How do JUnit 5 and Mockito work together? Explain mocks, stubs and verification.	PREPARED
98	How would you test a Spring Boot service that calls an external API?	REVISE
99	Explain your Jenkins pipeline, Maven build, test execution and SonarQube quality checks.	REVISE
100	How do you use GitHub Copilot for code generation, debugging and unit tests while ensuring code quality and security?	NEW / VERIFY

Questions 96–100 are not necessarily more likely than Core Java or AWS. They are included because your resume explicitly lists testing, CI/CD, SonarQube and GitHub Copilot certification. Be prepared to substantiate your actual usage of these tools. 
Rishabh_SNW.pdf
 Rishabh_SNW.pdf


3. The 25 questions I would prepare first
If time becomes tight, do these before trying to finish all 100. This is my recommended order based on your resume and the target role, not a claim about Infosys's exact question distribution.
Priority revision checklist
0/25



[ ] 2. HashMap internals, collision handling and treeification

Java


[ ] 3. equals() and hashCode() contract

Java


[ ] 8. Java 8 Streams and lazy evaluation

Java


[ ] 13. synchronized vs volatile vs AtomicInteger

Concurrency


[ ] 15. ExecutorService and thread pools

Concurrency


[ ] 16. Processing 1,000 concurrent tasks

Concurrency


[ ] 21. Spring Boot request lifecycle

Spring


[ ] 27. JWT authentication in Spring Security

Security


[ ] 28. Spring Security filter chain

Security


[ ] 30. @Transactional and rollback behaviour

Spring


[ ] 31. Self-invocation and @Transactional proxy behaviour

Spring


[ ] 35. External API timeouts, retries and circuit breakers

Integration


[ ] 37. Lazy loading and N+1 queries

JPA


[ ] 41. Composite indexes and query performance

SQL


[ ] 47. Promise vs Observable

Angular


[ ] 48. HTTP interceptor and authorization token

Angular


[ ] 52. switchMap vs mergeMap vs concatMap vs exhaustMap

Angular


[ ] 56. Your real AWS architecture and personal contribution

AWS


[ ] 57. Deploying Spring Boot on EC2

AWS


[ ] 58. IAM roles, policies and least privilege

AWS


[ ] 60. CloudWatch logs, metrics and alarms

AWS


[ ] 63. EC2 running but API inaccessible: troubleshooting

AWS


[ ] 67. Jenkins CI/CD deployment to EC2

DevOps


[ ] 76. Idempotency and duplicate processing

Project


[ ] 81. SQL optimization incident and EXPLAIN ANALYZE

Project


Reset checklistStart mock interview

4. Two-day preparation strategy
तुम्हारे पास लगभग 1–2 दिन हैं, इसलिए मेरा suggestion है कि पहला दिन technical depth पर और दूसरा दिन AWS + project explanation + mock interview पर रखो।
Day 1 — Core technical revision
2 hrs
Java
Core Java + Concurrency

Questions 1–20 में से HashMap, equals/hashCode, Streams, ExecutorService, synchronization और JVM।


1.5 hrs
Backend
Spring Boot + Security + JPA

Questions 21–42. खासकर JWT, transactions, external API failures, lazy loading और indexing।


1 hr
Coding
Live coding practice

Questions 86–95 में से कम से कम 4 problems बिना notes देखे solve करो।



Day 2 — AWS + Full Stack + interview delivery
2 hrs
AWS
AWS fundamentals + deployment troubleshooting

Questions 56–70. EC2, IAM, Route 53, CloudWatch, security groups और Jenkins pipeline।


1 hr
Angular
Angular + TypeScript

Questions 46–55. Interceptors, Observables, RxJS operators, routing और forms।


1.5 hrs
Project
Project stories + mock interview

Questions 71–85. अपनी architecture, individual contribution, incidents, root cause और fixes को aloud explain करो।


30 min
Final
Resume rapid revision

Introduction, AWS architecture, two production incidents, one SQL optimization story और GitHub Copilot usage।



अगर तुम्हारे पास दोनों दिन पूरे नहीं हैं, तो Core Java, Spring Security, AWS architecture/troubleshooting, project incidents और 4 coding questions पहले complete करना।
5. Face-to-face interview के लिए पांच चीजें जरूर तैयार रखना
1. Tell me about yourself: 60–90 seconds में, Java Full Stack experience और project contribution पर focus।
2. Explain your project architecture: Frontend → REST API → service layer → database → external integrations का flow।
3. Explain one difficult production issue: Symptom → root cause → investigation → fix → verification।
4. Explain AWS deployment: Application कहाँ host थी, request कैसे पहुंचती थी, access कैसे controlled था और logs कहाँ check करते थे।
5. Why should we hire you as a Senior Developer? Ownership, debugging, secure APIs, database optimization और full-stack understanding के concrete examples के साथ।
एक खास बात: तुम्हारे resume में AWS को hands-on exposure और deployment support के रूप में लिखा गया है। इसलिए AWS के answers को वास्तविक experience के अनुसार रखना। अगर किसी service का implementation खुद नहीं किया है, तो उसे personally implemented बताने के बजाय concept और अपनी involvement स्पष्ट करना।

मेरी final recommendation: 100 questions को एक बार पढ़कर खत्म करने की कोशिश मत करो। पहले 25 priority questions पर मजबूत answers तैयार करो, फिर बाकी list में से नए questions चुनो। हमने पहले से काफी technical groundwork किया है; अब सबसे ज्यादा फायदा targeted revision, clear communication और practical follow-ups की practice से मिलेगा।