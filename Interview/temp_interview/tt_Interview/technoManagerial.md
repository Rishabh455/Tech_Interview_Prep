## What I changed after re-evaluating the questions

The previous list had too many generic behavioral questions and not enough questions about day-to-day work, database implementation, Agile, recent functionality and bugs. These topics appear in reported Infosys Java interview experiences, including a November 2025 managerial-round account and an April 2025 Java Developer interview account.&#x20;

[image](https://www.google.com/s2/favicons?domain=https://www.glassdoor.co.in\&sz=32)

Glassdoor

+2



For your interview, I would use this revised set of 25:

| Priority | Questions | Main focus                                                                                                 |
| -------- | --------- | ---------------------------------------------------------------------------------------------------------- |
| P0       | 1–6       | Introduction, project, individual contribution, daily work, database and recent functionality              |
| P0       | 7–12      | Difficult problems, production bugs, SQL optimization, OCR mismatch, idempotency and external integrations |
| P0       | 13–16     | Security, code quality, Agile delivery and deadline management                                             |
| P1       | 17–20     | Conflict, failure and learning, prioritization, AWS deployment issue                                       |
| P1       | 21–25     | Actual AWS exposure, stakeholder communication, team ownership, why Infosys and why you                    |

One important adjustment: because you have around three years of experience and are not claiming to be a team lead, your answers should demonstrate independent technical ownership, collaboration and sound judgment, rather than suggesting that you manage the whole team.

The answers below are in English and written for direct interview use. For follow-ups, I have included the likely question and a ready answer, not merely a list of topics.

# Part 1 — Project, ownership and problem-solving

## 1. Tell me about yourself and your current responsibilities.

Answer:

“I am a Java Full Stack Developer at Tata Consultancy Services, with over three years of experience in enterprise application development and production support.

My primary experience is in developing banking customer onboarding workflows using Java, Spring Boot, REST APIs, PostgreSQL, JPA/Hibernate, Spring Security and Angular.

In my current project, I work on multi-step onboarding functionality involving mobile verification, identity verification, address validation, additional customer information and credential creation. My responsibilities include developing backend APIs, implementing business logic and validations, integrating external services, working with the database, and resolving production and UAT issues.

I also have exposure to AWS services such as EC2, IAM, Route 53 and CloudWatch, along with Jenkins-based deployments and Elasticsearch/Kibana monitoring.

I am now looking for an opportunity where I can deepen my backend engineering and cloud skills while taking greater ownership of application development and reliability.”

### Likely cross-questions

Q1. What is your strongest technical area?

“My strongest area is Java backend development using Spring Boot and REST APIs. I am comfortable implementing business logic, handling validation and exceptions, integrating external services, working with JPA/Hibernate and PostgreSQL, and debugging issues across application layers.”

Q2. You are a full-stack developer. Are you more interested in Java or Angular?

“My primary strength and career focus are Java backend development. However, my Angular experience helps me understand the complete request flow, troubleshoot frontend-to-backend integration issues and deliver functionality across the application.”

## 2. Explain your project architecture from end to end.

Answer:

“I am working on an enterprise banking customer onboarding platform for the Saudi market. The application enables customers to complete onboarding through multiple verification and information-capture steps.

At a high level, the Angular frontend communicates with backend REST APIs developed using Spring Boot. The backend follows a layered structure with controller, service and repository layers. Controllers handle incoming requests, service classes implement business logic, and repositories interact with PostgreSQL through JPA/Hibernate.

The workflow includes mobile verification, identity verification, address validation, additional customer information and credential creation. We integrate with external systems for OTP/SMS, identity or OCR validation, national address verification and internal IAM functionality.

Spring Security supports authentication and authorization, while request validation and exception handling help ensure reliable API behaviour. We also use centralized logging and monitoring to investigate application issues and support production releases.”

### Likely cross-questions

Q1. What happens when the user submits an onboarding form?

“The Angular application sends an HTTP request to the relevant REST endpoint. The controller receives the request and validates it, the service layer applies the business rules, and the repository performs database operations when required. The backend returns a suitable response to Angular, which updates the UI or displays a validation or error message. External verification steps are handled through their respective integrations.”

Q2. Why do you separate controller, service and repository layers?

“The controller handles HTTP-related responsibilities, the service layer maintains business logic, and the repository handles persistence. This separation improves maintainability, testability and reuse, and makes it easier to modify one responsibility without unnecessarily affecting the others.”

Q3. Is your entire system a microservices architecture?

“I can describe the specific components and integrations within my scope accurately. The important distinction is between the Spring Boot APIs and application layers I worked on and the external services they communicate with. I would not claim ownership of the entire architecture beyond my actual responsibilities.”

## 3. Explain your day-to-day activities as a Java Developer.

Answer:

“My day typically starts with reviewing sprint tasks, current priorities, blockers and any production issues.

For a development task, I first understand the business requirement and acceptance criteria. I then analyse the existing flow, identify the affected APIs, services and database operations, and implement the required changes using Java and Spring Boot.

I also work on request validation, exception handling, JPA/Hibernate integration and Angular-to-backend API integration where necessary. After development, I write or update unit tests using JUnit and Mockito, review the changes, and support integration or UAT testing.

For release activities, I contribute to build and deployment processes and investigate issues using application logs, database queries, Elasticsearch/Kibana and the available AWS monitoring tools. My work covers the SDLC from requirement analysis through development, testing, deployment support and production troubleshooting.”

### Likely cross-questions

Q1. How does a task move through your Agile process?

“Typically, a requirement is discussed during backlog refinement or sprint planning. The team agrees on the scope and acceptance criteria, develops the functionality during the sprint, reviews and tests the changes, and resolves any defects identified in integration or UAT testing. The work is demonstrated and completed once the agreed acceptance criteria are met.”

Q2. What do you do when you cannot complete a task because of a dependency?

“I identify the dependency early, understand its impact on the timeline, and communicate the blocker to the relevant team member or manager. I continue with any independent work that can be completed and update the estimate when the dependency becomes clear.”

## 4. What exactly is your individual contribution to the project?

Answer:

“My contribution is primarily in backend development, application integration and issue resolution.

I develop and maintain Spring Boot REST APIs for onboarding workflows, implement business rules and validation, work with DTOs and the service/repository layers, and persist or retrieve data using JPA/Hibernate and PostgreSQL.

I also contribute to integrating the frontend with backend APIs and connecting the application with external verification services. During testing and production support, I analyse application logs, investigate database behaviour, debug Java code and implement fixes for identified issues.

For example, I have worked on resolving a database performance problem by analysing the query execution plan and introducing a suitable composite index. I have also worked on data-normalization and duplicate-processing scenarios in the onboarding workflow.

I contribute to the team's delivery, while architectural decisions and work outside my scope remain collaborative responsibilities.”

### Likely cross-questions

Q1. Did you lead the entire project or manage a team?

“My role has been primarily as an individual contributor. I work with other developers, testers and stakeholders to deliver functionality and resolve issues. I take ownership of the tasks and problems assigned to me, but I would not describe myself as the overall project lead.”

Q2. Which task are you most confident handling independently?

“I am most confident handling a backend feature from understanding the requirement through API implementation, validation, persistence, testing and debugging. For problems involving infrastructure or another team's service, I investigate the issue within my access and collaborate with the appropriate team.”

## 5. What is the latest functionality you worked on? Explain its implementation.

Answer:

“One of the key areas of my project is the multi-step customer onboarding workflow.

The frontend collects customer information and calls the backend APIs corresponding to the relevant onboarding steps. On the backend, I work with Spring Boot controllers, service-layer business logic, DTOs, validation and database interactions.

For verification steps, the workflow integrates with external services such as OTP/SMS, identity or OCR validation, address verification and internal IAM functionality. The backend processes the responses, validates the required information and supports the next appropriate step in the onboarding flow.

My focus is on ensuring that each API handles valid and invalid input correctly, returns consistent responses, and does not leave the workflow in an inconsistent state when an integration or database operation fails.”

### Likely cross-questions

Q1. How do you validate data coming from Angular?

“I validate the data on both the frontend and backend. Angular provides immediate feedback to users, but backend validation is essential because client-side validation can be bypassed. I use appropriate Bean Validation annotations and service-level business rules before allowing the requested operation to proceed.”

Q2. How do you handle a failed verification step?

“I handle the failure based on its type. A validation failure should return a clear business response. A temporary dependency failure should be handled using the appropriate timeout and retry policy, where retries are safe. The application should preserve a consistent workflow state and avoid creating duplicate records or repeating non-idempotent operations.”

## 6. Which database do you use, and how is it implemented in your project?

Answer:

“We use PostgreSQL for the banking onboarding application.

The backend uses JPA/Hibernate for object-relational mapping. Entity classes represent persistent data, repositories provide database access, and the service layer coordinates business operations and transactions.

When a request is processed, the service layer applies business validations and invokes the relevant repository operations. We use database constraints and appropriate transaction boundaries to maintain data integrity. For performance-sensitive queries, I examine the SQL execution plan and consider indexing, filtering and sorting requirements rather than assuming that ORM-generated queries are efficient by default.

For example, I investigated a query that retrieved onboarding cases requiring review and sorted them by creation time. The query plan showed a sequential scan followed by a top-N sort. A composite index on status and descending creation time was used to better support the query pattern.”

### Likely cross-questions

Q1. Why did you use PostgreSQL?

“PostgreSQL provides relational data modelling, transactions, constraints, indexing and query-planning capabilities suitable for structured banking workflows. It also supports the data integrity and query-performance requirements of this type of application.”

Q2. How do you investigate a slow database query?

“I first identify the slow query and reproduce the relevant conditions. I use `EXPLAIN ANALYZE` to inspect the execution plan, including scans, filters, sorting and estimated versus actual work. Based on the findings, I assess whether an appropriate index, a query change or a reduction in unnecessary data retrieval would help. I then compare the execution behaviour after the change.”

Q3. What is your role in database design?

“My experience includes working with entities, repository queries, persistence behaviour and performance optimization. I contribute to database changes required by my assigned features, while broader schema decisions may involve the wider engineering team.”

# Part 2 — Difficult problems and production incidents

## 7. What is the most challenging technical problem you have worked on?

Answer:

“One challenging area has been maintaining data consistency in an onboarding workflow that interacts with multiple services.

The workflow depends on customer requests, database operations and external verification responses. Failures, retries or repeated submissions can create the risk of duplicate processing if the backend does not handle them correctly.

My approach is to identify the exact point where the workflow becomes inconsistent, trace the request through application logs and database records, and distinguish between a frontend retry, a repeated request and a genuine new operation.

For duplicate-processing scenarios, we strengthened the handling using idempotency keys and database-level uniqueness constraints. The database remains the final protection against duplicate records, rather than relying only on a frontend check.

This taught me that reliable business workflows need protection at the application and database levels, particularly when requests may be repeated.”

### Likely cross-questions

Q1. How do you distinguish a repeated request from a new request?

“I use an idempotency key associated with the logical operation. If the same operation is submitted again with the same key, the backend can identify it instead of creating a second operation. The key must be stored and checked consistently, and the database should enforce uniqueness where appropriate.”

Q2. Is adding an idempotency key enough to prevent duplicates?

“No. The implementation must also handle concurrent requests and database transactions correctly. A database constraint provides an additional safeguard, while the application must define how to handle an already-processed request and what response to return.”

## 8. Tell me about a production bug you identified and fixed.

Answer:

“One issue we investigated in the onboarding workflow involved inconsistent date formats returned during document processing.

The OCR/document-processing response could contain a date in `MM-DD-YYYY` format, while the backend validation expected `YYYY-MM-DD`. As a result, a valid document could fail downstream validation because the same date was represented in a different format.

I traced the issue to the mismatch between the external response and the backend's expected format. The fix was to normalize the date into the expected format before downstream validation and processing. We also considered validation behaviour for invalid or ambiguous date values so that incorrect data would not be silently accepted.

The key learning was that integrating an external service requires explicit validation and normalization at the system boundary. A technically successful API response does not automatically mean that its data is ready for our business logic.”

### Likely cross-questions

Q1. Why did you not directly accept the external date format?

“Different systems can have different data contracts. Rather than propagating format assumptions through the application, it is safer to normalize the external response into our internal canonical format at the integration boundary.”

Q2. What if the OCR confidence score is low?

“If the configured confidence threshold is not met, the document should not be accepted automatically. In our prepared approach, a confidence score below the 85% threshold triggers a request for another upload or an appropriate review path. The precise threshold and subsequent action must match the project's actual configured rules.”

## 9. Explain your SQL performance optimization incident.

Answer:

“We investigated a PostgreSQL query used to retrieve onboarding cases with the `REVIEW_REQUIRED` status, ordered by `created_at` in descending order.

Before optimization, `EXPLAIN ANALYZE` showed a parallel sequential scan followed by a top-N heapsort. This indicated that PostgreSQL was scanning many rows and then sorting the qualifying records to return the required results.

We introduced a composite index:

```
CREATE INDEX idx_onboarding_status_created
ON onboarding_case (status, created_at DESC);
```

The index was aligned with the query's equality filter on `status` and its descending order on `created_at`. We then checked the execution plan to verify whether the index improved access to the relevant records.

The main lesson was to use execution-plan evidence to identify the bottleneck and choose an index based on the actual query pattern, rather than adding indexes without analysis.”

### Likely cross-questions

Q1. Why did you put `status` before `created_at` in the index?

“The query filters by a specific status and then orders the matching rows by creation time. Putting the equality-filter column first allows the index to narrow the relevant portion of the data, while the second column supports the required ordering.”

Q2. Does creating an index always improve performance?

“No. An index has storage and write-maintenance costs, and it may not help a query if the planner determines that a sequential scan is cheaper. I validate it against the actual workload and execution plan.”

## 10. How do you approach root-cause analysis when an issue is reported?

Answer:

“I follow a structured process instead of immediately changing code.

First, I understand the symptom, expected behaviour, actual behaviour and business impact. I then reproduce the issue where possible and inspect the relevant application logs, request/response details, database records and recent changes.

Next, I identify the failing layer—frontend, API, business logic, database or external integration—and use evidence to narrow down the root cause. Once I have a likely cause, I implement the smallest appropriate fix, run relevant tests and verify that the original issue is resolved without introducing regressions.

For example, in the SQL optimization issue, I used `EXPLAIN ANALYZE` to identify the scan-and-sort behaviour before choosing the index. In the date-format issue, I traced the failure to the integration data contract rather than treating it as a generic validation error.”

### Likely cross-questions

Q1. What if you cannot reproduce a production issue locally?

“I preserve the available evidence, including timestamps, request identifiers, logs and environment details. I compare production configuration and input data with the test environment, and I investigate whether the issue depends on concurrency, environment-specific behaviour or external dependencies. I avoid making an unverified production change just to try something.”

Q2. How do you prevent an issue from recurring?

“Depending on the root cause, I add regression tests, stronger validation, database constraints, better error handling or monitoring. I also ensure the fix is documented and verified in the appropriate test environment.”

## 11. How did you address duplicate processing in the onboarding workflow?

Answer:

“We identified duplicate processing during production investigation. The issue highlighted that a request could be submitted or processed more than once, and that application logic alone was not a sufficient protection against duplicate operations.

We addressed this by introducing an idempotency key and a database-level uniqueness constraint. The backend identifies repeated logical operations using the key, while the database constraint provides a final safeguard against duplicate records.

The important design point is that a repeated request must not create a second business operation. The implementation also needs appropriate handling for concurrent submissions and a consistent response for requests that have already been processed.

We learned that idempotency is not something to rely on only at the frontend or HTTP client level; it must be part of the backend and persistence design where the operation requires it.”

### Likely cross-questions

Q1. Why was idempotency not implemented initially?

“Initially, the workflow handled the expected request path, and the duplicate-processing scenario was not sufficiently covered. When the issue became visible during production investigation, we addressed it by adding explicit idempotency handling and a database constraint. The learning was to evaluate retries, repeated submissions and concurrent requests while designing critical workflows, not only after an incident.”

Q2. What happens if two identical requests arrive at the same time?

“Both requests may initially reach the application, so a simple check-then-insert sequence is not enough. The implementation needs atomic persistence behaviour and a unique constraint to prevent both from creating the same logical record. The losing request should handle the duplicate outcome appropriately rather than returning an uncontrolled server error.”

Q3. Does idempotency mean the API can be called repeatedly without any consequences?

“It means repeated requests representing the same logical operation produce the intended equivalent outcome without performing the business operation multiple times. It does not mean every type of request can be repeated safely without a defined idempotency design.”

## 12. How do you handle failures in external API integrations?

Answer:

“In the onboarding platform, integrations such as OTP/SMS, identity or OCR validation, address verification and internal IAM introduce dependencies outside the immediate application code.

I first distinguish between a business rejection, a timeout, a connectivity problem and an unexpected response. A business validation failure should be handled as a business outcome rather than blindly retried. For transient failures, I would use bounded retries with backoff where the operation is safe to retry, along with appropriate timeouts.

I also validate external responses before using the data, normalize formats where required, log sufficient diagnostic information without exposing sensitive customer data, and ensure failures do not leave the database in an inconsistent state.

For operations such as OTP dispatch or customer creation, retry behaviour must also account for duplicate side effects.”

### Likely cross-questions

Q1. Would you retry every failed API request?

“No. I would not automatically retry validation errors or other permanent business failures. For transient errors, I would retry only when it is safe, with bounded attempts and backoff. For non-idempotent operations, I would first ensure that a retry cannot trigger duplicate business effects.”

Q2. How would you prevent an external service from slowing down the entire workflow?

“I would configure appropriate connection and response timeouts, handle failures cleanly, and use circuit-breaker behaviour where suitable. The workflow should return an appropriate response or enter a defined recoverable state instead of waiting indefinitely for a dependency.”

# Part 3 — Security, quality and delivery management

## 13. How do you ensure security in a banking application?

Answer:

“I approach security at multiple layers.

At the API layer, Spring Security is used for authentication and authorization. Requests must be permitted only when the caller has the required access. I also validate incoming data on the server side and avoid exposing internal implementation details through error responses.

For persistence and business operations, I pay attention to transaction boundaries and data integrity. Sensitive customer information should not be unnecessarily exposed in application logs or API responses, and access to credentials and infrastructure should follow least-privilege principles.

In a multi-step onboarding workflow, I also consider whether the customer is authorized to access the particular application or operation being requested, rather than assuming that a valid login alone grants access to all resources.”

### Likely cross-questions

Q1. What is the difference between authentication and authorization?

“Authentication verifies who the caller is. Authorization determines what that authenticated caller is allowed to access or perform.”

Q2. How would you handle a sensitive value appearing in application logs?

“I would assess the exposure, follow the incident process, and remove or mask the sensitive value from logging where possible. I would also review affected log access and retention, and ensure the fix is covered by testing. Simply deleting the logging statement without considering existing exposure would not be sufficient.”

## 14. How do you ensure code quality and sufficient test coverage?

Answer:

“I try to prevent defects at multiple stages rather than depending only on UAT or production testing.

I keep business logic in the appropriate service layer, validate input at API boundaries, handle exceptions consistently and write unit tests for important success and failure scenarios. I use JUnit 5 and Mockito for unit testing and mocking dependencies. Where integration behaviour matters, I also support integration testing.

During code review, I look for correctness, readability, error handling, duplicated logic, data integrity and possible regressions. SonarQube and the existing CI/CD quality checks can also help identify issues that require attention.

For a change that fixes a production issue, I add a regression test where practical so that the same scenario is checked in future builds.”

### Likely cross-questions

Q1. What would you test for an onboarding verification API?

“I would test valid input, missing or invalid fields, failed verification, successful verification, external API timeout, malformed external responses, database failures where testable, and repeated requests if the operation requires idempotency.”

Q2. Is 100% code coverage enough to guarantee quality?

“No. Coverage tells us which code was executed by tests, but it does not guarantee that the assertions are correct or that important scenarios were tested. I focus on meaningful tests covering business behaviour, failure paths, edge cases and critical integration boundaries.”

## 15. How do you work in Agile, and how do you manage your sprint commitments?

Answer:

“Our work follows the SDLC and Agile delivery process. Requirements are discussed and broken into manageable tasks, and the team agrees on the scope and acceptance criteria during planning.

During development, I track my tasks, raise dependencies early and share progress or blockers with the team. I implement the functionality, complete the relevant unit tests, support code reviews and help resolve integration or UAT defects.

At the end of the sprint, we review the delivered functionality against the agreed acceptance criteria. If new information changes the estimate, I communicate the impact instead of silently allowing a commitment to slip.

My focus is to deliver working and tested functionality while keeping the team informed about risks and dependencies.”

### Likely cross-questions

Q1. What do you do in a daily stand-up?

“I briefly communicate what I completed, what I am working on next, and whether anything is blocking progress. If a complex issue needs a longer discussion, I arrange a separate discussion with the relevant people rather than taking the entire stand-up off track.”

Q2. How do you estimate a task?

“I break it into analysis, implementation, testing and integration work, and identify dependencies or uncertainty. I use the complexity of the change and relevant past experience to estimate it, and revise the estimate when new information materially changes the scope.”

## 16. What would you do if a deadline were at risk or requirements changed just before release?

Answer:

“I would first assess the remaining work, the reason for the delay and the impact on the release. I would communicate the risk early with a realistic estimate, rather than waiting until the deadline.

If requirements changed, I would evaluate the impact on the code, database, integrations, testing and regression risk. I would then discuss the available options with the manager or product owner—for example, adjusting scope, moving a non-critical item to a later release or revising the release plan.

I would not silently skip critical validation or testing to meet a date. The objective is to make a transparent delivery decision that balances business urgency, application stability and quality.”

### Likely cross-questions

Q1. What if your manager still asks you to deliver everything on time?

“I would explain the remaining work and risks with evidence, and offer realistic options. If the deadline is fixed, I would ask which scope or resources can be adjusted. I would follow the agreed decision while being clear about any remaining risk, rather than making an unrealistic commitment.”

Q2. Would you ever release a feature without all tests passing?

“I would not bypass critical tests or release controls without the appropriate assessment and authorization. If a deadline is at risk, I would communicate the impact and follow the team's release process to decide whether the release should be adjusted or postponed.”

# Part 4 — Behavioral scenarios and decision-making

## 17. Tell me about a disagreement with a teammate or manager. How did you handle it?

Answer:

“My approach to disagreement is to focus on the technical problem and the available evidence rather than treating it as a personal difference.

I first listen to the other person's reasoning and clarify the requirement or constraint behind the proposed approach. If I have a different view, I explain the trade-offs using evidence such as application behaviour, query execution plans, test results or maintainability concerns.

If the other approach is better after discussion, I am comfortable accepting it. If there is still disagreement, I involve the appropriate decision-maker and align with the agreed approach.

My objective is not to prove that my solution is always right. It is to help the team make a sound technical decision and deliver the requirement reliably.”

### Likely cross-questions

Q1. What if your manager insists on a solution you disagree with?

“I would respectfully explain the technical concern, its impact and any safer alternatives. If the decision remains within acceptable technical and security boundaries, I would align with it and execute it professionally. If it creates a significant security, data-integrity or production risk, I would escalate that risk through the appropriate process.”

Q2. Can you give me a real example?

“Use a genuine example from your experience rather than inventing a conflict. A technical discussion around SQL optimization, validation behaviour or error handling could be suitable if you personally disagreed with an approach and worked through it with the team. Explain what the disagreement was, what evidence you presented and how the decision was reached.”

## 18. Tell me about a mistake you made and what you learned from it.

Answer:

“One important learning from our onboarding work was the need to account explicitly for duplicate processing.

The initial implementation handled the expected request flow, but duplicate-processing protection was not sufficiently covered for all repeated-request scenarios. During production investigation, we observed that this needed stronger handling.

We addressed the gap by introducing an idempotency key and a database-level uniqueness constraint. This strengthened the backend protection against repeated operations and duplicate records.

My key learning was that a workflow should be designed not only for the successful path, but also for retries, concurrent requests and partial failures. Since then, I pay more attention to data integrity and idempotency when analysing critical business operations.

I see a production issue as an opportunity to improve the system and the development process, not simply as something to fix and forget.”

### Likely cross-questions

Q1. Why was this not caught during testing?

“The original test coverage did not sufficiently exercise the duplicate-processing scenario. The production investigation highlighted that gap, and it reinforced the need to include repeated requests and concurrency-related cases in testing for critical workflows.”

Q2. How do you ensure the same mistake is not repeated?

“I use the root cause to identify a preventive measure, such as a regression test, a database constraint, stronger validation or better monitoring. In this case, idempotency handling and a database-level constraint strengthened the prevention of duplicate processing.”

## 19. What would you do if a production incident occurred while you were working on a committed sprint task?

Answer:

“I would first assess the severity of the production incident, the affected functionality and the business impact. A critical issue affecting customers or data integrity would generally take priority over routine sprint development.

I would follow the incident process, collect evidence from logs and the database, identify the likely root cause and coordinate with the relevant teams. I would keep my manager informed about the incident and its impact on my sprint commitment.

Once the issue was stabilized and the immediate response was complete, I would reassess the remaining sprint work and agree on an updated plan. I would also document the root cause and any preventive actions.

The important points are to prioritize based on impact, communicate early and avoid making changes under pressure without appropriate validation.”

### Likely cross-questions

Q1. Would you abandon your sprint task completely?

“Not necessarily. I would communicate the reprioritization, preserve any completed work and resume the task when the incident is stable and priorities are agreed. The decision should be transparent rather than leaving the sprint commitment untracked.”

Q2. What if you cannot identify the root cause immediately?

“I would first work to contain the impact using the approved incident process, gather diagnostic evidence and involve the appropriate experts. A safe workaround or rollback may be more important than forcing an immediate permanent fix. I would continue the root-cause investigation after stabilization.”

## 20. Suppose the application becomes unavailable after an AWS deployment. What would you do?

Answer:

“I would approach the issue layer by layer.

First, I would confirm the impact and identify whether the issue started immediately after the deployment. I would check the deployment status, EC2 instance health and application process, followed by CloudWatch metrics and available logs.

Next, I would verify the application port, security group rules, network reachability, application configuration, database connectivity and external dependencies. I would also inspect the application logs for startup failures, missing configuration or exceptions.

If the new deployment is confirmed to be the cause and the previous version is known to be stable, I would follow the team's rollback or recovery procedure after coordinating with the responsible stakeholders. After service restoration, I would investigate the root cause and verify the fix before another deployment.”

### Likely cross-questions

Q1. The EC2 instance is running, but the REST API is not accessible. What would you check?

“I would distinguish instance health from application health. I would check whether the Java process is running, whether the application has started successfully, whether it is listening on the expected port, whether the security group and network path permit traffic, and whether the application can connect to the database and its dependencies.”

Q2. How would you use CloudWatch?

“I would inspect the available instance metrics, application or system logs where configured, and alarms around the time of the incident. I would correlate these with deployment timestamps and application exceptions to narrow down the failure.”

Q3. Have you personally architected a complete AWS environment?

“My experience is in AWS exposure and deployment support, including EC2-hosted application environments, IAM, Route 53, CloudWatch and Jenkins-based deployments. I would not claim to have independently architected the entire AWS environment. My focus has been on application deployment, monitoring and troubleshooting within my assigned responsibilities.”

# Part 5 — AWS experience, communication and career fit

## 21. What AWS services have you used, and what exactly did you do with them?

Answer:

“My AWS exposure is primarily related to application hosting, access control, traffic management and monitoring.

EC2 is relevant to running the application in the cloud. IAM is used to manage permissions and access according to the required roles and policies. Route 53 supports DNS management and routing users to the appropriate application endpoint. CloudWatch helps monitor configured metrics, logs and alarms.

I have also supported AWS-based CI/CD deployments using Jenkins, with application hosting on EC2 and monitoring and troubleshooting through CloudWatch.

My strongest contribution is on the application development and troubleshooting side. I understand the purpose of these services and how they relate to application delivery, while I distinguish that from owning the full cloud infrastructure architecture.”

### Likely cross-questions

Q1. What is the difference between IAM and a Security Group?

“IAM controls which identities can perform actions on AWS resources. A Security Group controls network traffic permitted to and from an associated resource, such as an EC2 instance. They address different types of access control.”

Q2. What is the difference between CloudWatch and Elasticsearch/Kibana?

“CloudWatch is AWS's monitoring and observability service for metrics, logs and alarms. Elasticsearch/Kibana provides centralized log searching, filtering and visualization in our application-monitoring setup. I would use whichever tool contains the relevant evidence for the issue being investigated.”

Q3. Can you describe your Jenkins deployment pipeline?

“At a high level, code changes are committed to version control, the pipeline builds the application using Maven, runs the configured tests and quality checks, and supports deployment to the target environment. After deployment, we validate the application and use the available logs and monitoring tools to investigate failures. The exact stages depend on the pipeline configured by the team.”

## 22. How do you explain a complex technical issue to a non-technical manager or client?

Answer:

“I start with the business impact rather than the implementation details.

For example, if a customer onboarding step fails because an external document service returns a date in an unexpected format, I would explain that the document information is being received, but the application is rejecting it because the format does not match the required validation rules.

I would then explain the impact, what we have confirmed, the current workaround if available, the next action and the expected update time. I would avoid stating an unverified root cause or promising an unrealistic resolution time.

Once the issue is resolved, I would summarize what caused it, how we fixed it and what preventive change was introduced.”

### Likely cross-questions

Q1. What if a stakeholder demands an immediate resolution?

“I would acknowledge the business urgency, explain what is confirmed and distinguish a workaround from a permanent fix. I would provide the next update time based on the investigation and communicate any uncertainty clearly.”

Q2. How do you handle a situation where another team owns the dependency?

“I would share the relevant evidence, request the required investigation and coordinate ownership and next steps. I would continue investigating my application's side of the integration rather than simply passing the issue to another team.”

## 23. How do you contribute to your team beyond writing code?

Answer:

“I contribute by taking responsibility for my tasks beyond implementation alone.

That includes understanding the business requirement, identifying dependencies, writing and updating tests, participating in code reviews, helping investigate defects and documenting findings where useful. When an issue spans multiple application layers, I try to trace the complete flow and share the evidence with the relevant team members.

I also use the debugging and problem-solving experience gained from production incidents to improve future implementations. For example, investigations into query performance and duplicate processing highlighted the importance of query-plan analysis, data integrity and regression testing.

I have not held myself out as a formal team lead. My contribution is through technical ownership, collaboration and dependable delivery within my role.”

### Likely cross-questions

Q1. Do you mentor junior developers?

“My formal role is as an individual contributor. I collaborate with teammates by discussing implementation approaches, explaining findings during debugging and sharing relevant technical context. I would distinguish that collaborative support from formal people-management responsibility.”

Q2. What would you do if another developer introduced a bug in code you reviewed?

“I would focus on containing the impact and identifying the root cause rather than assigning blame. I would help correct the issue, understand why it was not caught earlier and consider whether additional tests or review checks would prevent recurrence.”

## 24. Why do you want to leave TCS, and why do you want to join Infosys?

Answer:

“TCS has given me valuable experience working on enterprise applications, backend development, external integrations and production support. I have developed a stronger understanding of how real-world banking workflows need to handle validation, data integrity, security and operational issues.

At this stage of my career, I am looking for a role that allows me to deepen my Java backend skills while continuing to work with full-stack technologies and cloud-based deployments.

The Infosys role interests me because it aligns with my experience in Java, Spring Boot, Angular and AWS exposure. I would like to contribute my existing application-development and troubleshooting skills while learning more and taking on greater technical responsibility.

My decision is based on professional growth and the responsibilities of the new role, not on dissatisfaction with my current employer.”

### Likely cross-questions

Q1. Why should you leave TCS if you are already learning there?

“I appreciate what I have learned at TCS. My decision is about finding the right next step for my career. I want to continue building on my current experience through the responsibilities, technical challenges and learning opportunities of the role I am applying for.”

Q2. Why Infosys specifically?

“The advertised role aligns with the combination of Java backend development, full-stack integration and AWS exposure that I have been working with. I am interested in applying that experience in a role where I can contribute to delivery and continue improving my technical depth.”

Q3. What if the project requires you to learn a different technology?

“I am comfortable learning technologies based on project requirements. I would first understand the architecture and team's existing standards, then use documentation, small practical exercises, testing and code review to become productive without making assumptions about unfamiliar components.”

## 25. Why should we hire you for this Java Full Stack Senior Developer role?

Answer:

“I believe I bring a useful combination of backend development, full-stack integration and practical production troubleshooting.

I have over three years of enterprise application experience at TCS, working with Java, Spring Boot, REST APIs, Spring Security, JPA/Hibernate, PostgreSQL and Angular. My work includes implementing multi-step banking onboarding functionality, integrating external services, validating data, writing tests and supporting production releases.

I can also explain practical problems I have worked on, such as SQL query optimization using `EXPLAIN ANALYZE`, correcting a date-format mismatch at an integration boundary, and strengthening duplicate-processing protection with idempotency handling and a database constraint.

In addition, I have AWS deployment and monitoring exposure, and I understand the importance of reliable application behaviour, communication and code quality.

I would bring hands-on implementation skills, a structured debugging approach and a willingness to take greater ownership while collaborating effectively with the team.”

### Likely cross-questions

Q1. What differentiates you from another candidate with similar Java experience?

“I can connect backend implementation with real application behaviour. I have experience not only writing APIs but also investigating database performance, handling external integration issues, improving data integrity and supporting production troubleshooting. I try to understand the root cause and the business impact instead of treating each task as an isolated coding assignment.”

Q2. What is one area you need to improve?

“One area I want to deepen is my AWS architecture knowledge. I have exposure to application hosting, monitoring and deployment support, and I want to build stronger understanding of cloud networking, security, scalability and production architecture. I am continuing to strengthen this alongside my Java backend skills.”

Q3. What would your manager say about you?

“I would expect my manager to describe me as someone who takes responsibility for assigned work, investigates issues methodically and collaborates with the team to reach a resolution. I try to communicate blockers early and verify changes rather than assuming that code working locally means the task is complete.”

# Final revision strategy

Do not try to memorize all 25 answers word for word. Memorize the main answer structure and the supporting evidence.

These are the five stories you should be able to explain in depth, because one story can generate several follow-up questions:

| Story                                                         | Questions it supports |
| ------------------------------------------------------------- | --------------------- |
| End-to-end Saudi banking onboarding architecture              | 1–6, 12, 13, 23       |
| OCR date-format mismatch                                      | 8, 10, 12, 22         |
| PostgreSQL `EXPLAIN ANALYZE` optimization                     | 6, 9, 10, 17          |
| Duplicate processing, idempotency key and database constraint | 7, 11, 18, 19         |
| AWS deployment, logs and troubleshooting                      | 20, 21, 22            |

Before the interview, verify three details against your actual work: the precise functionality you personally implemented, the exact scope of the AWS services you used, and the final outcome of each incident. Do not add an unverified performance percentage, claim ownership of a team-level decision, or describe a hypothetical scenario as a real incident.

My recommended practice order: Questions 1–6 first, then 8–12, then 18–21, and finally 24–25. Practise the answers aloud and allow the interviewer to interrupt with follow-ups. The goal is to demonstrate that you understand your project, can explain your decisions, and can take responsibility for resolving problems—not just that you have memorized technical definitions.&#x20;