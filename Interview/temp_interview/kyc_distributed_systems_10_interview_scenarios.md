# KYC Onboarding: 10 High-Probability Distributed Systems Interview Scenarios

**Topics:** Idempotency, unique constraints, database transactions, atomic state transitions, optimistic/pessimistic locking, Kafka, Outbox, Inbox, retries, external API failures, and reconciliation.

**How to use this sheet:** For each scenario, learn the question, understand the answer, and memorize the short flow. These are reference designs for interview discussion. Describe something as an implemented project feature only if it was actually implemented in your project.

---

## Scenario 1 — Two identical requests arrive at exactly the same time

### Interviewer question

A customer submits a document for KYC verification. Because of a double-click or retry, Request A and Request B reach your backend almost simultaneously. Both carry the same idempotency key, `K123`. Both requests initially check the database and find no record. How do you ensure that both requests do not start the same business operation?

### Interview-ready answer

An application-level `SELECT` followed by an `INSERT` is not sufficient because both requests can check before either one inserts. I would persist the idempotency key under a **database unique constraint** and create the processing record inside a database transaction.

Both requests may attempt the insert, but the unique constraint allows only one row for `K123`. The winning request owns the new operation. The losing request handles the unique-constraint conflict, retrieves the existing record, and returns its current status or stored result instead of starting the operation again.

If the winning transaction rolls back, the other request may be able to insert successfully; the implementation must handle that outcome too. The crucial point is that duplicate detection and creation must be safe under concurrency, not just sequential requests.

### Short flow

```text
Request A (K123) ─┐
                  ├─> INSERT + UNIQUE(idempotency_key)
Request B (K123) ─┘             |
                 One insert wins; the other handles conflict
                                |
                  Read existing status/result; do not start twice
```

**Memory trick:** **Same key + simultaneous requests → UNIQUE constraint.** The application check helps; the database constraint settles the race.

---

## Scenario 2 — The request is retried after the operation is already completed or is still processing

### Interviewer question

Request A with idempotency key `K123` has completed document processing, but its HTTP response never reached the client. The client retries with the same key. In another case, the first request is still processing when the retry arrives. What should the backend return in each case?

### Interview-ready answer

The backend first looks up the operation associated with `K123`; it must not automatically start a new operation.

- **COMPLETED:** Return the stored result or an equivalent response for the original operation.
- **PROCESSING:** Return the current status or a defined in-progress response, such as HTTP `202 Accepted`, if that matches the API contract.
- **FAILED:** Follow an explicit retry policy. Decide whether the same operation can be retried safely or whether a new operation is required.
- **Key not found:** Register and start the operation, while relying on a unique constraint to handle concurrent first requests.

The key must remain stable across retries of the same logical operation. Idempotency means repeated requests produce no additional business effect; it does not necessarily mean every response must have identical HTTP timing or status codes.

### Short flow

```text
Same key arrives
      |
  Read operation state
  ├─ COMPLETED  -> return stored result
  ├─ PROCESSING -> return current status
  ├─ FAILED     -> apply defined retry/recovery policy
  └─ NOT FOUND  -> create operation safely
```

**Memory trick:** **Same key, different state → same operation, state-aware response.** Never blindly restart it.

---

## Scenario 3 — The customer clicks Upload twice, or the frontend sends two uploads

### Interviewer question

Your Angular UI disables the Upload button after the first click. Is that enough to prevent duplicate document uploads? What happens if two requests still reach the backend? Will the document be stored twice?

### Interview-ready answer

Disabling the button is useful for user experience, but it is not a reliable backend guarantee. A second request can still come from a network retry, refresh, another browser tab, an automatic client retry, or a direct API call.

For the same logical upload, the client should reuse the same idempotency key when retrying. The backend should enforce that key with persistent storage and a unique constraint, then return the existing operation instead of creating another one.

However, an idempotency record alone does not automatically prevent duplicate file objects in object storage. The storage workflow also needs suitable controls—for example, a stable document/object identifier, a unique document record, or safe storage-write semantics. Whether duplicate bytes are stored depends on how file storage is implemented; do not claim it is automatically prevented by the idempotency key alone.

A new key may represent a new logical operation, so the key should be created for the logical upload and reused for its retries—not regenerated on every retry.

### Short flow

```text
Disable button          -> reduces accidental clicks
Same key for retries    -> identifies same logical upload
Backend unique record   -> prevents duplicate operation records
Storage safeguards      -> control duplicate file objects
```

**Memory trick:** **Frontend helps UX; backend enforces correctness; storage needs its own safeguards.**

---

## Scenario 4 — Two workers try to change the same KYC status

### Interviewer question

Two workers read the same application in `PROCESSING`. One tries to mark it `COMPLETED`, while another tries to mark it `REVIEW_REQUIRED`. How do you prevent lost updates or invalid state transitions? When would you use optimistic locking versus pessimistic locking?

### Interview-ready answer

First, make the state transition atomic. For a simple transition, a conditional update can be sufficient:

```sql
UPDATE document_processing
SET status = 'COMPLETED'
WHERE id = :id
  AND status = 'PROCESSING';
```

If exactly one row is updated, the transition succeeded. If zero rows are updated, the state may already have changed, so the application should reread the current state and decide what to do. Add a version check if stale reads across broader updates must be detected:

```sql
UPDATE document_processing
SET status = :new_status,
    version = version + 1
WHERE id = :id
  AND version = :expected_version;
```

**Optimistic locking** is a good option when conflicts are relatively uncommon: the update checks a version, and a stale update affects zero rows or raises a concurrency exception. **Pessimistic locking** such as `SELECT ... FOR UPDATE` can serialize transactions that need to update an existing row when contention is high or strict serialization is required. Keep the transaction short; do not hold a database lock while waiting for Azure or another network call.

These techniques solve concurrent updates to an existing record. They do not replace the unique constraint needed for a first-time idempotency-key insert when no row exists yet.

### Short flow

```text
Need valid state change? -> conditional UPDATE / atomic transition
Stale concurrent update? -> optimistic version check
Must serialize existing row? -> short transaction + SELECT FOR UPDATE
External API call? -> do not hold DB lock while waiting
```

**Memory trick:** **State transition = atomic update; rare conflict = optimistic; serialize an existing row = pessimistic.**

---

## Scenario 5 — Database commit succeeds, but the application crashes before publishing to Kafka

### Interviewer question

Your onboarding service updates the database to `PROCESSING`. It then needs to publish a `DOCUMENT_PROCESSING_REQUESTED` event to Kafka. What happens if the database commit succeeds but the application crashes before publishing the event? How do you avoid losing the event?

### Interview-ready answer

Without a reliable publication mechanism, the database can say `PROCESSING` while no event reaches Kafka. Publishing first and then committing the database has the opposite risk: Kafka may receive an event even if the database transaction rolls back.

Use the **Outbox pattern**. In one local database transaction, update the business record and insert an Outbox event. Both commit or both roll back. A background publisher reads pending Outbox rows, publishes them to Kafka, and marks them as published after successful publication.

There is still a possible duplicate-publication window: Kafka may accept the event, then the publisher may crash before marking the Outbox row as published. The publisher may send it again after recovery. Therefore, consumers must tolerate duplicate delivery, typically with idempotent processing or an Inbox pattern.

Outbox provides reliable event persistence/publication; it does not guarantee that every downstream external side effect happens exactly once.

### Short flow

```text
ONE DB TRANSACTION
  ├─ update onboarding status
  └─ insert Outbox event (PENDING)
           |
         COMMIT
           |
    Outbox publisher -> Kafka
```

**Memory trick:** **Producer-side DB change + event intent → Outbox in the SAME DB transaction.**

---

## Scenario 6 — Kafka redelivers an event after the database commit

### Interviewer question

A Kafka consumer receives event `E123`, updates the processing database, and commits the DB transaction. The application crashes before committing the Kafka offset. When the consumer restarts, Kafka delivers `E123` again. How do you stop the business operation from being applied twice?

### Interview-ready answer

Kafka may redeliver the event because the offset was not committed. This is a normal failure scenario in at-least-once processing; the solution is to make the consumer idempotent.

Use an **Inbox table** with a unique constraint on `event_id` (or another stable event identity). In one local database transaction, the consumer registers the event, applies the business database changes, and marks the event processed. If the event ID is already recorded as successfully processed, the consumer skips the business operation. Commit the Kafka offset only after the database transaction succeeds.

If the DB commit succeeds but the offset commit fails, the event is redelivered and the Inbox check makes the consumer skip the duplicate business effect. The Inbox record and the business DB change must share a transaction; otherwise, a crash could leave the Inbox saying “processed” while the business update did not commit, or vice versa.

This protects database-backed business processing. If the consumer calls an external service such as Azure, the Inbox alone cannot make that external call exactly once; see Scenario 7.

### Short flow

```text
Kafka event E123
      |
DB transaction: insert Inbox + business update + mark processed
      |
   DB COMMIT
      |
Commit Kafka offset

Crash before offset -> redelivery -> Inbox says processed -> skip effect
```

**Memory trick:** **Consumer-side duplicate event → Inbox. DB commit first; Kafka offset commit after.**

---

## Scenario 7 — Azure accepts the request, but the application crashes before saving the Azure operation ID

### Interviewer question

The consumer calls Azure Document Intelligence. Azure accepts the request, but the application crashes before it can persist the Azure operation ID. The database still contains `K123` with status `PROCESSING` and no Azure operation ID. The same request or event arrives again. How do you prevent a duplicate Azure operation?

### Interview-ready answer

This is an **uncertain external outcome**. The database tells us that the business operation was registered, but it does not tell us whether Azure accepted the request. We must not assume that the external call failed just because the operation ID is missing.

First, look for an external idempotency or client-correlation mechanism, or a way to query/reconcile the operation by a client reference, if Azure supports the required capability for that API. Persist the external operation ID as soon as it becomes available. If the operation can be found, resume polling or result retrieval instead of submitting it again.

If the external service offers no way to deduplicate or discover the accepted request, the application may not be able to determine the outcome automatically. Keep the record in a recoverable/uncertain state and use a controlled reconciliation or retry policy rather than immediately creating a new external operation. An Outbox and an Inbox improve event reliability and consumer deduplication, but neither can guarantee exactly-once execution at an external API that does not support it.

### Short flow

```text
Azure call -> response/operation ID not persisted -> app crashes
                         |
                 DB says PROCESSING
                 operation ID missing
                         |
       Do NOT blindly call Azure again
                         |
   lookup/idempotency if supported -> otherwise reconcile
```

**Memory trick:** **Missing external operation ID ≠ proof of failure. Unknown outcome → reconcile before retry.**

---

## Scenario 8 — The same idempotency key arrives with a different request payload

### Interviewer question

A client sends an upload request with key `K123` for document A. Later, the same key is reused for document B or for a different operation. Should the backend return document A's result, or should it process document B?

### Interview-ready answer

An idempotency key identifies one logical operation, so reusing the same key for a different payload must not silently return an unrelated result or accidentally suppress a valid new operation.

Persist enough request identity information to validate that a retry with the same key represents the same logical operation—for example, a request fingerprint/hash or stable document and operation identifiers. If the key exists and the payload matches, return the existing operation state/result. If the key exists but the payload conflicts, reject the request with a clear client error, commonly a conflict response such as HTTP `409`, according to the API contract.

The key should have a defined scope, such as customer/session + operation type, and a defined retention period. A different key usually means a different logical operation, unless another business-level uniqueness rule says the document itself must not be processed twice.

### Short flow

```text
Existing key K123?
  ├─ Same logical request/payload -> reuse existing operation
  └─ Different payload            -> reject key reuse/conflict
```

**Memory trick:** **Same key must mean same intent. Same key + different payload → reject, don't silently reuse.**

---

## Scenario 9 — Events arrive out of order or multiple workers try to update the same application

### Interviewer question

Your application receives two events for the same onboarding application. A `COMPLETED` event arrives before a delayed `PROCESSING` event, or two consumers try to update the same record. How do you stop an old event from moving the application backwards or overwriting a newer status?

### Interview-ready answer

Message delivery order and business-state validity are separate concerns. A Kafka partition key such as `applicationId` can help keep events for the same application in one partition, but it does not solve every out-of-order event problem across different producers, retries, or asynchronous sources.

The consumer should validate the current state and apply only permitted transitions. Use an atomic conditional update, a version/sequence number, or optimistic locking to reject stale updates. For example, an update should not change `COMPLETED` back to `PROCESSING` just because a delayed event arrives. If an event is stale or invalid, record that decision and acknowledge it according to the processing policy; if it is a transient failure, retry instead.

Idempotency handles repeated operations/events; state-transition validation and versioning handle stale or out-of-order updates. They complement one another.

### Short flow

```text
Event arrives -> check event identity + version + current business state
       |
Valid transition? -> apply atomic update
Stale/invalid?    -> ignore or quarantine according to policy
Concurrent write? -> version check / row lock as appropriate
```

**Memory trick:** **Duplicate event → Inbox/idempotency. Stale or invalid transition → state/version validation.**

---

## Scenario 10 — A Kafka consumer keeps failing on one event

### Interviewer question

A document-processing event fails repeatedly because of invalid data or a persistent downstream error. If the consumer retries forever, it may block progress or repeatedly waste resources. How would you handle retries and recovery without losing the event or duplicating business effects?

### Interview-ready answer

Classify errors before retrying. Transient errors—such as temporary network failures or rate limits—may be retried with bounded exponential backoff and jitter. Permanent errors—such as an invalid schema or unrecoverable business validation failure—should not be retried indefinitely.

After a configured retry limit, route the event to a Dead Letter Topic/Queue (DLT/DLQ) with the original event identity, failure reason, attempt count, and correlation details. Alert or expose it for investigation and controlled replay. Before replaying, keep the consumer idempotent: replaying an event must not repeat a business effect that already committed.

Do not commit/acknowledge the message as successfully processed before the relevant durable outcome is secured. Also avoid holding a database transaction or row lock while waiting through long backoff periods. The exact offset/acknowledgment strategy depends on the Kafka consumer framework and retry design.

### Short flow

```text
Event -> process
  ├─ transient failure -> bounded retry/backoff
  ├─ success           -> commit DB, then offset/ack
  └─ repeated/permanent failure -> DLT/DLQ + alert + controlled replay

On replay: Inbox/idempotency still protects business effects.
```

**Memory trick:** **Transient → retry; permanent/repeated → DLQ; replay → still idempotent.**

---

# Final Cheat Sheet — Which tool do I use, and when?

| Problem you see | Primary mechanism | What it protects | What it does NOT guarantee by itself |
|---|---|---|---|
| Same logical HTTP request retried | Idempotency key + stored operation/result | Duplicate business requests | Exactly-once external API execution or duplicate file bytes in storage |
| Two first-time requests race to create the same operation | DB unique constraint | Duplicate operation records | Correct handling of every downstream side effect |
| Several DB changes must succeed or fail together | DB transaction | Atomic local database changes | Atomicity across DB + Kafka + Azure without additional patterns |
| Concurrent updates to an existing row | Conditional update, optimistic or pessimistic locking | Lost updates/invalid concurrent changes | Duplicate first-time inserts where no row exists |
| DB update must reliably result in a Kafka event | Outbox | Lost-event gap between DB commit and event publication | No duplicate Kafka publication or exactly-once external effects |
| Kafka can redeliver an event | Inbox + idempotent consumer | Duplicate consumer-side DB business effects | Exactly-once external API calls |
| Azure accepted a request but outcome/ID is unknown | External idempotency/correlation if supported; reconciliation otherwise | Recovery from uncertain external state | Automatic certainty if the external service provides no lookup/deduplication facility |
| Old event or stale update arrives | Valid transition rules + version/sequence/atomic update | State regressions and stale writes | Duplicate message suppression unless idempotency/Inbox is also used |
| Event repeatedly fails | Bounded retry/backoff + DLT/DLQ + controlled replay | Unbounded retries and operational visibility | Safe replay unless processing is idempotent |
| Same key reused for different content | Request fingerprint/identity validation | Accidental reuse of an idempotency key | Business-level duplicate-document detection unless explicitly designed |

## One-line memory map

```text
HTTP duplicate       -> Idempotency key
Concurrent first row -> UNIQUE constraint
Atomic DB changes    -> Transaction
Concurrent row write -> Conditional update / Optimistic / Pessimistic lock
DB -> Kafka event    -> Outbox
Kafka duplicate      -> Inbox
Azure outcome unsure -> Reconciliation
Old/stale event      -> State + version validation
Repeated failure     -> Retry/backoff + DLQ
Same key, new payload-> Reject key reuse
```

## Five interview rules to remember

1. **Idempotency is about repeated logical operations.** It is not simply a frontend double-click prevention feature.
2. **A unique constraint closes the concurrent insert race.** A prior `SELECT` alone cannot do that.
3. **Outbox and Inbox solve different sides of event reliability.** Outbox is producer-side publication reliability; Inbox is consumer-side duplicate processing protection.
4. **The DB transaction does not include Azure automatically.** A timeout or crash can leave an uncertain external outcome; use idempotency/correlation if supported and reconciliation when needed.
5. **Never claim exactly-once external execution just because you use Kafka, Outbox, Inbox, or a database transaction.** Describe the actual guarantee and the remaining failure window.

**Suggested revision method:** Read the question, cover the answer, explain it aloud in your own words, then reproduce the short flow from memory. Repeat until you can choose the mechanism from the problem, rather than memorizing the mechanism names.
