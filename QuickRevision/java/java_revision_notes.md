# Java Revision Notes — Exceptions, Concurrency, and Stream API

Simple notes for quick revision and Java interview preparation.

## Contents

1. [Java Exception Hierarchy](#1-java-exception-hierarchy)
2. [Runnable vs Callable vs Supplier](#2-runnable-vs-callable-vs-supplier)
3. [ExecutorService and `submit()`](#3-executorservice-and-submit)
4. [Future](#4-future)
5. [FutureTask](#5-futuretask)
6. [CompletableFuture](#6-completablefuture)
7. [How All Concurrency Concepts Fit Together](#7-how-all-concurrency-concepts-fit-together)
8. [Java Stream API Basics](#8-java-stream-api-basics)
9. [12 Common Stream API Interview Problems](#9-12-common-stream-api-interview-problems)
10. [Stream API Follow-up Problems](#10-stream-api-follow-up-problems)
11. [Important Stream Methods](#11-important-stream-methods)
12. [Quick Interview Revision](#12-quick-interview-revision)

---

# 1. Java Exception Hierarchy

## What is an exception?

An **exception** is a problem that interrupts the normal flow of a program.

```java
int result = 10 / 0; // ArithmeticException
```

## Hierarchy

```text
Object
└── Throwable
    ├── Error
    │   ├── OutOfMemoryError
    │   ├── StackOverflowError
    │   └── AssertionError
    │
    └── Exception
        ├── RuntimeException  (unchecked)
        │   ├── NullPointerException
        │   ├── ArithmeticException
        │   ├── NumberFormatException
        │   └── ArrayIndexOutOfBoundsException
        │
        └── Other exceptions (commonly checked)
            ├── IOException
            ├── SQLException
            ├── ClassNotFoundException
            └── InterruptedException
```

## Main classes

- **`Throwable`**: Root class for Java objects that can be thrown and caught.
- **`Error`**: Usually indicates a serious JVM or system problem. Application code generally does not try to recover from it.
- **`Exception`**: Represents conditions an application may be able to handle.
- **`RuntimeException`**: A subclass of `Exception`; its subclasses are unchecked exceptions.

## Checked vs unchecked exceptions

### Checked exception

The compiler requires you to catch it or declare it with `throws`.

```java
FileReader reader = new FileReader("data.txt");
```

This can throw `FileNotFoundException`, which is checked.

### Unchecked exception

The compiler does not require you to catch it or declare it.

```java
int result = 10 / 0; // ArithmeticException
```

**Memory trick:**
- **Error** = serious system/JVM problem.
- **Checked** = compiler checks whether you handle or declare it.
- **Unchecked** = compiler does not force handling.

**Interview answer:** `Throwable` is the root of Java's exception hierarchy. It has two major subclasses: `Error` and `Exception`. `RuntimeException` and its subclasses are unchecked exceptions; many other exception classes are checked.

---

# 2. Runnable vs Callable vs Supplier

These are interfaces used to represent pieces of work or operations. They differ in how a result is returned and how checked exceptions are handled.

## Comparison

| Feature | `Runnable` | `Callable<T>` | `Supplier<T>` |
|---|---|---|---|
| Package | `java.lang` | `java.util.concurrent` | `java.util.function` |
| Main method | `run()` | `call()` | `get()` |
| Returns a value? | No (`void`) | Yes, type `T` | Yes, type `T` |
| Can declare checked exceptions? | No | Yes | No |
| Accepted directly by `new Thread(...)`? | Yes | No | No |
| Common `CompletableFuture` method | `runAsync()` | Not directly | `supplyAsync()` |

## Runnable: do work without a result

```java
Runnable task = () -> {
    System.out.println("Downloading file...");
};

new Thread(task).start();
```

Think:

```text
Runnable -> run() -> void -> no task result
```

## Callable: do work and return a result

```java
Callable<String> task = () -> "Hello";
Callable<Integer> sumTask = () -> 10 + 20;
```

Its method has this general shape:

```java
V call() throws Exception;
```

- `V` is the type of the returned value.
- `Callable<String>` returns a `String`.
- `Callable<Integer>` returns an `Integer`.
- `call()` may throw a checked exception.

This line only defines the task; it does not run it yet:

```java
Callable<String> c = () -> readFile();
```

Calling `c.call()` directly executes the work on the current thread. Submitting it to an executor allows an executor worker thread to perform it.

## Supplier: provide a result

```java
Supplier<String> supplier = () -> "Hello";
String value = supplier.get();
```

A Supplier returns a value but does not declare checked exceptions in its `get()` method.

**Memory trick:**
- **Runnable** = run work.
- **Callable** = call work and return a result; checked exceptions are allowed.
- **Supplier** = supply a result.

---

# 3. ExecutorService and `submit()`

## Why use ExecutorService?

`ExecutorService` runs tasks and manages threads, often using a reusable pool of worker threads. It saves you from manually creating a new thread for every task.

```java
ExecutorService executor = Executors.newFixedThreadPool(3);
```

Conceptually:

```text
ExecutorService
├── Worker thread 1
├── Worker thread 2
└── Worker thread 3
```

**Memory trick:** `ExecutorService` = task/thread manager.

## The two `submit()` overloads

```java
Future<?> submit(Runnable task);
<T> Future<T> submit(Callable<T> task);
```

These are **overloaded methods**: they share the name `submit`, but accept different parameter types.

### A. `submit(Runnable)`

Use it when you do not need a useful return value from the task.

```java
Future<?> future = executor.submit(() -> {
    System.out.println("Working...");
});
```

The returned `Future<?>` can still be used to check completion, cancel the task, or wait for completion. For a Runnable submitted this way, `future.get()` returns `null` after successful completion.

### B. `submit(Callable<T>)`

Use it when the task returns a value.

```java
Callable<String> task = () -> "Hello";
Future<String> future = executor.submit(task);

String result = future.get();
System.out.println(result); // Hello
```

### What does `<T>` mean?

`T` represents the result type:

```text
Callable<String>   -> Future<String>
Callable<Integer>  -> Future<Integer>
Callable<Employee> -> Future<Employee>
```

**Memory trick:**

```text
submit(Runnable)    -> Future<?> -> no useful task result
submit(Callable<T>) -> Future<T> -> result has type T
```

---

# 4. Future

A **Future** is a handle representing the result of a task that may finish later. It can be used to retrieve the result, check status, or request cancellation.

```java
Future<Integer> future = executor.submit(() -> 10 + 20);
Integer result = future.get();
System.out.println(result); // 30
```

## Important Future methods

| Method | Meaning |
|---|---|
| `get()` | Wait for completion if needed, then return the result |
| `get(timeout, unit)` | Wait up to a specified time |
| `isDone()` | Check whether the task has completed |
| `cancel(true)` | Request cancellation and, if running, interruption |
| `isCancelled()` | Check whether cancellation succeeded |

`get()` is **blocking**: the calling thread waits if the result is not ready. The timed `get()` can throw `TimeoutException`; `get()` may also throw `InterruptedException` or `ExecutionException`. A cancelled future's `get()` throws `CancellationException`.

## How the worker's result reaches the main thread

```text
MAIN THREAD
    |
    | executor.submit(task)
    v
EXECUTOR SERVICE
    |
    v
WORKER THREAD runs the Callable
    |
    | returns a String
    v
Future<String> stores/represents completion and result
    |
    | future.get()
    v
MAIN THREAD receives the String
```

### Full example

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> task = () -> {
            Thread.sleep(1000);
            return "File content";
        };

        Future<String> future = executor.submit(task);
        System.out.println("Main thread can do other work...");

        String result = future.get(); // waits if task is not finished
        System.out.println(result);

        executor.shutdown();
    }
}
```

**Memory trick:** `Future` = result later.

---

# 5. FutureTask

## Why is FutureTask needed?

A `Thread` constructor accepts a `Runnable`, not a `Callable`. Therefore, this does not compile:

```java
Callable<String> c = () -> "Hello";
new Thread(c); // Compile error: Callable is not Runnable
```

`FutureTask<T>` can wrap a Callable. It implements `RunnableFuture<T>`, which combines `Runnable` and `Future<T>`.

```text
Callable<T>
    |
    v
FutureTask<T>
    ├── Runnable -> can be passed to Thread
    └── Future<T> -> can retrieve the result
```

## Example

```java
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Callable<String> c = () -> "Hello";

        FutureTask<String> task = new FutureTask<>(c);
        Thread thread = new Thread(task);
        thread.start();

        String result = task.get();
        System.out.println(result); // Hello
    }
}
```

**When to use it:** Use `FutureTask` if you specifically want to run a Callable with a manually created Thread. If you already have an `ExecutorService`, `executor.submit(callable)` is usually simpler.

**Memory trick:** FutureTask = Callable-to-Thread bridge + result handle.

---

# 6. CompletableFuture

## Why do we need CompletableFuture?

A basic `Future` can provide a result, but retrieving it with `get()` blocks the calling thread while it waits. `CompletableFuture` offers a richer asynchronous model: you can run work and chain follow-up operations to process the result.

**Memory trick:** CompletableFuture = asynchronous work + result-processing chain.

## `runAsync()` — no task result

```java
CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
    System.out.println("Hello");
});
```

- Accepts a `Runnable`.
- The task does not return a useful value.
- Returns `CompletableFuture<Void>` to represent completion.

## `supplyAsync()` — produces a result

```java
CompletableFuture<Integer> future =
        CompletableFuture.supplyAsync(() -> 10 + 20);

Integer result = future.join();
System.out.println(result); // 30
```

- Accepts a `Supplier<T>`.
- Produces a result of type `T`.
- `join()` waits for the result if necessary. If the operation fails, `join()` typically reports the failure using an unchecked `CompletionException`.

| Feature | `runAsync()` | `supplyAsync()` |
|---|---|---|
| Accepts | `Runnable` | `Supplier<T>` |
| Task result | No | Yes |
| Return type | `CompletableFuture<Void>` | `CompletableFuture<T>` |
| Example | Print a message | Calculate a sum |

## Why Callable cannot be passed directly

```java
Callable<String> c = () -> "Hello";

CompletableFuture<String> future =
        CompletableFuture.supplyAsync(c); // Compile error
```

`supplyAsync()` expects a `Supplier`, not a `Callable`. Wrap the Callable in a Supplier-compatible lambda:

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    try {
        return c.call();
    } catch (Exception e) {
        throw new CompletionException(e);
    }
});
```

The lambda calls the Callable and wraps a checked exception in `CompletionException`.

## Chaining operations

```java
CompletableFuture
        .supplyAsync(() -> "hello")
        .thenApply(String::toUpperCase)
        .thenAccept(System.out::println);
```

Output:

```text
HELLO
```

| Method | Simple meaning |
|---|---|
| `runAsync()` | Run a task without a result |
| `supplyAsync()` | Run a task and produce a result |
| `thenApply()` | Transform the result |
| `thenAccept()` | Use the result without returning another value |
| `thenRun()` | Run another action after completion, without using the result |
| `thenCompose()` | Chain another asynchronous operation |
| `exceptionally()` | Provide a recovery value after failure |
| `join()` | Wait for and retrieve the result |

Without a custom executor, static `runAsync()` and `supplyAsync()` normally use the common ForkJoinPool.

**Memory trick:** Run = no result; Supply = result; Apply = transform; Accept = consume.

---

# 7. How All Concurrency Concepts Fit Together

```text
                         TASK
                          |
              ┌───────────┴───────────┐
              v                       v
          Runnable                 Callable<T>
              |                       |
              v                       v
     ExecutorService.submit()  ExecutorService.submit()
              |                       |
              v                       v
          Future<?>                Future<T>
                                      |
                                  future.get()
```

For a manually created Thread:

```text
Callable<T> -> FutureTask<T> -> new Thread(task).start()
                          └-> task.get() returns the result
```

For CompletableFuture:

```text
Runnable    -> CompletableFuture.runAsync()       -> no task result
Supplier<T> -> CompletableFuture.supplyAsync()    -> result of type T
```

## Which one should I choose?

| Need | Use |
|---|---|
| Manage/reuse worker threads | `ExecutorService` |
| Submit work with no useful result | `submit(Runnable)` |
| Submit work with a result | `submit(Callable<T>)` |
| Retrieve a basic asynchronous result | `Future<T>` |
| Run a Callable using `new Thread(...)` | `FutureTask<T>` |
| Chain asynchronous result operations | `CompletableFuture<T>` |
| Async work without a result | `runAsync()` |
| Async work that produces a result | `supplyAsync()` |

## Other Callable interview points

### Direct `call()` vs `submit()`

```java
String result = task.call();             // runs on the current thread
Future<String> f = executor.submit(task); // executor runs the task
```

### `invokeAll()` vs `invokeAny()`

- `invokeAll()` submits a collection of tasks and waits for all tasks to finish (or for its timeout). It returns a list of Futures in input order.
- `invokeAny()` returns the result from the first task that completes successfully; if all tasks fail, it throws `ExecutionException`.

### Exceptions from Callable

```text
Callable.call() throws an exception
               |
               v
Executor records the failure in the Future
               |
               v
future.get() throws ExecutionException
               |
               v
e.getCause() gives the original exception
```

`InterruptedException` means the thread waiting for the result was interrupted. Restore the interrupt flag when handling it unless you are deliberately propagating it:

```java
catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

Remember to shut down an ExecutorService when it is no longer needed:

```java
executor.shutdown();
```

---

# 8. Java Stream API Basics

A **Stream** lets you process data from a collection using operations such as filtering, transforming, sorting, grouping, and collecting. It does not usually store the data itself; it processes values from a source.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

List<Integer> result = numbers.stream()
        .filter(n -> n > 2)
        .collect(Collectors.toList());

System.out.println(result); // [3, 4, 5]
```

Typical flow:

```text
Source -> Intermediate operations -> Terminal operation
List   -> filter(), map(), sorted() -> collect(), count()
```

- **Intermediate operations** return another Stream; examples: `filter()`, `map()`, `distinct()`, `sorted()`.
- **Terminal operations** produce a result or side effect and finish the stream; examples: `collect()`, `count()`, `forEach()`, `findFirst()`.

The examples below commonly need these imports:

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
```

---

# 9. 12 Common Stream API Interview Problems

## 1. Find the first non-repeated character

For `"programming"`, the result is `p`.

```java
String str = "programming";

Character result = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

System.out.println(result); // p
```

**Pattern:** Preserve character order, count occurrences, find the first count equal to `1`.

## 2. Print duplicate characters

For `"programming"`, the duplicate characters are `r`, `g`, and `m`.

```java
String str = "programming";

str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()))
        .entrySet()
        .stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .forEach(System.out::println);
```

**Pattern:** `groupingBy()` + `counting()` + count `> 1`.

## 3. Count the frequency of every word

```java
String sentence = "java is easy java is powerful";

Map<String, Long> map = Arrays.stream(sentence.split("\\s+"))
        .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()));

System.out.println(map);
```

Counts: `java=2`, `is=2`, `easy=1`, `powerful=1`. A normal HashMap-based result does not guarantee display order.

**Pattern:** Split into words, group identical words, count each group.

## 4. Find the second-highest distinct number

```java
List<Integer> list = Arrays.asList(10, 40, 30, 60, 90, 80);

Integer secondHighest = list.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .skip(1)
        .findFirst()
        .orElse(null);

System.out.println(secondHighest); // 80
```

**Pattern:** `distinct()` -> reverse sort -> `skip(1)` -> `findFirst()`.

## 5. Find the longest string

```java
List<String> list = Arrays.asList(
        "Java", "SpringBoot", "Microservices", "REST");

String longest = list.stream()
        .max(Comparator.comparing(String::length))
        .orElse("");

System.out.println(longest); // Microservices
```

**Pattern:** `max()` using string length.

## 6. Find the highest-paid employee in each department

Assume `Employee` has `getDept()` and `getSalary()` methods.

```java
Map<String, Optional<Employee>> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDept,
                Collectors.maxBy(
                        Comparator.comparing(Employee::getSalary))));
```

Each department maps to an `Optional<Employee>` containing the highest-paid employee, if one exists.

**Pattern:** `groupingBy(department)` + `maxBy(salary)`.

## 7. Flatten a nested list

```java
List<List<Integer>> list = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3, 4),
        Arrays.asList(5, 6));

List<Integer> result = list.stream()
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

System.out.println(result); // [1, 2, 3, 4, 5, 6]
```

**Pattern:** `flatMap()` flattens nested collections into one stream.

## 8. Find duplicate integers

```java
List<Integer> list = Arrays.asList(1, 2, 3, 2, 4, 5, 1);
Set<Integer> seen = new HashSet<>();

list.stream()
        .filter(n -> !seen.add(n))
        .distinct()
        .forEach(System.out::println);
```

Prints `2` and `1` (the order is not guaranteed by a `HashSet`, although the stream traverses the original list in order here). `Set.add()` returns `true` for a new value and `false` when the value already exists.

**Note:** This pattern uses mutable state. It is fine for a sequential stream, but should not be used this way with a parallel stream.

## 9. Remove duplicate words and maintain order

```java
String sentence = "java java spring java boot spring";

String result = Arrays.stream(sentence.split("\\s+"))
        .distinct()
        .collect(Collectors.joining(" "));

System.out.println(result); // java spring boot
```

**Pattern:** `distinct()` + `joining(" ")`.

## 10. Group strings by length

```java
List<String> list = Arrays.asList("Java", "REST", "API", "SpringBoot");

Map<Integer, List<String>> map = list.stream()
        .collect(Collectors.groupingBy(String::length));

System.out.println(map);
```

Conceptually:

```text
3  -> [API]
4  -> [Java, REST]
10 -> [SpringBoot]
```

**Pattern:** `groupingBy(String::length)`.

## 11. Find the top three highest distinct numbers

```java
List<Integer> list = Arrays.asList(10, 90, 20, 60, 50, 70, 90);

List<Integer> result = list.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .limit(3)
        .collect(Collectors.toList());

System.out.println(result); // [90, 70, 60]
```

**Pattern:** `distinct()` -> descending sort -> `limit(3)`.

## 12. Check whether two strings are anagrams

Anagrams contain the same characters in a different order; for example, `listen` and `silent`.

```java
String s1 = "listen";
String s2 = "silent";

boolean result = s1.length() == s2.length()
        && s1.chars().sorted().boxed().collect(Collectors.toList())
        .equals(s2.chars().sorted().boxed().collect(Collectors.toList()));

System.out.println(result); // true
```

This simple version is case-sensitive and treats spaces as characters.

**Pattern:** Sort both sets of characters and compare them.

---

# 10. Stream API Follow-up Problems

## 1. Find the first repeated character

```java
String str = "programming";
Set<Character> seen = new HashSet<>();

Character result = str.chars()
        .mapToObj(c -> (char) c)
        .filter(ch -> !seen.add(ch))
        .findFirst()
        .orElse(null);

System.out.println(result); // r
```

**Difference:** First non-repeated means count is `1`; first repeated means the character has already been seen.

## 2. Count each character's frequency

```java
String str = "banana";

Map<Character, Long> map = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()));

System.out.println(map); // counts: b=1, a=3, n=2
```

The exact order printed depends on the map implementation.

## 3. Remove duplicate characters while maintaining order

```java
String str = "programming";

String result = str.chars()
        .mapToObj(c -> String.valueOf((char) c))
        .distinct()
        .collect(Collectors.joining());

System.out.println(result); // progamin
```

## 4. Reverse every word

```java
String sentence = "Java Stream API";

String result = Arrays.stream(sentence.split("\\s+"))
        .map(word -> new StringBuilder(word).reverse().toString())
        .collect(Collectors.joining(" "));

System.out.println(result); // avaJ maertS IPA
```

## 5. Find palindrome strings

A palindrome reads the same forwards and backwards.

```java
List<String> list = Arrays.asList("madam", "java", "level", "spring");

list.stream()
        .filter(s -> s.equals(new StringBuilder(s).reverse().toString()))
        .forEach(System.out::println);
```

Output:

```text
madam
level
```

---

# 11. Important Stream Methods

| Method | Simple meaning | Typical use |
|---|---|---|
| `filter()` | Keep matching values | Select even numbers |
| `map()` | Transform each value | Convert words to uppercase |
| `flatMap()` | Flatten nested streams | Flatten a list of lists |
| `distinct()` | Remove duplicates | Unique words or numbers |
| `sorted()` | Sort values | Ascending/descending order |
| `limit(n)` | Keep the first `n` values | Top N values after sorting |
| `skip(n)` | Skip the first `n` values | Second-highest after sorting |
| `groupingBy()` | Group by a key | Group employees by department |
| `partitioningBy()` | Split into `true`/`false` groups | Separate passing and failing scores |
| `collect()` | Gather values into a result | Make a List or Map |
| `reduce()` | Combine values into one result | Sum or product |
| `max()` / `min()` | Find largest/smallest | Highest salary or longest string |
| `findFirst()` | Get the first matching element | First matching character |
| `findAny()` | Get any matching element | Useful in some parallel-stream operations |
| `joining()` | Join strings | Make a sentence from words |
| `counting()` | Count values in a group | Word or character frequencies |
| `forEach()` | Perform an action per element | Print or process results |

## Three useful patterns to memorize

### Pattern 1: Frequency counting

```java
.collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()))
```

Use for character frequency, word frequency, and identifying duplicates.

### Pattern 2: Maximum by a property

```java
.max(Comparator.comparing(Employee::getSalary))
```

Use for highest salary, longest string, or any maximum based on a property.

### Pattern 3: Top N values

```java
.distinct()
.sorted(Comparator.reverseOrder())
.limit(3)
```

Use for top three, top five, or similar ranking problems.

---

# 12. Quick Interview Revision

## Concurrency memory sheet

1. `Throwable` is the root of Java's exception hierarchy; major branches are `Error` and `Exception`.
2. `RuntimeException` and its subclasses are unchecked exceptions.
3. `Runnable.run()` returns nothing.
4. `Callable.call()` returns a value and can declare checked exceptions.
5. `Supplier.get()` provides a value but does not declare checked exceptions.
6. `ExecutorService` runs tasks and manages worker threads.
7. `submit(Runnable)` returns `Future<?>`; `submit(Callable<T>)` returns `Future<T>`.
8. `Future.get()` waits for and retrieves a result.
9. `FutureTask` wraps a Callable so it can be passed to a Thread; it is also a Future.
10. `CompletableFuture` supports asynchronous operations and chaining.
11. `runAsync()` accepts a Runnable; `supplyAsync()` accepts a Supplier.
12. `thenApply()` transforms a result; `thenAccept()` consumes it; `thenRun()` runs an action after completion.
13. `invokeAll()` waits for all tasks; `invokeAny()` returns the first successful result.
14. Use `executor.shutdown()` when the executor is no longer needed.

## Stream API memory sheet

1. First non-repeated character: count characters and select count `1`, preserving order.
2. Duplicate characters: frequency count greater than `1`.
3. Word frequency: split words + `groupingBy()` + `counting()`.
4. Second-highest distinct number: `distinct()` + reverse sort + `skip(1)` + `findFirst()`.
5. Longest string: `max(Comparator.comparing(String::length))`.
6. Highest-paid by department: `groupingBy()` + `maxBy()`.
7. Flatten nested list: `flatMap()`.
8. Find duplicate values: a Set can track previously seen values.
9. Remove duplicate words in order: `distinct()` + `joining()`.
10. Group strings by length: `groupingBy(String::length)`.
11. Top three distinct numbers: `distinct()` + reverse sort + `limit(3)`.
12. Anagrams: sort characters in both strings and compare.
13. Reverse every word: `map()` + `StringBuilder.reverse()` + `joining()`.
14. Palindromes: compare a string to its reverse.

## Final memory tricks

```text
ExecutorService   = RUN / manage tasks
Future            = RESULT LATER
FutureTask        = BRIDGE between Callable and Thread
CompletableFuture = ASYNC + CHAIN operations

Runnable          = run()   -> no task result
Callable          = call()  -> result + checked-exception support
Supplier          = get()   -> supplies a result

Stream patterns:
COUNT     -> groupingBy() + counting()
MAXIMUM   -> max(Comparator...)
TOP N     -> distinct() + sorted() + limit(N)
FLATTEN   -> flatMap()
UNIQUE    -> distinct()
```

**Revision tip:** First learn the one-line memory tricks. Then write one example each for Callable + Future, FutureTask + Thread, CompletableFuture chaining, frequency counting, top N numbers, and `flatMap()` without looking at these notes.
