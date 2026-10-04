Yes. I reviewed the transcript specifically for the **project-driven questioning pattern**. The interviewer repeatedly moved from **“tell me about the project” → architecture → integrations → authentication/authorization → API flow → failure handling → implementation details** rather than asking only textbook theory. Pasted text Pasted text

For your **Saudi Bank Digital Customer Onboarding + KYC project**, this is the 30-minute set I would prepare.

## 30-Minute Nomura Project Question Bank

### 1. “Can you explain your project?”

**Best answer:**

> “I worked on a Digital Customer Onboarding and KYC platform for a Saudi bank. The objective was to digitally onboard customers by collecting and validating their identity and KYC information before creating the customer or product relationship.
>
> From the backend perspective, the onboarding service acted as the workflow and orchestration layer. It coordinated steps such as customer data validation, identity verification, OTP verification, document processing, KYC validation, AML checks and finally the business decision.
>
> We had separate services for capabilities such as identity verification, OTP, document processing and AML because these had different responsibilities and external dependencies. The onboarding workflow maintained the overall application state and moved the application from one stage to the next.
>
> My contribution was mainly around backend APIs, workflow/state management, validations, downstream integrations, error handling and making the flow reliable and idempotent.”

**Possible cross-question:**  
“Which module did you personally work on?”

> “My main focus was the onboarding workflow/orchestration layer, where I worked on REST APIs, state transitions, validations, downstream service integration and failure handling.”

---

# 2. “What kind of architecture did you use?”

This is one of the **highest-probability questions** because the interviewer explicitly asked about architecture in the transcript. Pasted text

**Answer:**

> “We followed a microservices-based architecture. The onboarding service acted as the orchestration layer for the customer journey.
>
> The frontend interacted with the onboarding APIs, and the onboarding service coordinated downstream capabilities such as identity verification, OTP, KYC/document processing and AML.
>
> The reason for separating these capabilities was that they had independent responsibilities, scaling requirements and external integrations. Instead of putting everything into one service, we kept the business capabilities decoupled while maintaining the overall customer journey through the onboarding workflow.”

### Cross-question: “Why not put everything in one service?”

> “Because identity, OTP, AML and document processing have different responsibilities and failure characteristics. Separating them reduces coupling and allows independent scaling and deployment.”

### Cross-question: “Why did you keep orchestration inside onboarding-service?”

> “Because the onboarding steps are strongly connected to the customer journey and several steps are synchronous and user-driven. Keeping orchestration inside onboarding-service avoids creating another unnecessary orchestration microservice.”

---

# 3. “Can you explain your onboarding workflow end to end?”

**Very important.**

> “The journey starts when the customer initiates an onboarding application.
>
> The onboarding service validates the request and creates the application with an initial state.
>
> Then the required verification steps are triggered, such as identity verification, mobile OTP verification and KYC document processing. Document data is extracted and validated, and compliance checks such as AML are performed.
>
> Once the required validations are successful, the application moves to the business decision stage. Based on the decision, we either proceed toward customer or product creation or move the application to a rejected or manual-review state.
>
> The important part is that the onboarding service maintains the application state throughout the journey so that we always know exactly where the customer is in the process.”

### Cross-question: “How do you maintain the current state?”

> “The onboarding application maintains a persisted workflow state. State transitions are controlled by business rules, so we don't allow an application to arbitrarily jump from one stage to another.”

---

# 4. “Why did you need a workflow/state machine?”

This is a likely follow-up once you mention orchestration.

> “Because onboarding is not just a sequence of API calls. Each application can be in a specific business state, such as identity pending, OTP pending, document verification pending, AML pending, approved or rejected.
>
> A state machine makes these transitions explicit and controlled. Each event causes a valid state transition, and invalid transitions can be rejected.
>
> It also makes the workflow easier to maintain because the business flow is represented as states and transitions instead of being scattered across multiple if-else blocks.”

### Cross-question: “What happens if the same step is triggered twice?”

> “We make the operation idempotent and validate the current state before processing. If the application is already in the target or completed state, we don't execute the same business operation again.”

---

# 5. “What exactly was your contribution?”

This can separate a genuine project owner from someone who only knows the architecture.

> “My contribution was mainly on the backend onboarding flow. I worked on REST APIs, request validation, business validation, workflow/state transitions, integration with downstream services, exception handling and reliability mechanisms.
>
> I also worked on handling duplicate requests and maintaining consistent application state during failures.”

### Cross-question: “Did you design the entire architecture?”

Do **not** overclaim.

> “I contributed to the design of the onboarding workflow and service-level architecture. The overall enterprise architecture was a collaborative decision with the senior architects and team, but I was directly involved in the design and implementation of my service.”

That answer is much safer in an interview.

---

# 6. “How did you integrate your application with other services?”

The transcript explicitly drilled into external integration and then asked about **code-level integration**, not only infrastructure. Pasted text

**Answer:**

> “We integrated the onboarding service with downstream services through well-defined REST APIs and service contracts.
>
> The onboarding service prepared the required request, called the downstream service, validated the response and then updated the onboarding state based on the outcome.
>
> For each integration we considered authentication, request and response mapping, timeout handling, error handling and idempotency.
>
> We also kept the external integration isolated from the core workflow logic so that changes in one downstream service would have minimum impact on the onboarding domain.”

### Cross-question: “What happens if downstream service is unavailable?”

> “We don't blindly continue the workflow. We classify the failure as retryable or non-retryable. For transient failures we can retry with controlled limits, while for persistent failures we keep the application in an appropriate pending/error state and avoid corrupting the onboarding state.”

---

# 7. “What challenges did you face while integrating external services?”

The transcript specifically asked this. Pasted text

**Answer:**

> “The main challenges were external dependency failures, different request and response contracts, authentication, timeout handling and occasional data mismatches.
>
> One practical issue was that data returned from an external document-processing capability could have a different format from what our backend expected. We therefore introduced a normalization and validation layer before allowing the workflow to proceed.
>
> We also had to handle downstream failures carefully so that a temporary external-service failure would not incorrectly mark the onboarding application as successful.”

---

# 8. “Tell me about a real integration/data mismatch issue.”

This is a **very strong project story** for you.

> “We had a document-processing integration where a date extracted from the document could come in a different format from the format expected by our backend.
>
> Instead of directly storing the external response, we introduced a normalization layer. We converted the external format into our canonical format and then performed validation.
>
> We also used the document-processing confidence score. When the confidence was below the configured threshold, instead of accepting potentially incorrect data, we asked for document re-upload or moved the case toward manual review after repeated unsuccessful attempts.
>
> This prevented low-confidence OCR output from silently entering the onboarding workflow.”

### Cross-question: “Why not trust the OCR result?”

> “Because OCR is probabilistic. A technically successful OCR response does not necessarily mean the extracted field is correct. So confidence plus business validation gives us a safer decision boundary.”

---

# 9. “How do you make sure the same onboarding request is not processed twice?”

**Very likely for your project.**

> “We use idempotency. The client provides or we generate an idempotency key for a logical operation.
>
> We persist that key along with the operation state and use database constraints to prevent duplicate processing.
>
> Before processing a request, we check whether the same operation has already been completed or is currently being processed. This is particularly important for onboarding because duplicate processing could trigger duplicate OTP, duplicate document processing or duplicate downstream customer creation.”

### Cross-question: “Why isn't checking the database enough?”

> “Because two concurrent requests can both read ‘not processed’ and then both proceed. So the protection needs to be atomic, using database constraints or locking rather than only application-level checks.”

---

# 10. “What happens if your service crashes after a database update but before sending an event?”

This is an excellent cross-question from your architecture.

> “That is a classic consistency problem. We use the Outbox pattern for reliable event publication.
>
> Instead of updating the business data and independently publishing an event, we persist the business change and the event record in the same database transaction.
>
> A separate publisher then reads the outbox record and publishes it. If the application crashes after the transaction commits, the outbox record still exists and can be published later.”

### Cross-question: “Can the event be published twice?”

> “Yes, duplicate delivery is possible, so consumers must be idempotent. We use an idempotency or message identifier and persist processing status so that duplicate events do not create duplicate business effects.”

---

# 11. “How did you handle concurrent requests for the same application?”

> “We protect critical state transitions at the database level. For cases where two requests can modify the same onboarding application concurrently, we use appropriate locking or version-based concurrency control.
>
> For critical transitions, pessimistic locking such as `SELECT FOR UPDATE` can ensure that only one transaction modifies the record at a time. Optimistic locking with a version field is useful when conflicts are less frequent and we want to detect stale updates without holding a database lock.”

### Cross-question: “Why not simply synchronize the Java method?”

> “Because the application can run on multiple instances. Java `synchronized` only protects threads within one JVM, whereas database-level concurrency control works across application instances.”

---

# 12. “How did you implement authentication and authorization?”

This was a major interviewer topic in the transcript. Pasted text

**Answer:**

> “For the backend APIs, authentication and authorization are handled through Spring Security.
>
> Authentication establishes who the caller is, while authorization determines whether that authenticated user or client is allowed to perform a specific operation.
>
> For protected APIs, the request goes through the security filter chain. The authentication information is validated, the authenticated principal is established in the security context, and then role or authority checks are applied before the controller logic is executed.
>
> For example, different APIs may require different permissions depending on the operation, so we enforce authorization at the endpoint or service layer rather than relying only on frontend restrictions.”

### Cross-question: “What is the difference between 401 and 403?”

> “401 means the request is not properly authenticated. 403 means the caller is authenticated but does not have sufficient permission.”

---

# 13. “How does JWT authentication work?”

**Answer:**

> “After successful authentication, the server issues a signed JWT containing the relevant claims.
>
> For subsequent requests, the client sends the JWT in the Authorization header. A security filter intercepts the request, validates the signature and token properties such as expiry, extracts the identity and authorities, and places the authenticated principal into the security context.
>
> The request is then allowed to continue only if the required authorization rules are satisfied.”

### Cross-question: “Why JWT?”

> “JWT supports stateless authentication because the server does not need to maintain a server-side session for every request. That makes it convenient for horizontally scaled APIs.”

---

# 14. “How do you implement role-based authorization?”

> “Authentication tells us who the caller is, and authorization checks what that caller can do.
>
> We associate roles or authorities with the authenticated principal and then protect sensitive APIs using Spring Security rules or annotations such as `@PreAuthorize`.
>
> For example, an operation that changes an onboarding status or performs an administrative action would require a specific authority rather than being accessible to every authenticated user.”

### Cross-question: “Can frontend authorization alone provide security?”

> “No. Frontend checks are only for user experience. Real authorization must always be enforced on the backend because the client cannot be trusted.”

---

# 15. “How would you debug an onboarding API that is failing?”

This was directly asked in the transcript. Pasted text

**Answer:**

> “First I reproduce the problem using Postman or an equivalent API client.
>
> Then I identify whether the failure is happening at the API layer, business-logic layer, database layer or downstream integration.
>
> I check application logs and correlation IDs to trace the request across services.
>
> Then I inspect the database state and verify whether the expected state transition actually happened.
>
> If the request calls an external service, I check its request, response, timeout and error information.
>
> Finally, after fixing the issue, I add or update automated tests for the failure scenario so that it does not regress.”

### Excellent follow-up:

**“What if the API returns 200 but the business result is wrong?”**

> “Then HTTP-level success is not sufficient. I validate the business state, downstream response and persisted data because a technically successful request can still produce an incorrect business outcome.”

---

# 16. “How do you handle API failures?”

> “We distinguish between technical failures and business failures.
>
> For example, a timeout from a downstream service is a technical failure, while an invalid customer document can be a business validation failure.
>
> Technical failures may be retryable depending on the operation, while business failures normally should not be retried blindly.
>
> We return meaningful error responses to the caller and persist the correct onboarding state so that the application can safely continue, retry or move to manual review.”

---

# 17. “What happens if OTP service is down?”

This is a classic cross-question.

> “The onboarding application should not be marked as OTP verified. The OTP step remains pending or retryable.
>
> We can retry transient failures with a controlled retry policy, but we should avoid unlimited retries.
>
> Most importantly, the workflow state should remain consistent, so the customer cannot proceed to the next stage without successful OTP verification.”

---

# 18. “What if AML service succeeds but your onboarding service crashes?”

> “The downstream AML result must be persisted or made recoverable so that we don't lose the business outcome.
>
> We can use reliable event or transaction patterns depending on whether the interaction is synchronous or asynchronous. The onboarding state transition must be idempotent so that when recovery happens, we don't execute the AML business effect twice.”

---

# 19. “How do you handle partial failure in a multi-step onboarding flow?”

This maps directly to the transcript's partial-API-failure discussion, where the interviewer asked what happens when only one part of a complex flow fails. Pasted text

**Answer:**

> “I don't treat the complete onboarding journey as one simple success or failure flag.
>
> Each major stage has its own state. For example, identity verification can be successful while AML is still pending.
>
> Therefore, the application state represents exactly which stage has completed and which stage needs action.
>
> If one downstream dependency fails, other completed stages remain successful, while the failed stage is retried or moved to an appropriate exception state.
>
> This prevents us from unnecessarily restarting the entire onboarding journey.”

---

# 20. “How do you connect frontend and backend?”

The transcript explicitly asks this after the DI discussion. Pasted text

For your project, keep it backend-oriented:

> “The frontend communicates with the onboarding backend through REST APIs over HTTPS.
>
> The frontend sends the onboarding request and receives the current application status or validation result from the backend.
>
> The backend exposes domain-specific APIs, performs validation and orchestration, and returns structured responses.
>
> Authentication information is passed with the request, and backend authorization determines whether the operation is allowed.
>
> The frontend should not contain critical business rules; those belong in the backend.”

---

# 21. “What happens if one backend dependency fails while other onboarding data is available?”

> “We isolate the dependency failure rather than failing unrelated functionality.
>
> For example, if customer profile data is available but document verification is temporarily unavailable, we preserve the already validated information and keep document verification in a pending state.
>
> Once the dependency becomes available, the application can resume from that stage instead of starting from scratch.”

This is the **backend equivalent** of the partial-API failure question from the transcript.

---

# 22. “Why did you separate Identity, OTP, AML and Document Processing?”

This is an important architectural cross-question.

> “They represent different business capabilities and have different integration and scaling characteristics.
>
> Identity verification may depend on identity providers, OTP has messaging and security concerns, document processing depends on OCR technology, and AML has compliance-specific logic and external dependencies.
>
> Separating them keeps responsibilities clear and prevents changes in one capability from unnecessarily affecting the others.”

---

# 23. “Why didn't you make every step asynchronous?”

Very strong Nomura-style cross-question.

> “Not every step benefits from asynchronous processing.
>
> Some onboarding steps are synchronous and user-driven because the user is actively waiting for the result before proceeding.
>
> We use synchronous communication where the immediate response is required, while asynchronous/event-driven processing is useful for decoupled operations, notifications, audit events or background processing.
>
> The choice depends on business latency requirements and failure semantics rather than simply making everything asynchronous.”

---

# 24. “How do you make your onboarding APIs reliable?”

**Answer:**

> “I focus on idempotency, validation, proper state management, timeout and retry handling, consistent database transactions, concurrency control, structured logging and clear error handling.
>
> For asynchronous communication, reliable event publishing and idempotent consumers are important.
>
> The objective is that a temporary infrastructure or downstream failure should not lead to duplicate customer creation or an inconsistent onboarding state.”

---

# 25. “Suppose customer clicks Submit twice. What happens?”

**Perfect 20-second answer:**

> “The request is protected through idempotency. Both requests may arrive, but only one should create the business effect. We use an idempotency key and database-level uniqueness/atomic state transition so the second request either returns the existing result or is safely ignored.”

---

# 26. “Suppose two requests try to approve the same application simultaneously?”

> “The application state transition must be atomic. We can use optimistic locking with a version field or pessimistic locking for critical transitions.
>
> The first transaction updates the state successfully. The second request detects the changed version or locked state and cannot perform the same transition again.”

---

# 27. “How do you handle a downstream service timeout?”

> “I first classify whether the operation is safe to retry.
>
> For a transient timeout, controlled retry with a timeout limit can help. But retries should not create duplicate business operations, so the downstream call must support idempotency where required.
>
> After retry exhaustion, we preserve the correct application state and expose the operation as pending or failed rather than assuming success.”

---

# 28. “How do you monitor/debug such a distributed onboarding flow?”

> “We use centralized logging and correlation IDs so that one customer request can be traced across the onboarding service and downstream services.
>
> We monitor API latency, error rates, downstream failures and processing failures.
>
> Logs should contain enough technical context to troubleshoot a request without exposing sensitive customer information.”

Your project context already includes Elasticsearch/Kibana-style centralized logging, so this is a good answer for you.

---

# 29. “How do you handle sensitive KYC data?”

This is very relevant for a banking project.

> “KYC and identity data should be treated as highly sensitive.
>
> We use HTTPS for communication, authentication and authorization for API access, restricted database access, secure secrets management and controlled logging.
>
> We should avoid putting sensitive document data, tokens or personal information into application logs unnecessarily.
>
> Access should follow least-privilege principles.”

---

# 30. “What was the most challenging part of your project?”

Use this story.

> “The most challenging part was maintaining a reliable onboarding workflow across multiple downstream dependencies.
>
> The challenge was not simply calling different APIs; it was ensuring that the overall business state remained correct when a downstream service timed out, returned invalid data, or when the application failed in the middle of processing.
>
> I worked on state management, idempotency, validation and reliable failure handling so that the workflow could resume safely without duplicating business operations.”

---

# The 10 Questions I Would Absolutely Memorize

For **30 minutes only**, don't try to memorize all 30 equally.

### Tier 1 — Must Know

1. **Explain your project end to end.**
2. **What architecture did you use?**
3. **What exactly was your contribution?**
4. **Explain onboarding workflow/state machine.**
5. **How did you integrate downstream services?**
6. **What integration challenge did you face?**
7. **How did you implement authentication and authorization?**
8. **How do you handle duplicate requests/idempotency?**
9. **What happens if a downstream service fails?**
10. **What happens if your service crashes during processing?**

### Tier 2 — Cross-question preparation

11. Why microservices?
12. Why orchestration?
13. Why state machine?
14. Why synchronous vs asynchronous?
15. How do you handle concurrent requests?
16. How do you debug an API?
17. How do you handle partial failure?
18. How do you secure KYC data?
19. How do you monitor distributed requests?
20. What was your biggest technical challenge?

---

# One Very Important Nomura Interview Pattern

The transcript shows that the interviewer did **not** stop at the first answer. He repeatedly challenged the candidate:

**Architecture → “How exactly?” → “What challenge?” → “At code level?” → “How authentication?”**

That pattern is visible very clearly in the architecture/integration section and then again in authentication. Pasted text Pasted text Pasted text

So your rule in the interview should be:

> **Never give a project answer without being ready for “Why?”, “How?”, “What if it fails?”, and “What exactly did YOU do?”**

For your project, that means the strongest four words to keep in your head are:

**Workflow → Integration → Reliability → Security**

That is the core of your 30-minute revision.