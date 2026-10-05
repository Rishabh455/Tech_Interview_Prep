For **Nomura Java Developer**, I would avoid saying something generic like “I learned Spring Boot.” They are asking for a **specific engineering change** that shows ownership, learning, problem-solving, and measurable impact.

Given your onboarding/KYC project, a strong answer is your **idempotency + state-management improvement**.

### Interview-ready answer — ~90 seconds

One change that I’m particularly proud of was improving the reliability of our customer onboarding workflow.

In our onboarding application, the same request could sometimes be triggered more than once because of user retries, network issues, or downstream timeouts. Since onboarding involved multiple steps like identity verification, document processing, and KYC validation, duplicate processing could create inconsistent states or duplicate downstream calls.

I worked on making the workflow more reliable by introducing an **idempotency mechanism and controlled state transitions**. We generated and validated an idempotency key for important requests, stored the processing state in the database, and used database-level constraints and atomic state updates to ensure that the same operation was not processed multiple times.

I also worked with the state-machine-based orchestration so that a request could move only through valid states, for example from `INITIATED` to `IDENTITY_VERIFIED` and then to the next stage. For concurrent requests, we used proper transaction handling and locking where required.

What I learned from this was that writing the API is only one part of backend development. In a banking application, I also need to think about **failure scenarios, retries, concurrency, consistency, and data integrity**.

I’m particularly proud of this change because it made the onboarding flow more resilient and gave me a much deeper understanding of designing reliable Java backend systems.

### If they ask: **“Why are you proud of this specifically?”**

Say:

> “Because initially I was mainly focused on implementing the business functionality. This change made me think beyond the happy path. I learned how a backend service should behave when requests are duplicated, a downstream service fails, or two requests arrive concurrently. That changed the way I design APIs today.”

### 🔥 Nomura-specific closing line

If they ask **“What did you personally learn from it?”**, finish with:

> **“The biggest learning was that in enterprise banking systems, correctness and consistency are as important as functionality. A successful API is not just one that works; it should also behave correctly during retries, failures, and concurrent requests.”**

That last line is particularly strong for a **Java/backend banking interview**, because it naturally opens the door for them to ask you about **multithreading, transactions, idempotency, locking, Kafka, Saga, or failure handling**—topics you should be ready to discuss.