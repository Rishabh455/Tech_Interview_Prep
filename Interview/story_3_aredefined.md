हाँ — मैंने पूरे case को interview-defensibility के हिसाब से देखा है। मेरी recommendation: इस story को पूरी तरह skip मत करो, लेकिन इसे “major production incident I personally solved” की तरह मत present करो अगर 120 ms → 0.19 ms और exact record count तुम्हारे पास actually verified नहीं हैं।

तुम इसे SQL optimization / performance tuning experience के रूप में रख सकते हो, लेकिन numbers को measured test/staging numbers के रूप में ही बोलना चाहिए अगर वही सच है।

पहले सबसे important correction

तुम्हारे notes में:

> 120 ms → 0.19 ms



यह बहुत बड़ा improvement है — लगभग 99.84% reduction. यह technically possible है, लेकिन interviewer तुरंत पूछ सकता है:

कितने records थे?

production या local/staging?

database instance क्या था?

exact EXPLAIN ANALYZE before/after क्या था?

क्या cache warm था?

सिर्फ index से इतना improvement क्यों आया?

API latency थी या सिर्फ DB execution time?


अगर इनका जवाब नहीं है, तो 0.19 ms को production claim मत बनाना।


---

1. क्या Saudi Bank project में यह case possible है?

हाँ, बिल्कुल possible है।

Banking onboarding system में ऐसा dashboard/query naturally हो सकता है:

Onboarding
     ↓
Identity / KYC / Document verification
     ↓
Validation
     ↓
REVIEW_REQUIRED / PENDING
     ↓
Operations / KYC team
     ↓
Dashboard

Operations dashboard को queries चाहिए होंगी जैसे:

SELECT ...
FROM onboarding
WHERE status = 'REVIEW_REQUIRED'
ORDER BY created_at DESC
LIMIT 50;

और जैसे-जैसे historical onboarding records बढ़ते हैं, dashboard query performance tune करनी पड़ सकती है।

लेकिन: तुम्हारे actual project में यह exact query/index change हुआ था या नहीं — यह मैं verify नहीं कर सकता क्योंकि वह information तुमने अभी तक actual project evidence के रूप में नहीं दी है।

इसलिए interview में safest positioning:

> “I worked on SQL/query performance analysis and index optimization for dashboard-style queries.”



अगर interviewer पूछे “exactly what did you change?”, तभी composite index वाली story दो — only if you can defend it as something you actually did.


---

2. क्या 120 ms → 0.19 ms बोलना चाहिए?

मेरा answer: अभी नहीं, as a production number.

अगर तुमने सच में EXPLAIN ANALYZE/benchmark में यह measure किया था, तब:

> “In our test environment, the query execution time reduced from around 120 ms to around 0.19 ms.”



यह acceptable है।

लेकिन अगर यह number हमने previous preparation में example के तौर पर रखा था, तो इसे actual project fact मत बनाना।

और एक बहुत important distinction:

DB execution time
≠
API response time

अगर interviewer पूछे:

> “Did your API response time reduce from 120 ms to 0.19 ms?”



और तुम्हारे पास सिर्फ SQL execution measurement है, तो answer:

> “No, those numbers were query execution measurements, not end-to-end API latency.”



यह answer technically strong लगेगा।


---

3. तुम्हारे लिए 5-minute version

बस यह याद करो:

BUSINESS PROBLEM
Operations dashboard needed latest REVIEW_REQUIRED cases.

QUERY
WHERE status = 'REVIEW_REQUIRED'
ORDER BY created_at DESC
LIMIT 50

PROBLEM
As historical data increased, query was doing unnecessary
scan/sort work.

ANALYSIS
Used EXPLAIN ANALYZE.

OBSERVATION
Existing index:
(status)

But query pattern:
status filtering
+
created_at ordering

SOLUTION
Composite index:

(status, created_at DESC)

VALIDATION
Compared EXPLAIN ANALYZE before/after
and checked query execution time.

RESULT
Lower database work and better query/API performance.


---

4. Exact interview story — 60–90 seconds

अगर Nomura पूछे:

“Tell me about a SQL optimization you worked on.”

बोलो:

> “One SQL performance issue I worked on was related to an Operations/KYC dashboard in our onboarding platform. The dashboard needed to fetch onboarding cases that were in states such as REVIEW_REQUIRED and display the latest cases first.

We noticed that the query performance was degrading as the amount of historical onboarding data increased. I traced the API flow and used PostgreSQL EXPLAIN ANALYZE to understand the actual execution plan.

The query was filtering by status and ordering by created_at in descending order. We already had an index on status, but it didn't fully support the filter-plus-ordering access pattern.

So we introduced a composite index on (status, created_at DESC) and then compared the execution plan and query execution time before and after the change. The database had a more efficient access path and the query performance improved.

The main learning for me was that I don't add indexes blindly. I first look at the actual query pattern, execution plan, selectivity and data distribution, and then decide whether an index is appropriate.”



यह answer काफी strong है और unnecessary fake details avoid करता है।


---

5. अब Nomura क्या पूछ सकता है?

ये सबसे important हिस्सा है।

Q1. Why did you need an index?

> “The query repeatedly filtered by status and then needed the latest records based on created_at. The existing indexing strategy didn't efficiently support the complete access pattern, so we evaluated a composite index.”




---

Q2. Why (status, created_at)?

WHERE status = ?
ORDER BY created_at DESC

इसलिए:

(status, created_at DESC)

एक natural fit है।

Interview line:

> “The index was designed around the actual query pattern rather than simply indexing individual columns.”




---

6. “Why not just index status?”

यह बहुत likely question है.

Answer:

> “An index on status can help locate matching rows, but after locating them PostgreSQL may still need additional work to produce them in created_at order. The composite index can better support both the filtering and ordering pattern.”




---

7. “Why not (created_at, status)?”

Answer:

> “For this particular query, status is an equality predicate and created_at is used for ordering. Putting status first allows PostgreSQL to narrow down to the relevant status group and then use created_at ordering within that group. However, I would validate the actual plan rather than assuming the column order is universally better.”



यह last sentence बहुत important है।


---

8. “What exactly did EXPLAIN ANALYZE tell you?”

बोलो:

> “I looked at the scan type, actual rows versus estimated rows, sort operations, loops and actual execution time. I wanted to understand whether PostgreSQL was scanning or sorting more data than necessary.”




---

9. “Does an index guarantee faster performance?”

No.

> “No. PostgreSQL's optimizer chooses the execution plan based on estimated cost. For a small table or a low-selectivity predicate, a sequential scan can actually be cheaper.”




---

10. “What is selectivity?”

Example:

1,000,000 rows

REVIEW_REQUIRED = 20,000

Good selectivity.

But:

1,000,000 rows

REVIEW_REQUIRED = 800,000

Poor selectivity.

Interview line:

> “Index usefulness depends not only on the existence of an index but also on data distribution and predicate selectivity.”




---

11. “Why not create indexes on everything?”

> “Indexes improve certain reads but add storage and write-maintenance overhead because INSERT, UPDATE and DELETE operations may need to maintain those indexes. So indexes should be driven by actual query patterns and measured performance.”




---

12. Biggest trap: “Composite key or composite index?”

अगर interviewer बोले:

> “You created a composite key?”



तुम correct करो:

> “It was a composite index, not a composite primary key. We were optimizing query access, not defining entity identity.”



Very important.


---

13. If they ask your exact numbers

अगर तुम्हारे actual benchmark में सच में:

Before: ~120 ms
After:  ~0.19 ms

था, बोल सकते हो:

> “In our test environment, the query execution time reduced from approximately 120 milliseconds to approximately 0.19 milliseconds after the index change.”



फिर immediately be ready for:

“How many records?”

यह number तुम्हें actual रखना पड़ेगा।

अगर तुम्हारे पास actual number नहीं है, मत invent करना।

Instead:

> “The dataset contained historical onboarding records and was significantly larger than the daily onboarding volume. I don't want to quote an inaccurate production record count, but the important measurement was the execution plan and query execution time before and after the index.”



यह fabricated number बोलने से कहीं better है।


---

14. One more important thing: 500–600/day वाला argument

अगर interviewer पूछता है:

> “You only onboard 500–600 customers per day. Why was this slow?”



Answer:

> “The daily ingestion volume is not the same as the total dataset size. Historical records accumulate over time, and the dashboard query runs repeatedly. So query performance depends on the total data volume, selectivity and execution plan, not just the number of records created per day.”



यह बहुत अच्छा answer है।


---

15. Pagination वाला follow-up

अगर interviewer पूछे:

> “What if there are millions of records?”



तब:

> “For the first pages, LIMIT with an appropriate index may be sufficient. But for very deep pagination, OFFSET can become expensive because the database may need to skip many rows. In that case I would consider keyset or cursor pagination using a deterministic ordering such as created_at plus id.”




---

16. Why created_at + id?

Because:

A → 10:30:00
B → 10:30:00
C → 10:30:00

Same timestamp possible.

So:

created_at + id

gives deterministic ordering.


---

17. The one thing I DON'T want you to say

Don't say:

> ❌ “I created the index and the query became 0.19 ms.”



Instead:

> ✅ “I analyzed the query using EXPLAIN ANALYZE, identified the filter-plus-ordering access pattern, introduced a composite index, and validated the improvement using before/after execution-plan measurements.”



Then give 120 → 0.19 ms only if you genuinely measured it.


---

Final decision for your Nomura prep

Don't skip this topic.

But spend only ~5 minutes on it.

You don't need to memorize the 42 sections you pasted.

Your mental model should be:

SLOW DASHBOARD QUERY
        ↓
EXPLAIN ANALYZE
        ↓
What is DB actually doing?
        ↓
status filter
+
created_at ordering
        ↓
Existing index insufficient
        ↓
Composite Index
(status, created_at DESC)
        ↓
Compare execution plan
        ↓
Measure improvement

And remember these 7 keywords:

> EXPLAIN ANALYZE → Sequential Scan → Index Scan → Selectivity → Composite Index → Query Plan → Keyset Pagination



For a Java/Spring backend interview, this is enough depth unless the interviewer specifically takes you into PostgreSQL internals.