हाँ। Story 2 को 7 मिनट में cover करने के लिए पूरी 44-question वाली list पढ़ने की जरूरत नहीं है। Nomura के Java Developer / Lead interview में इस story से सबसे ज्यादा value Idempotency + DB concurrency + Kafka + Outbox/Inbox + failure handling में है।

नीचे exactly 7-minute revision pack है।

STORY 2 — DUPLICATE REQUEST / IDEMPOTENCY

7-Minute Nomura Interview Pack


---

⏱️ 0:00–1:00 — Main Story

अगर interviewer पूछे:

“Tell me about a production problem you faced and how you solved it.”

> “One issue we faced in the onboarding workflow was duplicate processing of the same business operation. For example, an identity-verification request could reach our backend more than once because of frontend retries, repeated submission, or network-related retries.

The problem was that two HTTP requests could represent the same logical business operation, and without idempotency the downstream operation could be executed twice.

We introduced an idempotency key for each logical operation and persisted it with the processing record. We also added a unique database constraint on the idempotency key.

When the same request arrives again, we check the existing processing state. If it is already completed, we return the existing result. If it is still processing, we return the current state instead of starting the operation again.

For concurrent updates to an existing record, we use optimistic locking with a version field. This separated duplicate-request handling from concurrent-update handling and made the workflow safer against retries and repeated processing.”



याद रखो:

> Same logical operation ≠ necessarily same HTTP request.



यही पूरी story का core है।


---

⏱️ 1:00–2:00 — Idempotency Questions

Q1. What is idempotency?

> “Idempotency means executing the same logical operation multiple times produces the same business effect as executing it once.”



Example:

Request 1 → K123 → Process
Request 2 → K123 → Existing operation
                         ↓
                    Don't process again


---

Q2. What is an idempotency key?

> “It is a unique identifier representing one logical business operation.”



Example:

POST /identity-verification
Idempotency-Key: K123

If retry comes with K123, backend recognizes it as the same operation.


---

Q3. Why not simply use sessionId?

Trap question.

> “Session ID identifies the onboarding session, but it doesn't necessarily identify one specific business operation. A single onboarding session may contain multiple operations. Therefore, a dedicated idempotency key gives us operation-level uniqueness.”



🔥 Important distinction:

sessionId
→ Which onboarding journey?

idempotencyKey
→ Which logical operation?


---

⏱️ 2:00–3:00 — Database + Concurrency

Q4. Why do you need a UNIQUE constraint?

> “Because application-level checking alone has a race condition.”



Request A → SELECT → Not Found
Request B → SELECT → Not Found

Both think:
"No record exists"

Both INSERT ❌

With:

UNIQUE(idempotency_key)

only one insert succeeds.

Killer line:

> “Application logic handles the business response; the database constraint provides the final data-integrity guarantee.”




---

Q5. What if two same-key requests arrive simultaneously?

> “Both may initially see no record. The unique constraint ensures only one can create the idempotency record. The other request handles the uniqueness conflict and then retrieves the existing processing record and returns its current state or result.”




---

Q6. Why isn't optimistic locking enough?

🔥 Very likely follow-up.

> “Optimistic locking protects updates to an existing record. For the initial creation, there may be no row to lock or version to check. Therefore, the unique constraint handles the initial race, while optimistic locking handles concurrent updates to an existing record.”



Remember:

First INSERT race
        ↓
UNIQUE CONSTRAINT

Existing row update
        ↓
OPTIMISTIC LOCKING


---

⏱️ 3:00–4:00 — Optimistic Locking

Q7. What is optimistic locking?

> “Optimistic locking detects conflicting updates using a version field instead of holding a database lock.”



Example:

version = 5

Request A → version 5 → update → version 6

Request B → still has version 5
           ↓
       update fails

JPA:

@Version
private Long version;


---

Q8. Does optimistic locking lock the row?

Trap.

> “No. It doesn't hold a database lock during normal processing. It detects stale updates when the update is performed.”




---

Q9. Why not pessimistic locking?

> “Because our workflow may involve an external service. I don't want to hold a database lock while waiting for a network call. Optimistic locking avoids keeping the DB lock open during that external processing.”



Strong architecture answer:

DB lock
   ↓
Azure / external API
   ↓
slow response
   ↓
long-held lock ❌

Instead:

Read
 ↓
External processing
 ↓
Update with version
 ↓
Detect conflict


---

⏱️ 4:00–5:00 — Kafka Questions

Q10. Why Kafka?

> “The document or identity processing can be asynchronous and potentially long-running. Kafka decouples the producer from the consumer and allows independent scaling and buffering.”



Don't say:

❌ “Kafka was mandatory.”

Say:

> “REST was technically possible, but Kafka was useful for asynchronous and decoupled processing.”




---

Q11. Can Kafka deliver the same event twice?

> “Yes. With at-least-once processing, duplicate delivery is possible.”



Classic scenario:

Kafka
 ↓
Consumer
 ↓
Business processing
 ↓
DB COMMIT
 ↓
💥 Crash
 ↓
Offset NOT committed
 ↓
Kafka redelivers

Therefore:

> “The consumer needs to be idempotent.”




---

Q12. How do you make Kafka consumer idempotent?

> “We can maintain an Inbox table with a unique event ID or idempotency key. Before performing the business operation, the consumer checks whether that event has already been successfully processed.”



Kafka Event
    ↓
Inbox
    ↓
Already processed?
   /       \
 YES       NO
 ↓          ↓
Skip      Process


---

⏱️ 5:00–6:00 — Outbox / Inbox / Crash Traps

Q13. Outbox vs Inbox?

Must memorize.

> “Outbox is producer-side; Inbox is consumer-side.”



Producer
   ↓
OUTBOX
   ↓
Kafka
   ↓
INBOX
   ↓
Consumer

Outbox:

> “Ensures a database change and event publication request are persisted together.”



Inbox:

> “Prevents duplicate processing of the same event.”




---

Q14. What if DB commit succeeds but Kafka offset commit fails?

🔥 Very likely senior follow-up.

> “Kafka can redeliver the event. That's acceptable because our consumer is idempotent. The Inbox record shows that the business operation was already processed, so we skip the duplicate operation and commit the offset.”



DB SUCCESS
 ↓
Crash
 ↓
Offset not committed
 ↓
Kafka redelivery
 ↓
Inbox = PROCESSED
 ↓
Skip


---

Q15. Does Outbox give exactly-once processing?

Trap.

> “No. Outbox provides reliable event persistence/publication. It does not guarantee exactly-once execution of external side effects.”



This sentence is very important.


---

⏱️ 6:00–7:00 — External API + Banking-Level Questions

Q16. What if Azure succeeds but application crashes?

> “That's an external side-effect failure window. Outbox doesn't automatically solve it. We need processing-state tracking, an external operation ID where available, and an idempotency or reconciliation mechanism so that recovery doesn't blindly submit the operation again.”




---

Q17. What if same idempotency key comes while operation is PROCESSING?

> “We don't start another operation. We return the current processing status, or depending on the API contract, return an appropriate in-progress response.”



K123
 ↓
PROCESSING
 ↓
Second request
 ↓
Return PROCESSING


---

Q18. What if status is COMPLETED?

> “Return the existing result instead of executing the operation again.”




---

Q19. What if status is FAILED?

> “It depends on the business semantics. We need to distinguish a retryable failure from a terminal failure. A retry may be allowed for transient failures, while a terminal business failure should return the existing outcome or require a new business operation.”



This answer shows maturity—don't say every FAILED request should automatically retry.


---

Q20. Why is this important in banking?

> “Duplicate processing can create inconsistent customer state, duplicate downstream actions, incorrect verification results, or potentially duplicate financial operations. Therefore, idempotency and reliable state management are important for data integrity and auditability.”




---

🔥 NOMURA LEAD — 5 TRAP QUESTIONS

इनके answers जरूर याद करो।

Trap 1:

“If request is POST, why are you making it idempotent?”

> “HTTP POST is generally non-idempotent by semantics, but specific business operations implemented through POST can still be designed to be idempotent using an idempotency key.”




---

Trap 2:

“Does unique constraint itself make the API idempotent?”

> “No. It prevents duplicate records, but the application still needs to interpret the existing record and return the correct existing result or state.”




---

Trap 3:

“Does idempotency mean the request is never executed twice?”

> “Not necessarily at the transport level. It means the business effect is protected from being duplicated.”



🔥 This is an excellent distinction.


---

Trap 4:

“Does Kafka exactly-once solve your duplicate problem?”

> “Not by itself. Kafka's delivery semantics and business-level idempotency are separate concerns. I would still design the consumer to handle duplicate events safely.”




---

Trap 5:

“Why not simply synchronize the Java method?”

> “A JVM-level synchronized block only coordinates threads within one application instance. In a distributed deployment with multiple instances, it cannot provide global idempotency. Database constraints and persistent state work across instances.”



🔥 Very important Java Developer question.


---

⚡ BONUS JAVA QUESTION

“Why can't you use a static HashSet to store processed IDs?”

> “Because it is in-memory, lost on restart, doesn't work reliably across multiple application instances, and doesn't provide durable transactional consistency. Idempotency state should be persisted in a shared durable store such as the database.”




---

🧠 LAST-MINUTE REVISION — JUST MEMORIZE THIS

अगर interview से 2 मिनट पहले देखना हो, सिर्फ यह पढ़ना:

DUPLICATE REQUEST
       ↓
IDEMPOTENCY KEY
       ↓
UNIQUE DB CONSTRAINT
       ↓
Existing State?
       ↓
COMPLETED → Return existing result
PROCESSING → Return current status
FAILED → Apply retry/business rules

6 distinctions:

Idempotency
→ Same logical request

Unique Constraint
→ Protect initial duplicate insert

Optimistic Locking
→ Protect concurrent updates

Kafka
→ Async event delivery

Outbox
→ Reliable event publication

Inbox
→ Duplicate event processing protection

5 golden sentences:

> “Same HTTP request and same business operation are not necessarily the same thing.”



> “Idempotency protects business effects, not merely HTTP requests.”



> “The database unique constraint is the final protection against concurrent duplicate creation.”



> “Optimistic locking handles stale concurrent updates; it doesn't solve initial duplicate insertion.”



> “Outbox provides reliable event publication; Inbox provides duplicate-event protection.”



सबसे important architecture:

API
               ↓
       Idempotency Key
               ↓
        Database Record
               ↓
            Outbox
               ↓
             Kafka
               ↓
            Inbox
               ↓
           Consumer
               ↓
        External Service
               ↓
       Result + State

बस इस flow को confidently explain कर पाओ, तो Story 2 के लगभग सारे follow-ups इसी से derive हो जाएंगे।


हाँ, तुम्हारा counter वाला example भी race condition ही है, लेकिन race condition सिर्फ “same time पर update” होने तक limited नहीं है। इसका broader meaning है:

> जब दो concurrent operations का result इस बात पर depend करे कि उनके बीच execution का timing/order क्या रहा।



अब तुम्हारे idempotency वाले case को exactly flow से समझो।

1. Initially DB में कोई row नहीं है

मान लो:

idempotency_key = K123

और दो requests same business operation के लिए एक साथ आ गईं:

Request A → K123
Request B → K123

DB initially:

No record for K123


---

2. दोनों requests पहले check करती हैं

Code कुछ ऐसा कर सकता है:

if (!repository.existsByIdempotencyKey("K123")) {
    repository.save(...);
}

अब timing देखो:

Time →

Request A                  Request B
   |                           |
   |-- SELECT K123 ----------> |
   |                           |
   |      NOT FOUND            |
   |                           |
   |                           |-- SELECT K123
   |                           |   NOT FOUND
   |                           |
   |-- INSERT K123             |
   |                           |
   |                           |-- INSERT K123

यही race condition है।

क्यों?

क्योंकि दोनों threads ने अपना decision same shared state के basis पर लिया, लेकिन बीच में दूसरे thread ने state बदल दी।

A ने पूछा:

> “K123 already exists?”



Answer: No

B ने भी पूछा:

> “K123 already exists?”



Answer: No

फिर दोनों ने decide किया:

> “ठीक है, मैं insert कर सकता हूँ।”



लेकिन ideally हमें एक ही logical operation की एक ही record चाहिए।


---

3. अब UNIQUE constraint लगाते हैं

DB में:

UNIQUE(idempotency_key)

अब same situation:

Request A → INSERT K123 → SUCCESS ✅

Request B → INSERT K123 → FAIL ❌
                         UNIQUE constraint violation

Database कहता है:

> “K123 already exists. दूसरा record नहीं बना सकता।”



इसलिए DB final protection देता है।


---

4. यहाँ तुम्हारे counter example से difference क्या है?

तुम्हारा example:

counter = 1

Thread A → read 1
Thread B → read 1

A → 2
B → 2

Expected → 3
Actual   → 2

यह भी race condition है क्योंकि दोनों threads ने stale value 1 पढ़ी।

हमारे idempotency case में:

A → check → NOT FOUND
B → check → NOT FOUND

A → INSERT
B → INSERT

यहाँ problem lost update नहीं है।

यहाँ problem है:

> दो concurrent requests ने एक ही initial state देखकर दोनों ने operation proceed कर दिया।



इसलिए race condition के अलग-अलग forms हो सकते हैं:

Case	Race condition

Counter	Lost update
Idempotency	Duplicate creation
Inventory	Overselling
Bank balance	Incorrect concurrent update
Booking	Two users getting same seat



---

अब सबसे important: "Row ही नहीं है तो Optimistic Locking कैसे?"

यही key point है।

Suppose:

K123 → कोई row नहीं

Optimistic locking normally काम करता है:

Existing row
      ↓
version = 5
      ↓
A reads version 5
B reads version 5
      ↓
A updates → version 6
B tries version 5 → FAIL

लेकिन हमारे initial case में:

K123 → NO ROW

तो:

> Optimistic locking के लिए version ही नहीं है।



इसलिए initial creation race को handle करने के लिए:

UNIQUE(idempotency_key)

use करते हैं।

फिर row create हो जाने के बाद:

id | idempotency_key | status     | version
-------------------------------------------
1  | K123            | PROCESSING | 1

अब concurrent updates के लिए:

Optimistic Locking
        ↓
@Version
        ↓
version = 1 → 2


---

पूरा flow याद रखो

Request A ─────┐
               ├── Same Idempotency Key K123
Request B ─────┘
                 ↓
          Check existing record
                 ↓
          No record exists
                 ↓
       Both try to INSERT
                 ↓
       ┌───────────────────┐
       │ UNIQUE constraint │
       └───────────────────┘
          ↓             ↓
       A SUCCESS      B FAILS
                         ↓
                Fetch existing record
                         ↓
                Return current result

और row बनने के बाद:

Existing Row
     ↓
version = 1
     ↓
Concurrent Updates
     ↓
Optimistic Locking
     ↓
Only valid/latest update succeeds

Interview में killer answer:

> “Race condition here is not a lost-update problem. It is a check-then-act race. Two concurrent requests can both check that the idempotency key doesn't exist and then both try to create the record. A database UNIQUE constraint makes the creation atomic from a data-integrity perspective, allowing only one request to create the record. Once the row exists, optimistic locking can protect subsequent concurrent updates.”



बस यह distinction याद रखो:

No row → UNIQUE constraint → duplicate creation protection

Existing row → @Version → concurrent update protection
