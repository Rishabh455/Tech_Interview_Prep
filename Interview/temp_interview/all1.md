Yes. I’ve treated this as a **25-minute Nomura Java Backend rapid-revision sheet**. The transcript supports the same style: the interviewer did not stop at definitions; for example, after interfaces they immediately went into **multiple-interface/default-method conflicts**, and after Observable they asked for **Observable vs Promise**. Pasted text Pasted text Pasted text

The rule for your answers:

> **Definition → one technical detail → one example → stop.**

Do not give 2-minute textbook answers unless they cross-question.

---

# NOMURA JAVA BACKEND — 25-MINUTE REVISION

## PART 1 — MULTITHREADING

### 4. Explain multithreading in Java.

### Interview answer

> “Multithreading is the ability of a Java application to execute multiple threads concurrently within the same process.
>
> A thread is an independent path of execution, and multiple threads can share the same heap memory while each thread has its own stack.
>
> Multithreading is useful for improving responsiveness and throughput, especially for I/O-bound or concurrent backend operations.
>
> In backend applications, instead of creating and managing many threads manually, I prefer using `ExecutorService` or thread pools because they provide controlled thread management.”

### Trap

Don't say:

> “Multithreading means multiple processes running simultaneously.”

Wrong.

**Process ≠ thread.**

---

### 5. What are the ways to define multithreading in Java?

### Best answer

> “There are several ways to create or execute concurrent tasks in Java.
>
> The traditional approaches are extending `Thread` and implementing `Runnable`.
>
> When a task needs to return a result, `Callable` can be used together with `Future`.
>
> In real backend applications, I prefer `ExecutorService` with a thread pool because it separates task submission from thread management and avoids creating unlimited threads manually.”

### Remember

```java
class MyThread extends Thread {
    public void run() {
        System.out.println("Task");
    }
}
```

```java
class MyTask implements Runnable {
    public void run() {
        System.out.println("Task");
    }
}
```

```java
Callable<Integer> task = () -> 10;
```

```java
ExecutorService executor =
        Executors.newFixedThreadPool(5);

executor.submit(task);
```

### Follow-up: “Runnable vs Callable?”

> “Runnable does not return a result and cannot throw checked exceptions directly. Callable returns a value through Future and can throw checked exceptions.”

### Follow-up: “Which one do you prefer?”

> “ExecutorService with Runnable or Callable, depending on whether I need a result.”

### Project connection

For your onboarding project:

> “For independent background processing, I would prefer controlled execution through a thread pool rather than creating a new thread for every request.”

Do **not** claim you used multithreading in onboarding unless you can explain the exact implementation.

---

# PART 2 — OOP

## 6. Difference between overloading and overriding?

First, say **method**, not function.

### Interview answer

> “Method overloading means defining multiple methods with the same name but different parameter lists in the same class. It is resolved at compile time.
>
> Method overriding means a child class provides its own implementation of a method inherited from the parent class. It is resolved at runtime through dynamic method dispatch.”

### Example

```java
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }
}
```

Overriding:

```java
class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Bark");
    }
}
```

### Trap — very important

**Can return type alone overload a method?**

> “No. Changing only the return type does not create a valid overload.”

**Can static methods be overridden?**

> “Static methods are hidden, not overridden.”

**Can final methods be overridden?**

> “No.”

**Can private methods be overridden?**

> “No, because they are not inherited by the subclass.”

---

# 7. How do you achieve abstraction in Java?

### Answer

> “Abstraction means exposing essential behavior while hiding implementation details.
>
> In Java, abstraction is primarily achieved using abstract classes and interfaces.
>
> An abstract class can contain both abstract and concrete methods, while an interface defines a contract and, in modern Java, can also contain default, static and private methods.”

### Simple example

```java
abstract class Payment {
    abstract void pay();

    void validate() {
        System.out.println("Validation");
    }
}
```

```java
interface PaymentService {
    void pay();
}
```

### Project connection

> “In a backend application, I can define an interface such as `DocumentProcessor` and have different implementations for different document-processing providers. The onboarding service depends on the abstraction rather than the concrete implementation.”

That's a good **microservices/backend answer**.

---

# 8. Interface vs abstract class

### Memorize this table

| Interface | Abstract class |
|---|---|
| Contract-oriented | Common base behavior/state |
| Class can implement multiple interfaces | Class can extend only one class |
| No normal instance state | Can have instance fields/state |
| No constructor for object creation | Can have constructors |
| Supports abstract/default/static/private methods | Can have abstract + concrete methods |
| Best for capability/contract | Best for shared implementation |

### Spoken answer

> “I use an interface when I want to define a contract or capability and potentially allow a class to implement multiple contracts.
>
> I use an abstract class when related classes need to share common state or implementation.
>
> The biggest practical difference is that Java allows a class to implement multiple interfaces but extend only one class.”

---

# PART 3 — INHERITANCE

## 9. Which inheritance does Java not support and why?

### Answer

> “Java does not support multiple inheritance of classes, meaning a class cannot extend two classes.
>
> The primary reason is ambiguity, especially the diamond problem, where the same method could be inherited through multiple paths.”

### Example

```text
       A
      / \
     B   C
      \ /
       D
```

If B and C both inherit or override the same method from A, D could face ambiguity about which implementation to use.

---

# 10. Why?

### Answer

> “Because allowing multiple class inheritance can create ambiguity in inherited state and behavior. Java avoids that complexity by allowing single class inheritance while supporting multiple interfaces.”

---

# 11–12. What exactly is the diamond problem?

### Strong answer

> “The diamond problem occurs when a class inherits from two classes that both ultimately derive from the same parent, creating two possible paths to the same method or state.
>
> If both paths provide the same method, the child class may not know which implementation to use.
>
> Java avoids this by not allowing multiple inheritance of classes.”

### Important Java 8 follow-up

The interviewer may say:

**“But interfaces allow multiple inheritance, so doesn't the same problem happen?”**

Answer:

> “It can happen with conflicting default methods, but Java requires the implementing class to resolve the conflict explicitly by overriding the method. It can also explicitly invoke an interface's default implementation using `InterfaceName.super.method()`.”

The supplied transcript actually went into exactly this scenario: one class implementing two interfaces with the same default method. Pasted text

### Example

```java
interface A {
    default void show() {
        System.out.println("A");
    }
}

interface B {
    default void show() {
        System.out.println("B");
    }
}

class C implements A, B {
    @Override
    public void show() {
        A.super.show();
    }
}
```

### Trap

Don't say:

> “Java doesn't support multiple inheritance at all.”

Correct:

> **Java doesn't support multiple inheritance of classes. It supports multiple inheritance of type through interfaces.**

---

# PART 4 — REST APIs

## 13. You've used REST APIs, right?

### Answer for your project

> “Yes. In my onboarding project, REST APIs were the primary communication mechanism between the frontend and backend and also for synchronous communication between backend services.
>
> We used resource-oriented endpoints, HTTP methods, request/response DTOs, validation and appropriate HTTP status codes.
>
> The onboarding APIs were responsible for initiating applications, validating data, performing workflow operations and returning the current application state.”

The transcript also shows the interviewer explicitly checked frontend → backend REST communication and API failure handling. Pasted text

---

# 14. What are idempotent methods in REST?

### This is a MUST-KNOW Nomura question.

### Answer

> “An HTTP method is idempotent if making the same request multiple times has the same intended effect on the server as making it once.
>
> GET, PUT and DELETE are generally idempotent. POST is generally not idempotent because repeating it may create multiple resources.
>
> Idempotency is especially important in banking and onboarding systems because clients may retry requests due to network failures, and we don't want duplicate business operations.”

### Example

```http
PUT /applications/123
```

Setting application status to:

```text
APPROVED
```

multiple times should still leave it APPROVED.

### Important trap

Don't say:

> “Idempotent means the response is always exactly the same.”

Wrong.

It refers to the **intended server-side effect**, not necessarily identical HTTP responses.

### Project cross-question

**“How did you implement idempotency in onboarding?”**

> “We use an idempotency key and persist the operation state, along with database-level uniqueness and atomic state transitions, so retries don't create duplicate business effects.”

That's a very strong banking answer.

---

# 15. Query parameter vs Path variable

### Answer

> “A path variable identifies a specific resource and is part of the URL path.
>
> A query parameter is generally used for filtering, searching, sorting or optional parameters.”

### Example

```http
GET /applications/123
```

`123` is a path variable because it identifies the application.

```http
GET /applications?status=PENDING&page=1
```

`status` and `page` are query parameters because they filter or control the result.

### Memory trick

**Path = WHICH resource**

**Query = HOW I want the data**

### Trap

Don't say query parameters are always optional. They are **commonly optional**, but an API may require them.

---

# PART 5 — STREAM API

## 16. Difference between map and flatMap?

### Best answer

> “`map` transforms each element into exactly one result, so the number of stream elements generally remains the same.
>
> `flatMap` is used when each element produces another stream or collection, and we want to flatten the nested structure into a single stream.”

### Example

```java
List<String> names = List.of("Ravi", "Raj");

names.stream()
     .map(String::length)
     .forEach(System.out::println);
```

Result:

```text
4
3
```

`map`:

```text
String → Integer
```

### flatMap

```java
List<List<Integer>> numbers =
        List.of(
            List.of(1, 2),
            List.of(3, 4)
        );

numbers.stream()
       .flatMap(List::stream)
       .forEach(System.out::println);
```

Output:

```text
1
2
3
4
```

### Memory

```text
map:
[A, B, C]
 ↓
[X, Y, Z]

flatMap:
[[A,B], [C,D]]
 ↓
[A,B,C,D]
```

---

# 17–18. They may ask you to write flatMap

Write exactly this:

```java
List<List<Integer>> list = Arrays.asList(
        Arrays.asList(1, 2),
        Arrays.asList(3, 4)
);

List<Integer> result = list.stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());

System.out.println(result);
```

Output:

```text
[1, 2, 3, 4]
```

### If interviewer says: “Do it without method reference.”

```java
List<Integer> result = list.stream()
        .flatMap(innerList -> innerList.stream())
        .collect(Collectors.toList());
```

### Follow-up: “What will map produce?”

```java
list.stream()
    .map(x -> x)
```

Essentially:

```text
Stream<List<Integer>>
```

while `flatMap` gives:

```text
Stream<Integer>
```

### Trap

Do **not** say:

> “flatMap is faster than map.”

That is not the definition.

---

# 19. Observable vs Promise

This exact area was tested in the supplied interview transcript. The interviewer asked about Observable and then immediately compared it with Promise. Pasted text

### Answer

> “An Observable represents a stream of asynchronous values and can emit multiple values over time, while a Promise represents a single eventual result.
>
> Observables are lazy and support RxJS operators such as `map`, `filter` and `switchMap`. They can also be unsubscribed from.
>
> A Promise settles once, either successfully or with an error, and doesn't provide the same stream-processing model.”

### Angular example

> “In Angular, `HttpClient` returns Observables, so we can compose API calls using RxJS operators and subscribe to the result.”

The transcript specifically says Angular `HttpClient` and operators such as `map` and `switchMap` were used for API calls. Pasted text

### Important trap

For Angular HTTP:

> “Observable can emit multiple values.”

Technically true for Observables in general, but an individual Angular `HttpClient` request normally emits one response and completes.

So if they challenge you:

> “Observable supports multiple emissions in general; Angular HTTP Observables are typically one response followed by completion.”

Excellent answer.

---

# PART 6 — LOGGING

## 20. Have you used logging?

### Answer

> “Yes. In Spring Boot applications, I use SLF4J with an implementation such as Logback for application logging.
>
> I use logs mainly for request tracing, business-flow debugging, exceptions and production troubleshooting.
>
> I also avoid logging sensitive information such as passwords, tokens or unnecessary KYC data.”

The transcript itself describes API debugging through backend logs using **SLF4J with Logback**. Pasted text

---

# 21–23. Logging levels and ranking

### Safest Spring Boot / SLF4J answer

```text
ERROR
WARN
INFO
DEBUG
TRACE
```

### Spoken answer

> “From most critical to least critical, the common SLF4J/Logback levels are ERROR, WARN, INFO, DEBUG and TRACE.”

### Meaning

**ERROR**  
Serious failure requiring attention.

**WARN**  
Something unusual or potentially problematic.

**INFO**  
Normal application/business events.

**DEBUG**  
Detailed information for troubleshooting.

**TRACE**  
Very detailed diagnostic information.

### BIG TRAP

Some logging frameworks such as Log4j also have:

```text
FATAL
ERROR
WARN
INFO
DEBUG
TRACE
```

So if interviewer says:

**“Where is FATAL?”**

Say:

> “FATAL exists in some logging frameworks such as Log4j, but SLF4J's standard API levels do not include FATAL. In a typical Spring Boot + SLF4J/Logback setup, I would use ERROR as the highest level.”

That's a **very strong trap answer**.

---

# PART 7 — SPRING AOP

## 24–25. What is Spring AOP?

### Answer

> “AOP stands for Aspect-Oriented Programming. It is used to separate cross-cutting concerns from core business logic.
>
> Typical cross-cutting concerns are logging, security, auditing, transaction handling and performance monitoring.
>
> In Spring, AOP is commonly implemented using proxies, so the framework can execute additional behavior around a target method.”

### Project connection

For your onboarding application:

> “For example, auditing or centralized logging around onboarding service methods can be handled as a cross-cutting concern rather than duplicating logging code in every business method.”

### Don't overclaim

If you did not personally write custom aspects, say:

> “I understand and have worked with the concept of Spring AOP and annotations, although I didn't build a large custom AOP framework myself.”

That is much safer.

---

# 26. What are the different types of advice?

### Memorize exactly these five:

```text
1. Before
2. After
3. AfterReturning
4. AfterThrowing
5. Around
```

### Meaning

**Before**

> Runs before target method execution.

**After**

> Runs after method execution, typically whether it succeeds or throws.

**AfterReturning**

> Runs only when method completes successfully.

**AfterThrowing**

> Runs when method throws an exception.

**Around**

> Wraps the method and can run code before and after it; it can also control whether the target method executes using `proceed()`.

---

# 27. Explain Around Advice

### Interview answer

> “Around advice surrounds the target method execution.
>
> It can perform logic before the method, invoke the target using `ProceedingJoinPoint.proceed()`, and then perform logic after it.
>
> Because it controls the invocation, it can also modify the arguments, return value or decide not to proceed, depending on the use case.”

### Code

```java
@Around("execution(* com.example.service..*(..))")
public Object around(ProceedingJoinPoint pjp) throws Throwable {

    long start = System.currentTimeMillis();

    Object result = pjp.proceed();

    long end = System.currentTimeMillis();

    System.out.println("Time: " + (end - start));

    return result;
}
```

### Project example

> “For onboarding APIs, Around advice could be used for measuring execution time or adding standardized audit/logging behavior around service methods.”

---

# 28. Around vs AfterThrowing

This is a likely trap because the interviewer explicitly asked this comparison.

### Answer

> “Around advice wraps the entire method execution and can execute both before and after the method. It can also decide whether `proceed()` is called.
>
> AfterThrowing advice is specifically triggered when the target method throws an exception. It is useful for exception logging, auditing or metrics related to failures.”

### Example

### Around

```text
Before
   ↓
target method
   ↓
After
```

### AfterThrowing

```text
target method
   ↓
exception
   ↓
AfterThrowing
```

### Very important distinction

| Advice | Success | Exception | Can control method execution? |
|---|---:|---:|---:|
| Before | Before | Before | No |
| After | Yes | Yes | No |
| AfterReturning | Yes | No | No |
| AfterThrowing | No | Yes | No |
| Around | Yes | Yes | **Yes** |

---

# The 25-Minute Memorization Plan

Don't spend equal time on all 25 questions.

## Minute 0–5: Java Core

Memorize:

> **Multithreading → Thread/Runnable/Callable → ExecutorService**

> **Overloading = compile time**

> **Overriding = runtime**

> **Abstraction = interface + abstract class**

> **Interface = contract**

> **Abstract class = shared state/implementation**

> **No multiple class inheritance → diamond problem**

---

## Minute 5–10: REST

Memorize this block:

> **GET, PUT, DELETE = idempotent**

> **POST = generally non-idempotent**

> **Path variable = resource identity**

> **Query parameter = filtering/options**

Then your project sentence:

> “In onboarding, idempotency is important because retries must not create duplicate business operations.”

That's the sentence I especially want you to remember.

---

## Minute 10–14: Streams

Only memorize this:

```text
map       → one input → one output
flatMap   → one input → multiple/stream → flatten
```

And code:

```java
list.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
```

Then:

> `map` gives `Stream<List<Integer>>`; `flatMap` gives `Stream<Integer>`.

---

## Minute 14–17: Observable

Memorize:

> **Observable = stream**

> **Promise = one result**

> **Observable = RxJS operators + unsubscribe**

> **Angular HttpClient = Observable**

The supplied transcript confirms that this interviewer specifically went into Observable vs Promise, so don't skip this one. Pasted text

---

## Minute 17–20: Logging

Just memorize:

```text
ERROR
WARN
INFO
DEBUG
TRACE
```

And the trap:

> “FATAL exists in some logging frameworks, but not in SLF4J's standard API.”

---

## Minute 20–25: AOP

Memorize:

```text
Before
After
AfterReturning
AfterThrowing
Around
```

Then:

> **Around = controls complete execution**

> **AfterThrowing = only exception path**

---

# 🔥 12 Traps You Must Not Get Wrong

### 1.
**“Java doesn't support multiple inheritance.”**

❌ Too broad.

✅ **Java doesn't support multiple inheritance of classes.**

---

### 2.
**“Overloading depends on return type.”**

❌ Wrong.

✅ Parameters must differ.

---

### 3.
**“Overriding happens at compile time.”**

❌

✅ Runtime polymorphism.

---

### 4.
**“DELETE isn't idempotent because second DELETE can return 404.”**

❌

✅ Its intended server-side effect remains idempotent.

---

### 5.
**“POST is never idempotent.”**

Don't say “never.”

✅ POST is **generally non-idempotent**, but an application can design an idempotent POST using an idempotency key.

---

### 6.
**“flatMap is faster than map.”**

❌

✅ They solve different transformation problems.

---

### 7.
**“Promise can return multiple values.”**

❌

✅ Promise settles once.

---

### 8.
**“Every Observable emits multiple values.”**

❌

✅ Observable supports multiple emissions; a particular Observable may emit one or many. Angular HTTP is typically one emission.

---

### 9.
**“Fatal is always the highest Spring logging level.”**

❌ Not for SLF4J/Logback.

✅ `ERROR` is the highest standard SLF4J level.

---

### 10.
**“After advice means only success.”**

❌

✅ After can run whether the method returns or throws.

---

### 11.
**“AfterReturning catches exceptions.”**

❌

✅ AfterThrowing handles exceptions.

---

### 12.
**“Around advice is just Before + After.”**

Incomplete.

✅ Around can control whether the target method executes through `proceed()`.

---

# Final 60-Second Revision

Before entering the interview, say this to yourself:

> **Multithreading:** Thread, Runnable, Callable, ExecutorService.  
> **Overloading:** compile time.  
> **Overriding:** runtime.  
> **Abstraction:** interface + abstract class.  
> **Inheritance:** no multiple class inheritance because diamond problem.  
> **REST:** GET/PUT/DELETE idempotent; POST generally non-idempotent.  
> **Path variable:** identifies resource.  
> **Query parameter:** filters/options.  
> **map:** one-to-one transformation.  
> **flatMap:** flatten nested streams.  
> **Observable:** asynchronous stream.  
> **Promise:** one eventual result.  
> **Logging:** ERROR → WARN → INFO → DEBUG → TRACE.  
> **AOP:** cross-cutting concerns.  
> **Advice:** Before, After, AfterReturning, AfterThrowing, Around.  
> **Around:** controls execution with `proceed()`.  
> **AfterThrowing:** exception path.

And for **Nomura specifically**, whenever they push you from theory into project:

> **“In my onboarding system, the important considerations were reliability, idempotency, concurrency, security and failure handling.”**

That one sentence gives you a very natural bridge from Java theory to your Saudi banking backend project. Pasted text