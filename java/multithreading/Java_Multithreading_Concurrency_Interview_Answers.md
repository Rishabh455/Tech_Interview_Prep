# Java Multithreading & Concurrency --- Interview Answers

**Level:** 3--4 years of experience\
**Revision style:** Moderate-length explanations with key technical
terms and practical examples.

------------------------------------------------------------------------

## Core Concepts & Thread Basics

### CONC-001 --- Explain multithreading and concurrency in Java.

**Concurrency** means a system can make progress on multiple tasks
during the same period. The tasks do not need to execute at the exact
same instant; the CPU may switch between them using context switching.

**Multithreading** means using multiple threads within a process so
multiple tasks can progress concurrently. It is a mechanism for
achieving concurrency, not the definition of concurrency itself.

**Parallelism** means multiple tasks actually execute at the same time,
typically on multiple CPU cores.

**Example:** In a web application, different threads can handle
different user requests. This improves responsiveness, but shared data
must be protected to avoid race conditions.

**Key point:** Concurrency = multiple tasks in progress; parallelism =
simultaneous execution.

### CONC-002 --- What is the difference between a Process, Thread, and Task in Java?

-   **Process:** An executing application with its own memory space and
    resources.
-   **Thread:** An execution unit within a process. Threads in the same
    process share heap memory but have their own stacks.
-   **Task:** A unit of work that needs to be executed, such as
    processing a request or calculating a result. A task can be
    represented by `Runnable` or `Callable`.

**Example:** A Java application is a process, a worker thread executes
code inside it, and sending an email can be a task assigned to that
thread.

In enterprise applications, we usually separate the task from the thread
and let `ExecutorService` manage execution.

### CONC-003 --- What is the difference between `start()` and `run()`?

`start()` creates a new thread of execution and causes the JVM to
execute `run()` on that new thread. The new thread runs independently of
the calling thread.

Calling `run()` directly does not create a new thread. It is just a
normal method call executed by the current thread.

``` java
Thread thread = new Thread(() -> System.out.println("Working"));

thread.start(); // Executes on a new thread
thread.run();   // Executes like a normal method call
```

A `Thread` instance can be started only once. Calling `start()` again on
the same instance throws `IllegalThreadStateException`.

### CONC-004 --- What is the difference between `Runnable` and `Callable`?

Both represent tasks, but the main difference is the return value and
exception handling.

-   **`Runnable`:** Has a `run()` method, returns no result, and cannot
    declare checked exceptions.
-   **`Callable<V>`:** Has a `call()` method, can return a result of
    type `V`, and can throw checked exceptions.

A `Callable` is commonly submitted to an `ExecutorService`, which
returns a `Future` that can be used to retrieve the result.

``` java
Callable<String> task = () -> "SUCCESS";
Future<String> result = executor.submit(task);
```

**Interview point:** Use `Runnable` when no result is required; use
`Callable` when the task must return a result or report checked
failures.

### CONC-005 --- What is the difference between `wait()` and `sleep()`?

`wait()` and `sleep()` both pause a thread, but they serve different
purposes.

-   **`wait()`** is defined in `Object`. A thread must own that object's
    monitor before calling it. It releases that monitor and waits until
    notification, interruption, timeout, or another permitted wake-up.
-   **`sleep()`** is a static method of `Thread`. It pauses the current
    thread for a specified time and does **not** release any monitor the
    thread already holds.

`wait()` is used for coordination between threads; `sleep()` is used
when a timed pause is needed.

When using `wait()`, check the condition in a `while` loop because a
thread can wake up without the condition being satisfied.

------------------------------------------------------------------------

## Synchronization & Thread Safety

### CONC-006 --- What is synchronization in Java, and why is it required?

Synchronization controls access to shared mutable data when multiple
threads are working at the same time. It helps prevent race conditions
and provides memory-visibility guarantees.

A `synchronized` method or block allows only one thread at a time to
execute code protected by the same monitor. When a thread exits a
synchronized section, its changes become visible to a thread that
subsequently acquires the same monitor.

**Example:** If multiple threads increment a shared counter,
synchronization can ensure that each read-modify-write operation is
performed safely.

Synchronization should protect the smallest logical critical section
needed, because excessive locking can reduce concurrency.

### CONC-007 --- What is the difference between a synchronized method, synchronized block, and `Lock`?

-   **Synchronized method:** Locks the method's monitor. An instance
    method locks `this`; a static synchronized method locks the class's
    `Class` object.
-   **Synchronized block:** Locks a specific object only around the
    required section of code, allowing a smaller critical section.
-   **`Lock`:** An explicit locking API, such as `ReentrantLock`, which
    offers features like `tryLock()`, interruptible lock acquisition,
    optional fairness, and multiple `Condition` objects.

Use `synchronized` for straightforward mutual exclusion. Prefer `Lock`
when its extra control is genuinely required. A lock acquired explicitly
must be released in a `finally` block.

### CONC-008 --- How does `synchronized` work internally? What happens when multiple threads compete for the same monitor?

Every Java object can act as a monitor lock. When a thread enters a
synchronized method or block, it must acquire the associated monitor.

If the monitor is free, the thread acquires it and enters the critical
section. If another thread owns it, the competing thread cannot enter
and typically moves to the `BLOCKED` state while waiting for the
monitor.

When the owning thread exits the synchronized section, it releases the
monitor, allowing another waiting thread to acquire it. The JVM may
optimize monitor implementation depending on contention and runtime
conditions, so it is best not to assume one fixed low-level
implementation.

**Important:** Threads synchronize only when they use the same monitor.
Synchronizing on different objects does not protect the same shared
data.

### CONC-009 --- What is the difference between `synchronized`, `volatile`, and explicit locks?

These tools solve related but different concurrency problems.

-   **`synchronized`:** Provides mutual exclusion and memory visibility.
    Only one thread at a time can execute a section protected by the
    same monitor.
-   **`volatile`:** Ensures that reads and writes of a variable have the
    required visibility and ordering guarantees. It does not make
    compound operations such as `count++` atomic.
-   **Explicit `Lock`, such as `ReentrantLock`:** Provides mutual
    exclusion with additional controls such as `tryLock()`,
    interruptible acquisition, and multiple conditions.

Use `volatile` for simple state flags when there is no compound update
to protect. Use `synchronized` or `Lock` when an operation must be
performed atomically across multiple steps.

### CONC-010 --- What is the difference between `synchronized` and `ReentrantLock`? When would you prefer `ReentrantLock`?

Both provide mutual exclusion and are reentrant, meaning a thread that
already owns the lock can acquire it again.

`synchronized` is simpler and releases the monitor automatically when
execution leaves the synchronized section, including when an exception
occurs.

`ReentrantLock` requires explicit `lock()` and `unlock()`, normally
using `try/finally`, but it offers additional features: `tryLock()` to
avoid waiting indefinitely, interruptible acquisition, optional
fairness, and multiple `Condition` objects.

I would prefer `synchronized` for simple critical sections. I would
choose `ReentrantLock` when I need timed or non-blocking lock attempts,
interruptible acquisition, or more flexible thread coordination.

### CONC-011 --- Why is `volatile` not sufficient for thread-safe operations like `count++`?

`volatile` guarantees visibility of a variable's latest write under the
Java Memory Model, but it does not make a compound operation atomic.

`count++` involves three logical steps: read the current value,
increment it, and write the new value. Two threads can read the same
value and both write the same incremented result, causing a lost update.

For a counter, use `AtomicInteger.incrementAndGet()` or protect the
entire increment operation with `synchronized` or a lock.

**Remember:** `volatile` = visibility and ordering; `AtomicInteger` =
atomic counter operations.

### CONC-012 --- What is a race condition? How would you identify and fix one in production?

A **race condition** occurs when the result depends on the timing or
interleaving of concurrent operations on shared state.

For example, two threads read a counter value of 10, both increment it,
and both write 11. One increment is lost.

In production, I would first identify the affected operation and shared
state, then review thread dumps, logs, metrics, and code paths for
concurrent access. I would reproduce the issue with a focused
concurrency test if possible.

The fix depends on the requirement: use `synchronized` or `Lock` for a
critical section, atomic classes for simple atomic updates, or
concurrent collections for shared data structures. I would add
regression tests and monitor the behavior after deployment.

### CONC-013 --- Multiple threads are updating shared data incorrectly. How would you fix the issue?

First, I would identify the shared mutable data and determine whether
the problem is caused by non-atomic updates, visibility, or unsafe
collection access.

Then I would choose the simplest correct mechanism: - Use
`AtomicInteger` or another atomic class for simple atomic operations. -
Use `synchronized` or `Lock` when multiple steps must execute as one
critical section. - Use a suitable concurrent collection, such as
`ConcurrentHashMap`, for concurrent access to a shared map. - Avoid
shared mutable state where practical by using immutable objects or
passing data between tasks.

Finally, I would add concurrency tests and verify correctness under
load. The solution must protect the full business operation, not just an
individual line of code.

### CONC-014 --- How would you handle high concurrency safely in a Java application?

I would design for both thread safety and capacity rather than simply
creating more threads.

1.  Use a bounded and appropriately sized thread pool instead of
    creating a new thread for every request.
2.  Protect shared mutable state using atomic classes, locks, or
    concurrent collections.
3.  Set timeouts for network, database, and other external calls.
4.  Use bounded queues, backpressure, and suitable rejection handling so
    overload does not grow without limit.
5.  Limit access to constrained resources such as database connections
    using connection pools or semaphores.
6.  Monitor latency, throughput, queue depth, active threads, rejected
    tasks, and downstream errors.

The goal is to maintain correctness and predictable latency without
overwhelming the JVM or downstream services.

------------------------------------------------------------------------

## Concurrent Collections & Atomic Operations

### CONC-015 --- How does `ConcurrentHashMap` achieve thread safety internally?

`ConcurrentHashMap` is designed for concurrent access without locking
the entire map for every operation.

In modern Java implementations, reads are generally non-blocking, while
updates use techniques such as CAS (Compare-And-Set) and synchronization
on individual bins when needed. This allows operations on different
parts of the map to make progress concurrently.

It does not allow `null` keys or values. Individual operations such as
`put()` and `get()` are thread-safe, but a sequence of separate
operations is not automatically one atomic business transaction. For
compound logic, use methods such as `compute()`, `merge()`, or
`putIfAbsent()` where appropriate.

### CONC-016 --- What is the difference between `Collections.synchronizedMap()` and `ConcurrentHashMap`?

`Collections.synchronizedMap()` wraps a normal map and synchronizes its
operations using a common mutex. This is simple, but concurrent access
can become more serialized.

`ConcurrentHashMap` is built for concurrent access and allows better
concurrency between operations on different parts of the map.

Another important difference is iteration: when iterating over a
synchronized map, the caller must manually synchronize on the map
wrapper for the entire iteration. `ConcurrentHashMap` iterators are
weakly consistent; they can tolerate concurrent updates and do not throw
`ConcurrentModificationException` merely because the map changes during
iteration.

Use `ConcurrentHashMap` for frequently accessed shared maps in
concurrent applications. Use a synchronized wrapper when a simple
synchronized collection is sufficient.

### CONC-017 --- What is the difference between `AtomicInteger` and a counter protected by `synchronized`?

`AtomicInteger` provides atomic operations such as `incrementAndGet()`
and `compareAndSet()` without requiring an explicit synchronized block.
It is a good fit for simple independent counter updates.

A `synchronized` counter uses a monitor to ensure that only one thread
at a time executes the protected operation. It is useful when the update
involves multiple variables or several steps that must remain
consistent.

**Example:**

``` java
AtomicInteger count = new AtomicInteger();
count.incrementAndGet();
```

Use `AtomicInteger` for a simple counter. Use synchronization when a
larger operation must be protected as one unit. Atomic operations do not
automatically make unrelated operations or whole business workflows
thread-safe.

------------------------------------------------------------------------

## ExecutorService & Thread Pools

### CONC-018 --- What is `ExecutorService`, and why should we use thread pools?

`ExecutorService` is a Java API that manages task execution and the
lifecycle of worker threads. Instead of creating a new thread for every
task, we submit tasks to an executor, which schedules them on available
workers.

Thread pools reduce thread-creation overhead and allow us to control
concurrency, task queuing, and shutdown. Depending on the implementation
and configuration, tasks may wait in a queue when all workers are busy.

In production, I would choose the pool size and queue capacity based on
the workload, set appropriate timeouts, handle rejected tasks, and shut
down the executor gracefully when it is no longer needed.

### CONC-019 --- What is the difference between `execute()` and `submit()`?

Both methods submit work to an executor, but their interfaces and result
handling differ.

-   **`execute(Runnable)`:** Returns `void`. It is suitable when no
    result handle is required. An exception from the task is not
    captured in a returned `Future`.
-   **`submit(...)`:** Returns a `Future`. It supports `Runnable` and
    `Callable`, allowing the caller to check completion, cancel the
    task, or retrieve a result when available.

For a `Callable`, `submit()` can return the computed value through
`Future.get()`. Exceptions raised by submitted tasks are typically
surfaced through `ExecutionException` when retrieving the result.

### CONC-020 --- How do you size thread pools for CPU-bound versus I/O-bound workloads?

For **CPU-bound tasks**, threads spend most of their time using the CPU.
A pool size around the number of available CPU cores is a reasonable
starting point, followed by measurement and tuning.

For **I/O-bound tasks**, threads spend time waiting for database,
network, or file operations. A larger pool may improve throughput
because other tasks can run while some workers are waiting. However,
making the pool too large can overload downstream systems and increase
context switching and memory usage.

I would measure CPU utilization, task latency, queue depth, throughput,
and downstream capacity rather than rely on one fixed formula. Blocking
workloads and virtual-thread designs may require a different approach.

### CONC-021 --- What happens when too many threads are created? How would you troubleshoot thread exhaustion?

Too many threads consume stack memory and other resources, increase
context switching, and can reduce throughput. In severe cases, the JVM
may fail to create more native threads, resulting in an
`OutOfMemoryError` related to native thread creation.

I would inspect thread dumps and JVM metrics to check thread count,
thread states, blocked threads, and repeated stack traces. I would also
review executor configuration, task duration, queue growth, and blocking
calls.

The fix may include using a bounded thread pool, reducing unnecessary
thread creation, correcting stuck or blocked tasks, setting timeouts,
and limiting concurrency to downstream services. Simply increasing the
maximum thread count can make the problem worse.

### CONC-022 --- Your thread pool gets exhausted under load. How would you identify the bottleneck and resolve it?

First, I would check active thread count, pool size, queue depth,
rejected tasks, task duration, and request latency. A thread dump can
show whether workers are blocked on database calls, network calls,
locks, or slow processing.

Next, I would identify the actual bottleneck. If workers wait on a slow
downstream service, I would review timeouts, connection pools, retries,
and downstream capacity. If tasks are CPU-bound, I would review CPU
utilization and pool sizing. If tasks are stuck on locks, I would
investigate contention and lock ordering.

I would then apply bounded queues, backpressure, appropriate timeouts,
sensible pool sizing, and rejection handling. I would validate the
change with load tests and metrics rather than blindly increasing the
pool size.

------------------------------------------------------------------------

## Asynchronous Programming & Coordination

### CONC-023 --- What is the difference between `Future`, `FutureTask`, and `CompletableFuture`?

-   **`Future<V>`:** An interface representing the result of an
    asynchronous computation. It supports checking completion,
    cancellation, and retrieving the result with `get()`, which may
    block.
-   **`FutureTask<V>`:** A concrete class that implements both
    `Runnable` and `Future`. It can wrap a `Callable` or `Runnable` and
    can be executed by a thread or executor.
-   **`CompletableFuture<V>`:** Provides an API for composing and
    coordinating asynchronous computations. It supports chaining,
    combining results, and handling errors without manually blocking
    after every step.

Use `Future` for basic result tracking, `FutureTask` when a runnable
future task is useful, and `CompletableFuture` when several asynchronous
operations must be composed or coordinated.

### CONC-024 --- What is the difference between `thenApply()`, `thenCompose()`, `thenCombine()`, and `allOf()`?

These methods help compose asynchronous work using `CompletableFuture`.

-   **`thenApply()`** transforms the result of one stage into another
    value. Example: convert a user object into a username.
-   **`thenCompose()`** chains a stage that itself returns a
    `CompletableFuture`, flattening the nested future. Example: fetch a
    user, then asynchronously fetch that user's orders.
-   **`thenCombine()`** combines the results of two independent futures
    after both complete. Example: combine customer details and account
    details.
-   **`allOf()`** returns a future that completes when all supplied
    futures complete. It returns `CompletableFuture<Void>`, so the
    individual results must still be retrieved from their respective
    futures.

**Memory tip:** `thenApply` = transform; `thenCompose` = dependent async
call; `thenCombine` = combine two results; `allOf` = wait for all
futures to complete.

### CONC-025 --- What is the difference between `CountDownLatch`, `CyclicBarrier`, and `Semaphore`? Explain real-world use cases.

These utilities coordinate threads, but they solve different problems.

-   **`CountDownLatch`:** Allows one or more threads to wait until a
    counter reaches zero. It is one-use and cannot be reset. Example:
    wait until three startup tasks finish before accepting traffic.
-   **`CyclicBarrier`:** Makes a group of threads wait until all
    participants reach the same point. It can be reused after the
    barrier is released. Example: multiple workers finish one
    calculation phase before starting the next.
-   **`Semaphore`:** Controls how many threads can access a resource at
    the same time using permits. Example: limit concurrent calls to a
    rate-limited or capacity-limited resource.

**Memory tip:** Latch = wait for completion; barrier = meet at a common
point; semaphore = limit concurrent access.

### CONC-026 --- How would you execute multiple microservice/API calls concurrently using `CompletableFuture` and `ExecutorService`?

I would use a dedicated executor for blocking API calls, start
independent requests asynchronously, and combine the results after they
complete. This avoids waiting for each independent call one by one.

``` java
ExecutorService executor = Executors.newFixedThreadPool(10);

CompletableFuture<User> userFuture =
    CompletableFuture.supplyAsync(() -> userClient.getUser(), executor);

CompletableFuture<Account> accountFuture =
    CompletableFuture.supplyAsync(() -> accountClient.getAccount(), executor);

CompletableFuture<Summary> summaryFuture =
    userFuture.thenCombine(accountFuture, Summary::new);

try {
    Summary summary = summaryFuture.get(2, TimeUnit.SECONDS);
    // Use summary
} catch (TimeoutException e) {
    // Handle timeout
} catch (ExecutionException e) {
    // Handle API/task failure
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    // Preserve interrupt status
}
```

In production, I would also configure connection and request timeouts,
handle failures and partial results according to business requirements,
and ensure the executor is shut down gracefully. The pool size should
reflect expected traffic and downstream capacity.

------------------------------------------------------------------------

## Deadlocks & Thread Troubleshooting

### CONC-027 --- What is a deadlock? How would you detect, troubleshoot, and resolve it?

A **deadlock** occurs when two or more threads wait indefinitely for
locks held by one another, so none of them can proceed.

For example, Thread A holds Lock 1 and waits for Lock 2, while Thread B
holds Lock 2 and waits for Lock 1.

To detect it, I would capture a thread dump using tools such as `jstack`
or JVM monitoring tools and check for a deadlock report, threads in
`BLOCKED` state, and the locks each thread owns or waits for.

To prevent or resolve it, I would acquire locks in a consistent global
order, keep critical sections short, avoid unnecessary nested locks, and
consider `tryLock()` with a timeout when appropriate. I would verify the
fix with concurrency tests and production monitoring.

### CONC-028 --- A thread is stuck in the `BLOCKED` state. How would you identify the cause and fix it?

A thread in Java's `BLOCKED` state is waiting to acquire an intrinsic
monitor used by a `synchronized` method or block. This is different from
a thread waiting on a condition or sleeping.

I would capture multiple thread dumps and identify the monitor the
thread is waiting for, the thread that owns it, and the stack trace of
the owner. If the owner is performing slow work inside a synchronized
block, holding nested locks, or waiting on another resource, that may
explain the blockage.

The fix could be shortening the critical section, moving slow I/O
outside the synchronized block, correcting lock ordering, or reducing
contention. I would not assume every blocked thread indicates a
deadlock; a thread may simply be waiting for a lock that will soon be
released.

### CONC-029 --- What are `ReadWriteLock` and `ReentrantReadWriteLock`, and when would you use them?

`ReadWriteLock` is an interface that maintains a pair of locks: a read
lock and a write lock. Multiple threads can hold the read lock together,
but the write lock requires exclusive access.

`ReentrantReadWriteLock` is a standard implementation. It is useful when
a shared data structure is read frequently and updated less often,
because concurrent readers do not need to block one another.

For example, a frequently read in-memory configuration or cache may
benefit from a read-write lock if its access pattern and critical
sections justify it. However, the extra complexity and write-lock
contention can outweigh the benefit for small or highly contended
workloads, so I would measure before choosing it.

------------------------------------------------------------------------

## 60-Second Final Revision

-   **Concurrency:** Multiple tasks are in progress during the same
    period.
-   **Parallelism:** Tasks actually execute simultaneously.
-   **Runnable:** Task with no returned result.
-   **Callable:** Task that can return a result and throw checked
    exceptions.
-   **Race condition:** Result depends on concurrent timing.
-   **Atomicity:** An operation happens as one indivisible unit.
-   **Visibility:** One thread can see another thread's updates.
-   **`volatile`:** Visibility and ordering, not compound-operation
    atomicity.
-   **`synchronized`:** Monitor-based mutual exclusion.
-   **`ReentrantLock`:** Explicit lock control.
-   **`ExecutorService`:** Manages task execution using worker threads.
-   **`Future`:** Handle for an asynchronous result and task status.
-   **`CompletableFuture`:** Asynchronous composition, chaining,
    combining, and error handling.
-   **`ConcurrentHashMap`:** Map designed for safe concurrent access.
-   **Deadlock:** Threads wait indefinitely on one another's locks.

**Interview principle:** First identify the problem---shared state,
result handling, coordination, CPU/I/O profile, or capacity---then
choose the concurrency tool that fits it.
