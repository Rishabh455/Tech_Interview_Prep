// before optimization
 Limit  (cost=21474.75..21480.58 rows=50 width=56) (actual time=34.907..40.052 rows=50 loops=1)
   Buffers: shared hit=11444 read=3214
   ->  Gather Merge  (cost=21474.75..26268.22 rows=41084 width=56) (actual time=34.905..40.047 rows=50 loops=1)
         Workers Planned: 2
         Workers Launched: 2
         Buffers: shared hit=11444 read=3214
         ->  Sort  (cost=20474.72..20526.08 rows=20542 width=56) (actual time=25.926..25.929 rows=44 loops=3)
               Sort Key: created_at DESC
               Sort Method: top-N heapsort  Memory: 34kB
               Buffers: shared hit=11444 read=3214
               Worker 0:  Sort Method: top-N heapsort  Memory: 35kB
               Worker 1:  Sort Method: top-N heapsort  Memory: 34kB
               ->  Parallel Seq Scan on onboarding_case  (cost=0.00..19792.33 rows=20542 width=56) (actual time=0.019..23.197 rows=16667 loops=3)
                     Filter: ((status)::text = 'REVIEW_REQUIRED'::text)
                     Rows Removed by Filter: 316667
                     Buffers: shared hit=11370 read=3214
 Planning:
   Buffers: shared hit=29
 Planning Time: 0.109 ms
 Execution Time: 40.070 ms
(20 rows)


// after optimization

             QUERY PLAN                                                                        
---------------------------------------------------------------------------------------------------------------------------------------------------------------
 Limit  (cost=0.42..43.52 rows=50 width=56) (actual time=0.021..0.181 rows=50 loops=1)
   Buffers: shared hit=34 read=19
   ->  Index Scan using idx_onboarding_status_created on onboarding_case  (cost=0.42..42496.62 rows=49300 width=56) (actual time=0.020..0.176 rows=50 loops=1)
         Index Cond: ((status)::text = 'REVIEW_REQUIRED'::text)
         Buffers: shared hit=34 read=19
 Planning:
   Buffers: shared hit=19 read=1
 Planning Time: 0.176 ms
 Execution Time: 0.193 ms
(9 rows)


1\. Initially, the query took around 40 milliseconds to fetch the latest 50 onboarding cases with `REVIEW_REQUIRED` status.

2\. PostgreSQL was performing a Sequential Scan, scanning a large portion of the table and filtering records.

3\. It was also sorting the matching records by `created_at DESC`.

4\. To optimize the query, I created a composite index on `(status, created_at DESC)`.

5\. After optimization, PostgreSQL used an Index Scan, avoiding the full table scan and separate sorting.

6\. Execution time dropped from 40.07 ms to 0.193 ms, improving performance by approximately 99.5%.