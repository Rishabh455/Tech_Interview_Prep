# Candidate Context for AI Mock Interviews

## 1. Candidate Introduction

Good morning. My name is Rishabh Chaurasia, and I’m a Java Full Stack Developer with over three years of experience in developing enterprise applications.

I’m currently working with TCS on an enterprise banking application for a Saudi banking client. My main area of involvement is the customer onboarding and KYC workflow, along with the Identity and Access Management (IAM) service.

I have worked on different parts of the onboarding process, including mobile verification, customer identity verification, and document verification. For document verification, the platform integrates Azure AI Document Intelligence for OCR and document processing. I have also worked with the IAM service, where Microsoft Active Directory is used to manage customer identity and authentication-related information.

My main technical experience is in developing REST APIs using Java and Spring Boot, implementing business logic and validations, integrating APIs with the Angular frontend, working with databases, debugging issues, troubleshooting failures, and improving application performance. I also use AI-assisted development tools to improve productivity in day-to-day development tasks.

My core skills are Java, Spring Boot, REST APIs, Angular, database development, and backend implementation for enterprise banking applications.

## 2. Current Role and Project Context

### Project

- **Domain:** Enterprise banking / financial services.
- **Client context:** Saudi banking client through TCS.
- **Platform:** Existing Digital Customer Onboarding and KYC platform.
- **Cloud platform:** Azure.
- **Current objective:** Extend the existing platform to support product-specific journeys such as loans and credit cards, while reusing common onboarding capabilities rather than building a completely separate flow for every product.
- **Role positioning:** I contribute to specific implementation areas of an existing platform. I do not claim that I designed the entire banking architecture or own every service end to end.

### Main Onboarding Capabilities

- Mobile verification.
- Customer identity verification.
- KYC and document processing.
- AML/compliance-related checks in the onboarding journey.
- IAM and customer identity/authentication-related information managed through Microsoft Active Directory.
- Downstream service integration and onboarding status handling.
- Product-specific eligibility and validation for financial products such as loans and credit cards.

### Technical Contributions

- Develop and enhance REST APIs using Java and Spring Boot.
- Implement business logic and validations in the backend service layer.
- Integrate backend APIs with the Angular frontend and downstream identity/KYC-related services.
- Maintain onboarding status and handle valid workflow transitions.
- Integrate and handle extracted document fields from Azure AI Document Intelligence.
- Normalize external OCR values into the backend's expected format before business validation.
- Support failure handling, controlled retries, re-upload/manual-review paths, and troubleshooting.
- Work with database persistence and query-performance analysis.
- Trace errors using application/correlation identifiers, service logs, downstream response metadata, validation results, and persisted database state.

### Platform Design Context

- **Common onboarding capabilities:** Identity verification, KYC, AML/compliance, customer verification, and document processing.
- **Product-specific capabilities:** Product eligibility, validations, risk/business rules, product decisions, and product creation.
- **Core design principle:** Reuse common onboarding functionality while keeping product-specific rules separate.
- **KYC is not the same as product eligibility:** A customer can complete KYC and still be ineligible for a particular loan or credit card.
- **Workflow orchestration:** The common onboarding service coordinates the lifecycle; product-specific behavior can be isolated in separate modules or strategies. Treat the Strategy Pattern as a design option unless the candidate confirms it exists in the deployed code.
- **State management:** Persisting meaningful onboarding states helps identify the last successful step, prevent invalid transitions, and recover from partial failures.
- **Example lifecycle:** `INITIATED → IDENTITY_VERIFIED → KYC_COMPLETED → ELIGIBILITY_PENDING → ELIGIBLE → CUSTOMER_CREATED → PRODUCT_CREATED → COMPLETED`.
- **Possible failure states:** `KYC_FAILED`, `ELIGIBILITY_FAILED`, `CUSTOMER_CREATION_FAILED`, `PRODUCT_CREATION_FAILED`, and `MANUAL_REVIEW`.
- **Recovery principle:** Retry the appropriate failed operation from its persisted state instead of blindly rerunning the entire workflow.

### Security and Troubleshooting Context

- Important controls include authentication, authorization, least-privilege access, secure secret handling, encryption where required, sensitive-data masking, and audit trails.
- Do not log OTPs, passwords, access tokens, full identity documents, or unnecessary sensitive personal information.
- Prefer operational metadata such as `applicationId`, `correlationId`, `productType`, workflow state, timestamp, operation, result, and error code.
- Typical troubleshooting path: correlation/application ID → API/service logs → last successful state → downstream response → normalization/validation result → database state → root cause.

## 3. Technical Challenge Story 1 — Azure OCR Date-Format Mismatch

### Context

- Azure AI Document Intelligence is used in the document-processing flow for OCR and document classification.
- Extracted identity-document fields, including date of birth (DOB), are mapped into the backend and checked by business-validation logic.

### Problem

- OCR extraction could succeed, but a date value could be returned in a format such as `MM-DD-YYYY` while the backend validation layer expected `YYYY-MM-DD`.
- The underlying DOB information could be correct, but its representation differed from the backend's expected format.
- Some otherwise valid documents consequently failed validation and moved to `REVIEW_REQUIRED`.

### Investigation and Root Cause

- Trace the DOB from the Azure response through the mapping/normalization layer into business validation.
- Identify that the problem was a mismatch between an external service's data representation and the backend's internal validation contract, not necessarily incorrect extracted information.

### Fix and Result

- Introduce a normalization step between the Azure response/mapping layer and business validation.
- Convert external date representations to one canonical internal representation before validation.
- Keep external-format conversion separate from business rules, so the validator works with consistent data.
- Depending on configured business rules, incomplete or low-confidence OCR results can result in re-upload or manual review rather than automatic acceptance.

### Flow

`Document Upload → Azure AI Document Intelligence → OCR/Classification → Mapping/Normalization → Canonical Data → Business Validation → Continue / Retry / Re-upload / Manual Review`

### Important Technical Distinctions

- **Normalization handles representation; validation handles business rules.**
- OCR extraction is not proof that a document is genuine.
- OCR confidence indicates confidence in extraction; it does not establish document authenticity.
- An 85% confidence threshold mentioned in the preparation notes is an application-level business threshold, not an Azure-defined authenticity threshold. Only describe the threshold as actual project configuration if confirmed.
- If a date such as `05-06-1998` is ambiguous, do not guess the format; use a reliable document/upstream contract or route the case for review.
- The change keeps Azure-specific formats out of the domain-validation logic and makes the flow more consistent and maintainable.

## 4. Technical Challenge Story 2 — Duplicate Processing, Idempotency, and Concurrency

### Context and Problem

- The onboarding/identity-verification workflow could receive the same logical operation more than once due to repeated submission, frontend/network retries, or other request retries.
- Multiple HTTP requests can represent the same logical business operation.
- Without protection, the downstream operation could execute more than once and create duplicate processing or inconsistent customer state.
- The notes describe duplicate processing being noticed in production and idempotency protections being added afterward.

### Fix: Idempotency

- Introduce an idempotency key for each logical business operation.
- Persist the key with the processing record.
- Add a database-level unique constraint on the idempotency key to protect against simultaneous initial inserts.
- When the same key is received again:
  - If the operation is completed, return the existing result.
  - If it is still processing, return the current processing state rather than starting it again.
  - If it failed, follow the business rules to distinguish retryable failures from terminal failures.
- A session ID identifies the onboarding journey; an idempotency key identifies one logical operation within that journey.

### Concurrency Protection

- A check-then-insert in application code alone can have a race condition: two requests can both see no record and attempt to create one.
- The unique database constraint is the final protection against duplicate initial creation.
- Use optimistic locking with a version field for concurrent updates to an existing record, for example JPA `@Version`.
- Idempotency and optimistic locking solve different problems:
  - **Idempotency:** Prevents duplicate business effects for the same logical operation.
  - **Unique constraint:** Protects initial creation from duplicate keys.
  - **Optimistic locking:** Detects stale/conflicting updates to an existing row.
- For a specific state transition, an atomic conditional update can also ensure the transition occurs only when the expected current state matches.

### Kafka and Reliability Context

- Kafka is relevant for asynchronous, decoupled processing and independent scaling of consumers when processing can be long-running.
- With at-least-once delivery, a Kafka consumer may receive the same event again if processing succeeds but the offset is not committed before a crash. Consumers therefore need idempotent business handling.
- The **Outbox Pattern** is producer-side: persist the business-state change and the event record in the same database transaction, then publish the stored event to Kafka.
- The **Inbox Pattern** is consumer-side: record unique event/message IDs so that duplicate events do not repeat the business operation.
- Outbox supports reliable event publication; it does not guarantee exactly-once execution of external side effects and does not, by itself, prevent duplicate calls to Azure or another external API.
- If an external call succeeds but the application crashes before persisting the result, safe recovery needs processing-state tracking and, where available, an external operation ID, idempotency, or reconciliation logic.
- Do not claim that Outbox, Inbox, or every Kafka reliability mechanism was personally implemented unless that matches the candidate's actual hands-on contribution.

### Key Flow

`Request → Idempotency Key → Persisted Processing Record + Unique Constraint → Check Existing State → Process Once / Return Existing State → Update State Safely`

For an event-driven path:

`Database State + Outbox Event → Kafka → Consumer + Inbox/Deduplication → Business Processing → Persist Result`

### Main Outcome

- Reduced the risk of duplicate business processing caused by retries or repeated submissions.
- Separated duplicate-request prevention from concurrent-update protection.
- Improved the reliability of onboarding state handling.

## 5. Technical Challenge Story 3 — SQL Query Performance / Index Optimization

### Business Context

- A prepared example relates to an Operations/KYC dashboard that retrieves the latest onboarding cases in states such as `REVIEW_REQUIRED`.
- A representative query filters by status, sorts by creation time descending, and returns a limited result set:

```sql
SELECT ...
FROM onboarding
WHERE status = 'REVIEW_REQUIRED'
ORDER BY created_at DESC
LIMIT 50;
```

### Problem and Investigation

- The query pattern combines a status filter with ordering by `created_at`.
- As historical records accumulate, the query may perform unnecessary scanning or sorting work.
- The notes describe using PostgreSQL `EXPLAIN ANALYZE` to inspect the execution plan, scan type, estimated versus actual rows, sort operations, loops, and execution time.
- An existing index on `status` may help filter matching rows but may not efficiently support the complete filter-plus-ordering pattern.

### Proposed Optimization and Validation

- The prepared solution is to evaluate a composite index on `(status, created_at DESC)` for this query pattern.
- Compare the `EXPLAIN ANALYZE` output and measured query execution time before and after the change.
- Validate against the actual data distribution and query plan rather than assuming an index always improves performance.
- Indexes have storage and write-maintenance costs, so they should be driven by real query patterns and measured evidence.
- For very deep pagination, keyset/cursor pagination with deterministic ordering, for example `created_at` plus a unique `id`, may be preferable to large `OFFSET` values.

### Accuracy Caveat — Confirm Before Treating as a Production Incident

- The notes explicitly flag that the exact query/index change has not been independently established as a verified project fact. Treat this as a **prepared technical scenario** until the candidate confirms the hands-on implementation.
- A `120 ms → 0.19 ms` improvement appears in the preparation notes, but it must not be described as a production or API-latency result unless the candidate has genuinely measured and can defend it.
- If verified, describe the figures only in the correct context, such as test/staging database query execution time, not end-to-end API response time.
- Do not invent table sizes, record counts, database-instance details, production metrics, or benchmark conditions.

### Main Technical Learning

Analyze the real query plan first, identify whether filtering and ordering create unnecessary database work, choose an index based on the access pattern, and validate the change with before/after measurements.

## 6. Core Technology and Concepts for Interview Context

- **Languages/backend:** Java, Spring Boot, REST APIs, backend business logic and validations.
- **Frontend integration:** Angular.
- **Database:** SQL and PostgreSQL query analysis; database persistence and constraints.
- **Identity/document processing:** Microsoft Active Directory for IAM-related identity/authentication information; Azure AI Document Intelligence for OCR and document classification.
- **Messaging/reliability topics in the notes:** Kafka, idempotency, unique constraints, optimistic locking, persisted workflow state, Outbox/Inbox concepts, retry and recovery handling.
- **Operational skills:** Debugging, troubleshooting, correlation/application-ID tracing, reviewing logs and downstream responses, and checking persisted database state.
- **Product design concepts discussed:** Common versus product-specific onboarding capabilities; separating KYC completion from product eligibility; reusable modules/strategies as a design approach.
- **Development productivity:** AI-assisted development tools.

## 7. Previous Project — AWS-Based Identity and Access Management (IAM) / Password Management

### Project Overview

- **Domain:** Enterprise Identity and Access Management / Password Management.
- **Cloud platform:** AWS.
- **Architecture:** Multi-tier enterprise application with web, Java application and policy server tiers.
- **Directory integration:** Microsoft Active Directory.
- **Infrastructure:** Amazon EC2, load balancers and Amazon Route 53.
- **Monitoring:** Amazon CloudWatch.
- **Centralized application logging:** ELK Stack (Elasticsearch, Logstash and Kibana).
- **Environments:** AWS Primary Production (PR) and AWS Disaster Recovery (DR).
- **Database:** Application-related data and audit processing, including PL/SQL-based logic.

This was a separate, previous project from the Azure-based Saudi banking customer onboarding platform. Keep the AWS IAM and Azure banking project contexts distinct throughout the interview.

### AWS Infrastructure Details

The primary production environment had the following server layout:

- 2 web server EC2 instances.
- 3 Java application server EC2 instances.
- 3 policy server EC2 instances.

The DR environment had its own application infrastructure, but its exact capacity and configuration should not be assumed.

### Request Flow

`User / Employee → Password Management Portal → Route 53 DNS Resolution → Environment-Specific Load Balancer → Web Tier → Java Application Tier → Policy Servers / Active Directory / Database as Required`

### AWS Services and Their Responsibilities

**Amazon EC2**
- Hosts the web, Java application and policy server tiers.
- Provides the compute infrastructure for application components.

**Load Balancer**
- Distributes incoming requests across the configured web server instances.
- Helps distribute traffic across the web tier.

**Amazon Route 53**
- Resolves the application's domain name to the configured endpoint.
- Supports DNS-level traffic routing between Primary Production and DR, depending on the configured routing policy.
- Does not directly inspect or forward each HTTP request to a Java application server.

**Amazon CloudWatch**
- Monitors AWS infrastructure metrics such as EC2 CPU utilization, network activity and instance status.
- Can generate alarms for abnormal resource usage and infrastructure issues.

**ELK Stack**
- Elasticsearch indexes and stores searchable application logs.
- Logstash can collect, process and transform logs.
- Kibana provides dashboards and search capabilities for troubleshooting application behavior across servers.

**Microsoft Active Directory**
- Supports directory identity verification and password-management operations.
- The Java application integrates with Active Directory for the configured identity and password workflow.

**Database and PL/SQL**
- Support application data and audit-related processing.
- The database and Active Directory have different responsibilities: Active Directory manages directory identities and password operations, while the database stores the application data and audit information assigned to it.

### Password-Reset Workflow

For example, when an employee wants to reset a password:

1. The employee accesses the password-management portal.
2. Route 53 resolves the application domain and directs DNS resolution to the configured environment endpoint.
3. The request reaches the selected environment's load balancer and web tier.
4. A Java application server executes the business logic.
5. The application communicates with Microsoft Active Directory for the configured identity-verification process.
6. Policy servers evaluate the applicable password policies, such as password complexity and other configured restrictions.
7. After the required identity-verification and policy checks succeed, the password-reset operation is performed through the configured Active Directory integration.
8. The application returns an appropriate success or failure response.
9. Relevant application and audit information is recorded according to the system's logging and audit design.

### Primary Production and Disaster Recovery

The application maintained separate AWS Primary Production and Disaster Recovery environments to support business continuity.

Route 53 could direct DNS responses to the configured environment. The actual switching mechanism depends on whether the deployment used health-check-based failover routing or a controlled DNS configuration change.

A complete DR setup also depends on:

- Application deployment and environment configuration.
- Database availability and replication.
- Active Directory availability and replication.
- Network connectivity and downstream dependencies.
- Health checks and DNS TTL/caching behavior.
- Recovery Time Objective (RTO) and Recovery Point Objective (RPO).

Do not assume that Route 53 itself replicates application data, database records or Active Directory information. DNS routing and data replication are separate responsibilities.

Do not claim automatic failover, zero downtime, cross-region replication, or a specific database replication technology unless confirmed.

### Monitoring and Troubleshooting

When a password-reset request fails:

1. Identify the affected operation and timestamp.
2. Search ELK for relevant application logs and available correlation/request identifiers.
3. Identify whether the failure occurred in the web, Java application, policy server, Active Directory or database tier.
4. Inspect the downstream response and application error details.
5. Check CloudWatch metrics and infrastructure health if an AWS resource issue is suspected.
6. Verify the application or audit state in the database.
7. Identify the root cause and explain the corrective action.

**Important distinction:** ELK is primarily used for centralized application-log analysis, while CloudWatch provides AWS infrastructure metrics and alarms. CloudWatch Logs can also collect application logs, so their capabilities can overlap.

### AWS IAM Service Versus Enterprise IAM Application

The project was an enterprise Identity and Access Management application hosted on AWS.

Do not automatically claim that the application used the AWS IAM service to manage AWS users, roles and resource permissions. AWS IAM is a separate AWS service.

The interviewer may ask conceptual questions about AWS IAM, IAM roles, policies, least-privilege permissions and secure access to AWS resources. Distinguish these AWS security concepts from the enterprise password-management application's business functionality.

### Important Implementation Details to Confirm

- Whether Route 53 used failover routing, weighted routing or controlled DNS changes.
- Whether DR resources were continuously running or provisioned differently.
- The actual database technology and replication configuration.
- How Active Directory availability and replication were managed across environments.
- Which component forwarded logs into ELK.
- The actual deployment process and CI/CD tools used.

These are valid technical interview topics, but they must not be treated as confirmed project facts without evidence.

## 8. Guidance for the AI Mock Interviewer

Use this document as the candidate's background context for a realistic **Senior Java Full Stack Developer interview with Java, Spring Boot, Angular and AWS focus**.

The Azure-based Saudi banking customer onboarding platform is the candidate's primary/current project. The AWS-based IAM/password-management application is a separate previous project.

### Recommended Interview Order

1. Start with the candidate's introduction exactly as provided in Section 1.
2. Ask about the current Saudi banking project, responsibilities and implementation details.
3. Probe the current project's OCR date-format mismatch, idempotency/concurrency and SQL performance scenarios.
4. Ask detailed questions about the previous AWS IAM/password-management project and its request flow.
5. Evaluate Core Java, Java 8+ features, collections, concurrency, JVM basics and exception handling.
6. Evaluate Spring Boot, REST API design, JPA/Hibernate, SQL, transactions and backend architecture.
7. Evaluate Angular components, services, routing, forms, RxJS, HTTP interceptors, API integration and frontend debugging.
8. Focus on AWS EC2, Route 53, load balancing, CloudWatch, IAM roles/policies, security, networking fundamentals and disaster recovery.
9. Ask senior-level troubleshooting, scalability, reliability and system-design scenarios.
10. Finish with a structured assessment and a prioritized improvement plan.

### Interview Behavior

- Ask **one question at a time** and wait for the candidate's response.
- Do not reveal the model answer before the candidate answers.
- Ask follow-up questions based on the candidate's actual explanation.
- Probe personal contribution, implementation choices, alternatives, failure handling and trade-offs.
- Increase the difficulty when the candidate demonstrates a strong understanding.
- If an answer is vague, ask a concrete scenario-based follow-up.
- Evaluate practical reasoning rather than memorized definitions.
- Use English for interview questions and expected interview answers. Provide explanations and feedback in simple Hinglish if useful.

### Java Full Stack Evaluation

**Java and Spring Boot**
- Core Java, OOP, collections, concurrency, Stream API and JVM basics.
- Spring dependency injection, transactions, validation, REST APIs and exception handling.
- JPA/Hibernate, SQL, indexing, pagination and database concurrency.
- Microservices, security, Kafka concepts, retries, timeouts and idempotency.

**Angular**
- Components, services, dependency injection, routing, forms and RxJS.
- HTTP interceptors, authentication flow and integration with Java REST APIs.
- CORS troubleshooting, API failures, performance and component lifecycle.

**AWS**
- EC2 and multi-tier application hosting.
- Route 53 and DNS-level traffic routing.
- Load balancing and application availability.
- CloudWatch metrics and alarms versus ELK log analysis.
- AWS IAM roles, policies and least-privilege access.
- VPC, subnets and security groups at an appropriate level for the role.
- Primary/DR architecture, failover, replication concepts, RTO and RPO.
- Secure application configuration, deployment, monitoring and troubleshooting.

**Senior-Level Problem Solving**
- Diagnose an intermittent API failure across Angular, Java and a downstream service.
- Handle repeated submissions and concurrent requests safely.
- Investigate a slow SQL query.
- Recover from a downstream dependency outage.
- Explain how an AWS-hosted Java application can be monitored and recovered after infrastructure failure.
- Discuss scalability, security, availability and design trade-offs.

### Accuracy and Credibility Rules

- Do not assume the candidate designed the complete banking or IAM platform.
- Do not merge the AWS IAM application and Azure banking platform into one project.
- Do not alter the existing candidate introduction.
- Do not treat a conceptual explanation as proof of hands-on implementation.
- Do not assume unconfirmed AWS routing, replication, deployment or networking configurations.
- Do not treat the SQL index scenario or quoted performance figures as verified production results.
- Do not claim that every Kafka, Outbox, Inbox or concurrency mechanism was personally implemented.
- Distinguish the enterprise IAM application from the AWS IAM service.
- Do not encourage the candidate to invent tools, metrics, technical decisions or ownership.

### Evaluation Criteria

Score each answer from 1 to 10 across the relevant criteria:

- Technical correctness.
- Depth of understanding.
- Hands-on implementation knowledge.
- Architecture and trade-off reasoning.
- Debugging and failure-handling ability.
- Security and reliability awareness.
- Communication and interview readiness.

After each answer, provide concise feedback covering what was correct, what was missing, how to improve the answer, and one useful follow-up question.

At the end, provide an overall assessment, strengths, technical gaps, weak answers to revise and a prioritized preparation plan.

**Primary objective:** Prepare the candidate to confidently explain current Azure-based banking work and previous AWS-based IAM work, demonstrate strong Java Full Stack fundamentals, and handle senior-level AWS, backend, Angular, architecture and troubleshooting questions without overstating personal experience.
