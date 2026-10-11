# Callable<V> - Java Interview Notes

## What is Callable?

`Callable<V>` is a functional interface from `java.util.concurrent` (Java 5).

It is like a task that:
- Returns a result of type `V`
- Can throw a checked exception

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

Think of it as:

```text
No Input → call() → Returns V  (or throws Exception)
```

---

## Why Callable? (Problem with Runnable)

`Runnable` has two problems:

```java
void run(); // 1. Cannot return a value
            // 2. Cannot throw checked exceptions
```

**Problem 1 — No return value:**
If a background thread fetches data, you have no way to get that data back using `Runnable`.

**Problem 2 — No checked exceptions:**
You are forced to catch exceptions inside `run()` with try-catch. You cannot let them propagate.

```java
// ❌ Runnable — Ugly workaround needed
Runnable r = () -> {
    try {
        String data = readFile(); // throws IOException
        // Cannot return data from void!
    } catch (IOException e) {
        e.printStackTrace(); // forced to handle here
    }
};

// ✅ Callable — Clean
Callable<String> c = () -> readFile(); // IOException propagates naturally
```

---

## Callable vs Runnable vs Supplier

| Feature | `Runnable` | `Callable<V>` | `Supplier<T>` |
| :--- | :--- | :--- | :--- |
| Package | `java.lang` | `java.util.concurrent` | `java.util.function` |
| Since | Java 1.0 | Java 5 | Java 8 |
| Method | `run()` | `call()` | `get()` |
| Takes Input | No | No | No |
| Returns | `void` | `V` | `T` |
| Throws Checked Exception | ❌ No | ✅ Yes | ❌ No |
| Works with `new Thread(...)` | ✅ Yes | ❌ No (needs `FutureTask`) | ❌ No |
| Works with `CompletableFuture` | ✅ `runAsync()` | ❌ Not directly | ✅ `supplyAsync()` |

**Why not use `Supplier<T>` instead of `Callable<V>`?**

- `Callable<V>` was built for the concurrency framework and allows checked exceptions.
- `Supplier<T>` was built for functional/stream pipelines and does NOT allow checked exceptions.
- So if you use `Supplier` inside `supplyAsync()`, you must catch and wrap all checked exceptions manually.

---

## Compiler: How does `submit()` know Runnable vs Callable?

`ExecutorService` has both overloads:

```java
Future<?> submit(Runnable task);
<T> Future<T> submit(Callable<T> task);
```

Java decides based on your lambda:

```java
// No return → Runnable
executor.submit(() -> System.out.println("Hi"));

// Has return → Callable
executor.submit(() -> 42);

// Throws checked exception → must be Callable (Runnable can't throw)
executor.submit(() -> {
    Thread.sleep(1000); // InterruptedException is checked
    return null;
});
```

If ambiguous, cast explicitly:

```java
executor.submit((Callable<String>) () -> "Hello");
executor.submit((Runnable) () -> System.out.println("Hello"));
```

---

## How to Execute a Callable

```text
              Callable<V>
                   │
      ┌────────────┼────────────┐
      ▼            ▼            ▼
 1. Direct     2. Executor  3. FutureTask
  call()        submit()    + new Thread()
                   │
         4. invokeAll() / invokeAny()
         5. ScheduledExecutorService.schedule()



Method	Blocking behavior
submit()	Non-blocking — returns a Future without waiting for task completion.
invokeAny()	Blocks until one task completes successfully and returns its result.
invokeAll()	Blocks until all tasks complete, or the call is interrupted/times out.
```

---

### 1. Direct Call (Same Thread)

```java
Callable<Integer> task = () -> 10 + 20;

try {
    Integer result = task.call(); // Runs on current thread
    System.out.println(result);   // 30
} catch (Exception e) {
    e.printStackTrace();
}
```

> `Callable` does NOT create a thread. It is just a task definition.

---

### 2. ExecutorService (Most Common)

`submit()` runs the task on a pool thread and immediately returns a `Future`.
submit() returns immediately means it does not wait for the task to finish. It returns a Future that you can use to get the result later.

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

Callable<String> task = () -> {
    Thread.sleep(1000);
    return "Done";
};

Future<String> future = executor.submit(task); // Non-blocking

System.out.println("Doing other work...");

String result = future.get(); // Blocks until task finishes
System.out.println(result);   // Done

executor.shutdown();
```

---

### 3. FutureTask + Thread

> **Q: Can you pass `Callable` directly to `new Thread()`?**
> **A: No. `Thread` only accepts `Runnable`.**

Use `FutureTask` — it wraps a `Callable` and is itself a `Runnable`:

```text
FutureTask<V> implements RunnableFuture<V>
                     which extends Runnable + Future<V>
```

```java
Callable<Integer> callable = () -> {
    int sum = 0;
    for (int i = 1; i <= 5; i++) sum += i;
    return sum;
};

FutureTask<Integer> futureTask = new FutureTask<>(callable);

new Thread(futureTask).start(); // FutureTask is Runnable

Integer result = futureTask.get(); // FutureTask is also Future
System.out.println("Sum: " + result); // 15
```

---

### 4. Batch Execution: `invokeAll()` and `invokeAny()`

#### `invokeAll()` — Run all tasks, wait for ALL to finish

- Blocks until every task completes (or timeout hit).
- Returns `List<Future<T>>` in the same order as input.
- If one task fails, it does NOT stop others. The exception shows up when you call `get()` on that future.
- With timeout: any task not done in time is **cancelled**.

```java
List<Callable<String>> tasks = Arrays.asList(
    () -> "A",
    () -> "B",
    () -> "C"
);

List<Future<String>> futures = executor.invokeAll(tasks);
for (Future<String> f : futures) {
    System.out.println(f.get());
}
```

#### `invokeAny()` — Return result of the FASTEST successful task

- Runs all tasks concurrently.
- Returns the first task's result that succeeds (as `T`, not `Future<T>`).
- Cancels all other tasks automatically.
- If ALL tasks fail → throws `ExecutionException`.

```java
List<Callable<String>> mirrors = Arrays.asList(
    () -> fetchFromMirrorA(),
    () -> fetchFromMirrorB(),
    () -> fetchFromMirrorC()
);

String fastest = executor.invokeAny(mirrors); // Fastest wins
```

---

### 5. ScheduledExecutorService

> **Q: Can `Callable` be used for periodic/recurring tasks?**
> **A: NO. Only for one-time delayed tasks.**

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// ✅ One-time delay — works with Callable
ScheduledFuture<String> sf = scheduler.schedule(() -> "Done", 5, TimeUnit.SECONDS);
```

> `scheduleAtFixedRate()` and `scheduleWithFixedDelay()` only accept `Runnable`.

---

## Problem: Out-of-Order Results

If you submit 3 tasks and task 0 takes 10s but task 1 takes 1s:

```java
// ❌ Problem: We block on task 0 for 10s, even though task 1 finished in 1s
for (Future<Data> f : futures) {
    Data d = f.get(); // Blocks in submission order, not completion order
}
```

### Solution: `ExecutorCompletionService`

It gives you results **as they finish** (fastest first), not in submission order:

```java
CompletionService<String> cs = new ExecutorCompletionService<>(executor);

for (Callable<String> task : tasks) {
    cs.submit(task);
}

for (int i = 0; i < tasks.size(); i++) {
    Future<String> done = cs.take(); // Blocks until ANY task completes
    System.out.println(done.get()); // Process immediately
}
```

Your notes are mostly correct. Let's clarify two important points for interviews.

## 1. Can `Callable` be used with `ScheduledExecutorService`?

Yes, but only for one-time scheduled execution, not periodic execution.

```
ScheduledExecutorService scheduler =
        Executors.newScheduledThreadPool(1);

// One-time delayed execution: Callable is allowed
ScheduledFuture<String> future =
        scheduler.schedule(() -> "Done", 5, TimeUnit.SECONDS);

System.out.println(future.get()); // Done
```

- `schedule(Callable<V>, delay, unit)` → returns `ScheduledFuture<V>`.
- `schedule(Runnable, delay, unit)` → returns `ScheduledFuture<?>`.
- `scheduleAtFixedRate(Runnable, initialDelay, period, unit)` → accepts only `Runnable`.
- `scheduleWithFixedDelay(Runnable, initialDelay, delay, unit)` → accepts only `Runnable`.

Why only `Runnable` for recurring tasks? Because recurring tasks run repeatedly, so there isn't one final return value to collect. A `Callable` returns a result, which fits naturally with one-time execution.

Note: If a periodic `Runnable` throws an uncaught exception, subsequent executions are suppressed.

## 2. Problem: Out-of-order results

Suppose three tasks take different amounts of time:

Task 0

10 seconds

Task 1

1 second

Task 2

3 seconds

With a normal `List<Future<Data>>`, this code processes results in submission order:

```
for (Future<Data> f : futures) {
    Data d = f.get();
}
```

If Task 0 takes 10 seconds, the calling thread may wait 10 seconds at the first `get()`, even if Tasks 1 and 2 have already finished.

### Solution: `ExecutorCompletionService`

It lets you retrieve completed tasks in completion order, rather than submission order.

```
CompletionService<String> cs =
        new ExecutorCompletionService<>(executor);

for (Callable<String> task : tasks) {
    cs.submit(task);
}

for (int i = 0; i < tasks.size(); i++) {
    Future<String> done = cs.take();
    System.out.println(done.get());
}
```

- `cs.submit(task)` submits the task.
- `cs.take()` blocks until a completed task is available.
- `done.get()` retrieves that completed task's result.

If Task 1 finishes first, its result can be processed first—even if Task 0 was submitted first.

## Quick interview revision

| Method               | Blocking?                                     | Returns              |
| -------------------- | --------------------------------------------- | -------------------- |
| `submit()`           | No, not until task completion                 | `Future<T>`          |
| `invokeAny()`        | Yes, until one task succeeds                  | `T`                  |
| `invokeAll()`        | Yes, until all finish or timeout/interruption | `List<Future<T>>`    |
| `schedule(Callable)` | No, returns before delayed execution finishes | `ScheduledFuture<T>` |
| `cs.take()`          | Yes, until a completed task is available      | `Future<T>`          |

Remember: `Future.get()` waits for a particular task; `CompletionService.take()` waits for whichever submitted task completes next.

---

## Exception Handling

Any exception thrown in `call()` is stored inside the `Future`.

```text
call() throws Exception
       ↓
Executor captures it
       ↓
Stored inside Future
       ↓
future.get() → throws ExecutionException
       ↓
e.getCause() → original exception
```

### Full Example

```java
Callable<String> task = () -> { throw new IOException("DB down"); };

Future<String> future = executor.submit(task);

try {
    future.get(3, TimeUnit.SECONDS);

} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Always restore interrupt flag!

} catch (ExecutionException e) {
    Throwable cause = e.getCause(); // The real exception from call()
    System.out.println(cause.getMessage()); // "DB down"

} catch (TimeoutException e) {
    future.cancel(true); // Cancel the task

} catch (CancellationException e) {
    System.out.println("Task was cancelled");

} finally {
    executor.shutdown();
}
```

---

## Callable with Void (No return, but needs checked exceptions)

When you want to throw checked exceptions but don't need to return anything:

```java
Callable<Void> task = () -> {
    riskyIoOperation(); // throws IOException
    return null;        // Void always returns null
};

future.get(); // null on success, ExecutionException on failure
```

> Use `Callable<Void>` instead of `Runnable` when you need to propagate checked exceptions.

---

## Converting Runnable ↔ Callable

```java
Runnable r = () -> System.out.println("Running...");

// Runnable → Callable<Object> (returns null)
Callable<Object> c1 = Executors.callable(r);

// Runnable → Callable<T> with a fixed return value
Callable<String> c2 = Executors.callable(r, "DONE");
```

### Callable → CompletableFuture (manual bridge)

`CompletableFuture.supplyAsync()` takes a `Supplier`, not `Callable`. Wrap it:

```java
public static <T> CompletableFuture<T> fromCallable(Callable<T> callable, Executor executor) {
    return CompletableFuture.supplyAsync(() -> {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new CompletionException(e); // wrap checked → unchecked
        }
    }, executor);
}
```

---

## Modern Java: Virtual Threads (Java 21+)

Before Java 21, `future.get()` (blocking) was avoided in high-throughput apps because it blocked an OS thread.

With **Virtual Threads** (Java 21), blocking is cheap — it parks the virtual thread, not the OS thread:

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    Callable<String> task = () -> {
        Thread.sleep(500); // Parks virtual thread, OS thread is free
        return "Result";
    };

    System.out.println(executor.submit(task).get());
}
```

This makes simple `Callable` + `Future.get()` a viable pattern even at high scale.

---

## Top Interview Q&A

**Q1. Difference between Runnable and Callable?**
- `Runnable.run()` → `void`, no checked exceptions.
- `Callable.call()` → returns `V`, can throw checked exceptions.

---

**Q2. Does Callable create a thread?**
No. It's just a task. A `Thread` or `ExecutorService` is needed to run it.

---

**Q3. Can you pass Callable to `new Thread()`?**
No. `Thread` only accepts `Runnable`. Wrap `Callable` in `FutureTask` first.

---

**Q4. Does Callable need Future to return a value?**
No. You can call `.call()` directly — it returns the value immediately. `Future` is needed only for async execution.

---

**Q5. Is `future.get()` blocking?**
Yes. It blocks the calling thread until the task finishes.
Use `future.get(timeout, unit)` to avoid waiting forever.

---

**Q6. What exceptions does `future.get()` throw?**
1. `ExecutionException` — task threw an exception.
2. `InterruptedException` — waiting thread was interrupted.
3. `TimeoutException` — timed-out version only.
4. `CancellationException` — task was cancelled.

---

**Q7. How to get the real exception from `future.get()`?**
`catch (ExecutionException e) → e.getCause()`

---

**Q8. What does `future.cancel(mayInterruptIfRunning)` do?**
- `true` → interrupts the worker thread if task is running.
- `false` → lets task finish but marks future as cancelled.
- Either way → `get()` throws `CancellationException`.

---

**Q9. Can Callable be used for recurring scheduled tasks?**
No. `scheduleAtFixedRate()` / `scheduleWithFixedDelay()` only accept `Runnable`.
`Callable` is allowed only in `schedule(callable, delay, unit)` (one-time).

---

**Q10. What if all tasks fail in `invokeAny()`?**
It throws `ExecutionException`.

---

**Q11. `execute()` vs `submit()`?**
- `execute(Runnable)` → fire-and-forget, returns void, exceptions crash the thread.
- `submit(Callable)` → returns `Future`, exceptions stored safely.

---

## Common Traps

### ❌ Trap 1: Direct `.call()` thinking it runs on another thread
```java
// ❌ Runs on current thread!
String res = task.call();

// ✅ Runs on pool thread
String res = executor.submit(task).get();
```

---

### ❌ Trap 2: `future.get()` inside submit loop (kills parallelism)
```java
// ❌ Sequential — blocks after every submit
for (Callable<Data> task : tasks) {
    Data d = executor.submit(task).get(); // blocks here!
}

// ✅ Submit all first, then collect
List<Future<Data>> futures = new ArrayList<>();
for (Callable<Data> t : tasks) futures.add(executor.submit(t));
for (Future<Data> f : futures) process(f.get());
```

---

### ❌ Trap 3: Swallowing InterruptedException
```java
// ❌ Hides interruption from caller
catch (InterruptedException e) { e.printStackTrace(); }

// ✅ Restore the interrupt flag
catch (InterruptedException e) { Thread.currentThread().interrupt(); }
```

---

### ❌ Trap 4: Forgetting executor.shutdown()
```java
// ❌ JVM won't exit — pool threads are non-daemon!
ExecutorService e = Executors.newFixedThreadPool(4);

// ✅ Always shutdown
executor.shutdown();
```

---

## Quick Reference

```text
┌───────────────────────┬──────────────────────────────────────────────────┐
│ Method                │ V call() throws Exception                        │
│ Package               │ java.util.concurrent (Java 5)                    │
│ Arguments / Return    │ None → Returns V                                 │
│ Checked Exceptions    │ Yes                                              │
│ With Thread           │ Wrap in FutureTask → new Thread(futureTask)      │
│ With ExecutorService  │ executor.submit(callable) → Future<V>            │
│ Batch                 │ invokeAll() (wait all) / invokeAny() (fastest)   │
│ Out-of-order results  │ ExecutorCompletionService.take().get()           │
│ Exception from call() │ ExecutionException → e.getCause()                │
│ No return needed      │ Callable<Void> with return null                  │
│ Periodic tasks        │ ❌ Not supported (only one-time schedule)         │
└───────────────────────┴──────────────────────────────────────────────────┘
```
