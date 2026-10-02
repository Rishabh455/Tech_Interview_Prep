Absolutely. Below is the **complete revision sheet from our discussion**, reorganized into an interview-focused format with **Q&A → corrections → traps → follow-ups → code → mental models**.

# Java Concurrency Interview Revision

## Runnable, Callable, Future & CompletableFuture

---

# 1. Runnable

## Q1. What is Runnable?

### Interview Answer

`Runnable` is a functional interface that represents a task that can be executed by a thread.

It contains one abstract method:

```java
void run();
```

`Runnable` itself represents the **task**, while `Thread` provides the mechanism to execute that task.

Example:

```java
Runnable task = () -> {
    System.out.println("Running task");
};

Thread t = new Thread(task);
t.start();
```

When `start()` is called, a new thread of execution is created/scheduled, and the JVM eventually invokes the `run()` method on that thread.

---

## Q2. Does Runnable create a thread?

### Correct Answer

No.

This is an important interview distinction.

> `Runnable` represents the task. `Thread` represents the thread that executes the task.

```text
Runnable
   ↓
Represents TASK

Thread
   ↓
Executes TASK
```

Example:

```java
Runnable task = () -> System.out.println("Hello");

Thread thread = new Thread(task);
thread.start();
```

Here:

* `Runnable` → defines what should be done.
* `Thread` → provides the execution mechanism.
* `start()` → starts a new thread of execution.

### Interview Trap

❌ "Runnable is used to create multithreading."

Better:

✅ "Runnable represents a task that can be executed by a thread."

---

# 2. Runnable vs Extending Thread

## Q3. Why is implementing Runnable generally preferred over extending Thread?

### Interview Answer

Java supports single inheritance. If my class extends `Thread`, it cannot extend another class.

With `Runnable`, I separate the task from the thread and can still extend another class.

Example:

```java
class MyTask implements Runnable {

    @Override
    public void run() {
        System.out.println("Task running");
    }
}
```

Then:

```java
Thread thread = new Thread(new MyTask());
thread.start();
```

This provides better separation of responsibilities and allows the same task to be used with different execution mechanisms such as `ExecutorService`.

### Interview Trap

Don't say:

> "Runnable is always better than Thread."

Better:

> "Implementing Runnable is generally preferred when I want to separate the task from the thread and avoid the limitations of single inheritance."

---

# 3. start() vs run()

## Q4. What is the difference between start() and run()?

This is a very common trap.

```java
thread.start();
```

starts a new thread of execution.

But:

```java
thread.run();
```

is simply a normal method invocation. It executes `run()` on the **current thread**.

### Example

```java
Thread t = new Thread(() -> {
    System.out.println(Thread.currentThread().getName());
});

t.start();
```

The task executes on a separate thread.

But:

```java
t.run();
```

does NOT create a new thread.

### Interview Answer

> "`start()` starts a new thread and causes the JVM to invoke `run()` on that thread. Calling `run()` directly is just a normal method call and does not create a new thread."

---

# 4. Callable

## Q5. What is Callable?

`Callable<T>` is a functional interface used to represent a task that:

1. Returns a result.
2. Can throw a checked exception.

Its method is:

```java
T call() throws Exception;
```

Example:

```java
Callable<Integer> task = () -> {
    return 10 + 20;
};
```

---

# 5. Runnable vs Callable

## Q6. Difference between Runnable and Callable?

| Feature              | Runnable                         | Callable                    |
| -------------------- | -------------------------------- | --------------------------- |
| Method               | `run()`                          | `call()`                    |
| Return value         | `void`                           | `T`                         |
| Checked exception    | Cannot declare checked exception | Can throw checked exception |
| Functional interface | Yes                              | Yes                         |
| Typical use          | Task without result              | Task with result            |

### Interview Answer

> "`Runnable` is used when I need to execute a task without returning a result. `Callable<T>` is used when the task needs to return a result or throw a checked exception. Runnable's `run()` returns void, whereas Callable's `call()` returns a value of type T."

### Interview Trap

Don't say:

> "Runnable cannot give any information about execution."

More precise:

> "`Runnable.run()` does not return a result."

You can still monitor or control a Runnable using other mechanisms.

---

# 6. ExecutorService

## Q7. What is ExecutorService?

`ExecutorService` is a higher-level concurrency API used to manage and execute tasks using a pool of threads.

Example:

```java
ExecutorService executor =
        Executors.newFixedThreadPool(2);
```

Instead of manually creating:

```java
new Thread(...)
```

for every task, we submit tasks to the executor.

Example:

```java
executor.submit(task);
```

The executor manages the worker threads.

### Benefits

* Thread pooling
* Task submission
* Reuse of threads
* Better resource management
* Supports Runnable and Callable
* Can return Future objects

---

# 7. Future

## Q8. What is Future?

`Future<T>` represents the result of an asynchronous computation.

Example:

```java
Callable<Integer> task = () -> {
    Thread.sleep(2000);
    return 30;
};

Future<Integer> future = executor.submit(task);
```

Conceptually:

```text
Callable
   ↓
ExecutorService.submit()
   ↓
Future
   ↓
Result available later
```

The `Future` acts as a **handle to the result**.

---

# 8. Does Callable depend on Future?

## Q9. Does Callable require Future to return a result?

No.

This is an important correction.

`Callable` itself returns a value from:

```java
call()
```

For example:

```java
Callable<Integer> task = () -> 30;
```

The problem is that when we execute that task asynchronously using an executor, we need a way to retrieve the result later.

`Future` provides that handle.

So:

```text
Callable
→ defines a task that returns a value

Future
→ represents the result of that asynchronous execution
```

---

# 9. Future.get()

## Q10. What happens when Future.get() is called?

Example:

```java
Future<Integer> future = executor.submit(task);

System.out.println("Doing other work...");

Integer result = future.get();

System.out.println(result);
```

`submit()` returns immediately.

The worker thread can execute the task while the calling thread continues doing other work.

But:

```java
future.get();
```

blocks the calling thread if the result is not yet available.

### Flow

```text
Main Thread
    |
    | submit()
    |
    |-----------------------> Worker Thread
    |                              |
    |                       execute Callable
    |                              |
    | "Doing other work"           |
    |                              |
    | future.get()                 |
    |      ↓                       |
    |    BLOCKS <------------------|
    |                              |
    |         result = 30
```

### Important

Do NOT say:

> "Future is synchronous."

More precise:

> "`ExecutorService.submit()` is asynchronous, but calling `Future.get()` is blocking."

---

# 10. Does Future.get() always wait 2 seconds?

No.

Suppose the task contains:

```java
Thread.sleep(2000);
```

You should not say:

> "get() waits exactly 2 seconds."

It waits **until the result becomes available**.

If the task has already completed:

```java
future.get();
```

may return immediately.

If the task is still running, the calling thread waits.

---

# 11. CompletableFuture

## Q11. What is CompletableFuture?

`CompletableFuture` is an advanced asynchronous programming API in Java.

It allows us to:

* Run tasks asynchronously
* Chain asynchronous operations
* Transform results
* Combine independent operations
* Handle exceptions
* Build asynchronous pipelines

Example:

```java
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> 30);
```

---

# 12. Future vs CompletableFuture

## Q12. What is the difference between Future and CompletableFuture?

### Future

```java
Future<Integer> future =
        executor.submit(task);

Integer result = future.get();
```

`get()` is blocking.

Future is primarily a mechanism for retrieving the result of an asynchronous computation.

### CompletableFuture

```java
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> 30);

future.thenAccept(
    result -> System.out.println(result)
);
```

We can attach a callback/continuation that executes when the result becomes available.

### Key difference

```text
Future
→ submit
→ get
→ retrieve result
→ get() can block

CompletableFuture
→ async computation
→ compose/transform
→ callback
→ build async pipeline
```

### Important Interview Trap

Don't say:

> "CompletableFuture is always non-blocking."

That's incorrect.

CompletableFuture has blocking methods too:

```java
future.get();
future.join();
```

Better answer:

> "`CompletableFuture` provides non-blocking composition APIs such as `thenApply`, `thenCompose`, and `thenAccept`, but methods such as `get()` and `join()` can block."

---

# 13. supplyAsync vs runAsync

## Q13. Difference between supplyAsync() and runAsync()?

### runAsync()

Used when there is no return value.

```java
CompletableFuture<Void> future =
    CompletableFuture.runAsync(() -> {
        System.out.println("Running");
    });
```

Conceptually:

```text
runAsync
→ Runnable
→ no result
```

### supplyAsync()

Used when we need a result.

```java
CompletableFuture<Integer> future =
    CompletableFuture.supplyAsync(() -> 10);
```

Conceptually:

```text
supplyAsync
→ Supplier
→ result
```

### Memory Trick

```text
runAsync()
→ RUN
→ Runnable
→ no result

supplyAsync()
→ SUPPLY
→ Supplier
→ result
```

---

# 14. thenApply()

## Q14. What does thenApply() do?

`thenApply()` transforms the result of a previous stage.

It works conceptually like:

```text
A → Function → B
```

Example:

```java
CompletableFuture<Integer> future =
    CompletableFuture.supplyAsync(() -> 10);

CompletableFuture<Integer> result =
    future.thenApply(x -> x * 2);
```

Flow:

```text
10
 ↓
thenApply()
 ↓
20
```

`thenApply()` uses:

```java
Function<T, R>
```

It takes one value and returns another value.

---

# 15. thenAccept()

## Q15. What does thenAccept() do?

`thenAccept()` consumes the result but doesn't produce another result.

It uses:

```java
Consumer<T>
```

Example:

```java
future.thenAccept(
    result -> System.out.println(result)
);
```

Flow:

```text
Result
 ↓
Consumer
 ↓
void
```

Example:

```java
CompletableFuture.supplyAsync(() -> 10)
    .thenAccept(x -> System.out.println(x));
```

Output:

```text
10
```

---

# 16. thenRun()

## Q16. What does thenRun() do?

`thenRun()` executes an action after the previous stage completes.

It doesn't receive the previous result.

It doesn't return a result.

It uses:

```java
Runnable
```

Example:

```java
future.thenRun(() -> {
    System.out.println("Done");
});
```

Conceptually:

```text
Previous stage
     ↓
thenRun()
     ↓
execute Runnable
```

---

# 17. thenApply vs thenAccept vs thenRun

This is an extremely important interview table.

| Method         | Interface | Receives previous result? | Returns result? |
| -------------- | --------- | ------------------------: | --------------: |
| `thenApply()`  | Function  |                       Yes |             Yes |
| `thenAccept()` | Consumer  |                       Yes |              No |
| `thenRun()`    | Runnable  |                        No |              No |

### Memory Trick

```text
APPLY
→ Transform
→ Function

ACCEPT
→ Consume
→ Consumer

RUN
→ Just run
→ Runnable
```

---

# 18. Complete Pipeline

Example:

```java
CompletableFuture<Integer> future =
    CompletableFuture.supplyAsync(() -> 10);

future
    .thenApply(x -> x * 2)
    .thenAccept(x -> System.out.println(x))
    .thenRun(() -> System.out.println("Done"));
```

Output:

```text
20
Done
```

Flow:

```text
supplyAsync()
     ↓
10
     ↓
thenApply()
     ↓
20
     ↓
thenAccept()
     ↓
print 20
     ↓
thenRun()
     ↓
Done
```

### Interview Trap

`thenRun()` is NOT a Supplier.

It uses:

```java
Runnable
```

---

# 19. Why is CompletableFuture called an asynchronous pipeline?

Because we can chain multiple asynchronous stages:

```java
CompletableFuture
    .thenApply(...)
    .thenCompose(...)
    .thenAccept(...)
    .exceptionally(...)
```

Each stage can use the result/status of the previous stage.

Example:

```text
Async Task
   ↓
Transform
   ↓
Another Async Task
   ↓
Consume
   ↓
Handle Error
```

This is much more expressive than repeatedly calling `Future.get()`.

---

# 20. thenCombine()

## Q20. When do we use thenCombine()?

Use `thenCombine()` when we have **two independent CompletableFutures** and want to combine their results.

Example:

```java
CompletableFuture<User> userFuture =
    CompletableFuture.supplyAsync(() -> getUser());

CompletableFuture<List<Order>> ordersFuture =
    CompletableFuture.supplyAsync(() -> getOrders());
```

Then:

```java
CompletableFuture<UserOrders> result =
    userFuture.thenCombine(
        ordersFuture,
        (user, orders) ->
            new UserOrders(user, orders)
    );
```

Conceptually:

```text
Future<A> + Future<B>
        ↓
  thenCombine()
        ↓
     Future<C>
```

---

# 21. thenApply vs thenCombine

### thenApply()

Transforms ONE result.

```text
A
 ↓
thenApply()
 ↓
B
```

Example:

```java
userFuture.thenApply(
    user -> user.getName()
);
```

### thenCombine()

Combines TWO independent results.

```text
A ─────┐
       ├── thenCombine() → C
B ─────┘
```

Example:

```java
userFuture.thenCombine(
    ordersFuture,
    (user, orders) -> ...
);
```

---

# 22. thenCompose()

## Q22. Why do we need thenCompose()?

Use `thenCompose()` when the next asynchronous operation **depends on the result of the previous asynchronous operation**.

Example:

```text
Get Customer
     ↓
customerId
     ↓
Get Account
     ↓
accountId
     ↓
Get Balance
```

Each operation returns a `CompletableFuture`.

---

# 23. thenApply vs thenCompose — MOST IMPORTANT

Suppose:

```java
CompletableFuture<User> userFuture =
    CompletableFuture.supplyAsync(() -> getUser());
```

And:

```java
CompletableFuture<List<Order>> getOrders(String userId)
```

If we use:

```java
userFuture.thenApply(
    user -> getOrders(user.getId())
);
```

the result becomes:

```java
CompletableFuture<CompletableFuture<List<Order>>>
```

That's a nested Future.

### With thenCompose():

```java
userFuture.thenCompose(
    user -> getOrders(user.getId())
);
```

we get:

```java
CompletableFuture<List<Order>>
```

`thenCompose()` **flattens** the nested CompletableFuture.

---

# 24. Mental Model for thenCompose

```text
thenApply()

Future<A>
    ↓
Function<A, Future<B>>
    ↓
Future<Future<B>>   ❌
```

Whereas:

```text
thenCompose()

Future<A>
    ↓
Function<A, Future<B>>
    ↓
Future<B>           ✅
```

### Memory Trick

> `thenApply` = transform

> `thenCompose` = transform + flatten

---

# 25. Is thenCompose() non-blocking?

Do NOT explain `thenCompose()` as:

> "thenCompose doesn't block but thenApply blocks."

That's incorrect.

Both are composition mechanisms.

The real difference is:

```text
thenApply()
→ transformation

thenCompose()
→ dependent asynchronous operation
→ flatten nested future
```

---

# 26. Real Banking Example

Suppose:

```text
1. Get Customer
2. Get Account using customerId
3. Get Balance using accountId
```

Each operation returns:

```java
CompletableFuture<T>
```

Then:

```java
CompletableFuture<Balance> balanceFuture =
    getCustomer()
        .thenCompose(customer ->
            getAccount(customer.getId())
        )
        .thenCompose(account ->
            getBalance(account.getId())
        );
```

Flow:

```text
getCustomer()
     ↓
Customer
     ↓
thenCompose()
     ↓
getAccount(customerId)
     ↓
Account
     ↓
thenCompose()
     ↓
getBalance(accountId)
     ↓
Balance
```

### Interview Answer

> "I would use `thenCompose()` because these are dependent asynchronous operations. The output of one operation is required to start the next operation. `thenCompose()` also prevents nested CompletableFutures."

---

# 27. thenCombine vs thenCompose

This distinction is extremely important.

## thenCompose

Used for:

```text
Dependent operations
```

Example:

```text
Customer
   ↓
Account
   ↓
Balance
```

The second operation needs the first result.

---

## thenCombine

Used for:

```text
Independent operations
```

Example:

```text
Get User ──────┐
               ├── Combine
Get Orders ────┘
```

They can run independently.

### Easy Memory Trick

```text
DEPENDENT
→ thenCompose()

INDEPENDENT
→ thenCombine()
```

---

# 28. Common Interview Question

## Q: You have two APIs:

```text
getUser()
getOrders()
```

Both are independent.

Which method?

### Answer:

```java
thenCombine()
```

Because both operations can execute independently and their results can be combined afterward.

---

## Q: You have:

```text
getUser()
   ↓
getOrders(userId)
```

Which method?

### Answer:

```java
thenCompose()
```

Because the second operation depends on the first result.

---

# 29. Future vs CompletableFuture — Strong Interview Answer

If the interviewer asks:

> "Why would you use CompletableFuture instead of Future?"

Answer:

> "`Future` allows me to represent and retrieve the result of an asynchronous computation, but retrieving it using `get()` can block the calling thread. `CompletableFuture` provides a richer API for asynchronous composition, allowing me to transform results, chain dependent operations, combine independent operations, and handle exceptions without explicitly blocking for every intermediate result."

---

# 30. Common Interview Traps

## Trap 1

### Question:

Does Runnable create a thread?

### Wrong:

"Yes."

### Correct:

"Runnable represents the task. Thread executes the task."

---

## Trap 2

### Question:

What's the difference between `start()` and `run()`?

### Correct:

```text
start()
→ new thread

run()
→ normal method call
```

---

## Trap 3

### Question:

Can Runnable return a value?

### Correct:

No. `run()` returns `void`.

Use `Callable<T>` when you need a return value.

---

## Trap 4

### Question:

Can Callable throw checked exceptions?

### Correct:

Yes.

```java
T call() throws Exception;
```

---

## Trap 5

### Question:

Is Future asynchronous?

Better answer:

> "The task submitted through ExecutorService can execute asynchronously, but `Future.get()` is blocking."

---

## Trap 6

### Question:

Is CompletableFuture always non-blocking?

### Wrong:

"Yes."

### Correct:

> "CompletableFuture supports non-blocking composition, but `get()` and `join()` can block."

---

## Trap 7

### Question:

thenApply or thenCompose?

Ask yourself:

> "Does my next operation return another CompletableFuture?"

If yes, and the next operation depends on the previous result:

```text
thenCompose()
```

If you're simply transforming the value:

```text
thenApply()
```

---

## Trap 8

### Question:

thenCombine or thenCompose?

Ask:

> "Are the operations independent or dependent?"

```text
Independent → thenCombine()
Dependent   → thenCompose()
```

---

# 31. 30-Second Mental Revision

Before an interview, remember this:

```text
Runnable
→ task
→ no result

Callable<T>
→ task
→ returns T
→ checked exception possible

ExecutorService
→ manages thread pool
→ executes tasks

Future<T>
→ handle to async result
→ get() can block

CompletableFuture<T>
→ async result
→ composition
→ transformation
→ combination
→ exception handling
```

Then:

```text
supplyAsync()
→ returns value

runAsync()
→ no value

thenApply()
→ transform
→ Function

thenAccept()
→ consume
→ Consumer

thenRun()
→ execute action
→ Runnable

thenCompose()
→ dependent async operations
→ flatten

thenCombine()
→ independent async operations
→ combine
```

---

# 32. One-Line Memory Formula

```text
Runnable = DO
Callable = DO + RESULT

Future = RESULT LATER

CompletableFuture =
RESULT LATER
+ TRANSFORM
+ CHAIN
+ COMBINE
+ HANDLE ERRORS
```

And the most important:

```text
thenApply   → Transform
thenCompose → Dependent async chain
thenCombine → Independent async results
thenAccept  → Consume
thenRun     → Just run
```

---

# 33. Interview Scenario — Complete Example

Suppose you're designing a banking onboarding flow.

You need:

```text
1. Get Customer
2. Get Account
3. Get Fraud Score
4. Combine Account + Fraud Score
5. Save Final Decision
```

Assume:

```java
getCustomer()
    → CompletableFuture<Customer>

getAccount(customerId)
    → CompletableFuture<Account>

getFraudScore(customerId)
    → CompletableFuture<FraudScore>
```

Then:

```java
getCustomer()
    .thenCompose(customer ->
        getAccount(customer.getId())
            .thenCombine(
                getFraudScore(customer.getId()),
                (account, fraudScore) ->
                    createDecision(account, fraudScore)
            )
    )
    .thenAccept(decision ->
        saveDecision(decision)
    );
```

Why?

```text
getCustomer()
     ↓
Customer
     ↓
thenCompose()
     ↓
 ┌──────────────────────┐
 │                      │
getAccount()       getFraudScore()
 │                      │
 └──────────┬───────────┘
            ↓
      thenCombine()
            ↓
       Decision
            ↓
       thenAccept()
            ↓
       Save Decision
```

This combines all the major concepts:

```text
thenCompose()
→ dependency

thenCombine()
→ parallel/independent work

thenAccept()
→ consume final result
```

---

# 34. Final Interview Questions to Practice

Before your interview, make sure you can answer these without memorizing:

1. What is Runnable?
2. Runnable vs Thread?
3. `start()` vs `run()`?
4. Why prefer Runnable over extending Thread?
5. What is Callable?
6. Runnable vs Callable?
7. What is ExecutorService?
8. What is Future?
9. What happens when Future.get() is called?
10. Is Future.get() blocking?
11. Does submit() block?
12. What is CompletableFuture?
13. Future vs CompletableFuture?
14. `runAsync()` vs `supplyAsync()`?
15. What does `thenApply()` do?
16. What does `thenAccept()` do?
17. What does `thenRun()` do?
18. `thenApply()` vs `thenAccept()`?
19. `thenApply()` vs `thenCompose()`?
20. Why does thenCompose prevent nested CompletableFuture?
21. What is `thenCombine()`?
22. `thenCompose()` vs `thenCombine()`?
23. How do you execute two independent APIs in parallel?
24. How do you chain dependent APIs?
25. Is CompletableFuture always non-blocking?
26. Difference between `get()` and `join()`?
27. How do you handle exceptions in CompletableFuture?
28. What happens if an asynchronous stage throws an exception?
29. How would you combine three independent API calls?
30. How would you design a CompletableFuture-based banking workflow?

---

# Final Cheat Sheet

```text
                     JAVA CONCURRENCY

                         TASK
                          |
             ┌────────────┴────────────┐
             ↓                         ↓
         Runnable                   Callable<T>
         no result                  returns T
             |                         |
             └──────────┬──────────────┘
                        ↓
                 ExecutorService
                        |
                      submit()
                        ↓
                     Future
                        |
                      get()
                        ↓
                     BLOCKING


                 CompletableFuture
                        |
        ┌───────────────┼────────────────┐
        ↓               ↓                ↓
   thenApply()    thenCompose()    thenCombine()
   transform      dependent         independent
        |          async chain       results
        ↓               ↓                ↓
    Function          Future A+B       A + B
        |
        ↓
   thenAccept()
    Consumer

   thenRun()
    Runnable
```

## The 3 questions to ask yourself

### 1. "I only need to execute something?"

→ `Runnable`

### 2. "I need a result?"

→ `Callable` / `supplyAsync`

### 3. "I am chaining CompletableFutures?"

Ask:

```text
Just transform result?
→ thenApply()

Next async operation depends on result?
→ thenCompose()

Two independent async results?
→ thenCombine()

I just want to consume result?
→ thenAccept()

I just want to execute something afterward?
→ thenRun()
```

This is the mental model you should carry into the interview rather than memorizing individual definitions.
