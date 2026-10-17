Bhai, ye 6 high-probability interview scenarios prepare karo. Inse tumhare Idempotency, Unique Constraint, Transactions, Optimistic/Pessimistic Locking, Outbox, Inbox aur Azure failure recovery ke core concepts cover ho jayenge.

## 6 important scenarios

Scenario 1 · Must know

Two identical requests arrive simultaneously

Question: Request A and B arrive with the same idempotency key `K123`. Both find no existing record. How do you prevent duplicate processing?

Answer:

- Unique constraint on the idempotency key ensures only one record wins.
- The losing request fetches the existing record and returns its status/result.
- Idempotency handles duplicate business requests; the unique constraint protects concurrent record creation.

  Scenario 2 · Must know

  Application crashes after Azure accepts the request

Question: Azure accepts the document, but your application crashes before saving the Azure operation ID. What happens when the same request retries?

Answer:

- Find the existing `PROCESSING` record.
- Do not blindly call Azure again.
- Use external idempotency/correlation support or reconciliation if available.
- Outbox alone cannot guarantee exactly-once execution at Azure.

  Scenario 3 · Must know

  Database update succeeds, but Kafka offset commit fails

Question: The consumer processes an event and commits the database transaction, but crashes before committing the Kafka offset. What happens?

Answer:

- Kafka may redeliver the event.
- An Inbox table with a unique event ID prevents duplicate business processing.
- The business update and Inbox processed marker should be committed atomically in the same database transaction.

  Scenario 4 · Must know

  Application crashes before publishing an event

Question: The database update succeeds, but the application crashes before publishing the event to Kafka. How do you avoid losing the event?

Answer:

- Use the Outbox pattern.
- Save the business-state update and Outbox event in the same database transaction.
- A background publisher publishes pending events to Kafka.
- Duplicate publication is still possible, so consumers must be idempotent.

Two workers update the same application status

Question: Two workers try to change the same KYC application from `PROCESSING` to different states. How do you prevent inconsistent updates?

Answer:

- Optimistic locking uses a version field to detect conflicting updates.
- Pessimistic locking, such as `SELECT ... FOR UPDATE`, serializes access to an existing row.
- An atomic conditional update can enforce valid state transitions.
- Choose based on contention and transaction requirements.

  Scenario 6 · Retries

  Customer retries after a timeout

Question: The client receives no response and retries, but the original request may already have completed. How do you handle it?

Answer:

- Reuse the same idempotency key for the same logical operation.
- If completed, return the stored result.
- If processing, return the current status.
- If the external outcome is uncertain, reconcile before retrying the external side effect.

## Remember this cheat sheet

| Concept             | Solves what?                          |
| ------------------- | ------------------------------------- |
| Idempotency key     | Duplicate logical requests            |
| Unique constraint   | Concurrent duplicate record creation  |
| Atomic transaction  | All-or-nothing database changes       |
| Optimistic locking  | Detects conflicting updates           |
| Pessimistic locking | Serializes access to an existing row  |
| Outbox              | Reliable event publication            |
| Inbox               | Duplicate event processing            |
| Reconciliation      | Uncertain external operation outcomes |

Interview tip: Scenarios 1, 2 and 4 are especially useful for demonstrating that you understand the difference between database consistency, message delivery and external-service side effects. Learn the reasoning behind each answer, not just the wording.