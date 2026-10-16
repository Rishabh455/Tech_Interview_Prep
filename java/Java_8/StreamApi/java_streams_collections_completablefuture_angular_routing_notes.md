# Java Streams, Collections, Concurrency & Angular Routing --- Revision Notes

> Interview-focused notes in simple words, organized topic-wise with
> examples and quick revision points.

------------------------------------------------------------------------

# Part 1: Java Stream API

## 1. What is the Stream API?

The Stream API processes data from collections or arrays in a
declarative way. It does not usually modify the original collection.

Common operations: - **Intermediate operations** return another stream:
`filter()`, `map()`, `sorted()`, `distinct()`, `limit()`. - **Terminal
operations** produce a result or side effect: `collect()`, `forEach()`,
`count()`, `findFirst()`, `max()`, `reduce()`.

``` java
List<Integer> nums = Arrays.asList(5, 2, 8, 1);

List<Integer> result = nums.stream()
        .filter(n -> n > 2)
        .sorted()
        .collect(Collectors.toList());

System.out.println(result); // [5, 8]
```

A stream pipeline generally executes when a terminal operation is
called.

## 2. Count the Frequency of Every Word

``` java
String sentence = "java is easy java is powerful";

Map<String, Long> map = Arrays.stream(sentence.split("\\s+"))
        .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()));

System.out.println(map);
```

Possible output (order may vary):

``` text
{java=2, is=2, easy=1, powerful=1}
```

### Explanation

1.  `sentence.split("\\s+")` splits the sentence on one or more
    whitespace characters.
2.  `Arrays.stream(...)` converts the resulting array into a stream.
3.  `groupingBy(...)` groups equal words together.
4.  `Function.identity()` uses the word itself as the map key. It is
    equivalent to `word -> word`.
5.  `Collectors.counting()` counts occurrences and returns a `Long`.
6.  The result is a `Map<String, Long>`.

### Preserve first-seen order

The default `groupingBy()` usually creates a `HashMap`, whose iteration
order is not guaranteed. Use `LinkedHashMap::new` if order matters:

``` java
Map<String, Long> map = Arrays.stream(sentence.split("\\s+"))
        .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()));
```

## 3. Find the Longest String

Example list:

``` java
List<String> list = Arrays.asList("Java", "Spring", "Angular", "Python");
```

### Approach A --- `max()` (recommended)

``` java
String result = list.stream()
        .max(Comparator.comparingInt(String::length))
        .orElse(null);

System.out.println(result); // Angular
```

-   `String::length` gets each string's length.
-   `comparingInt(...)` compares strings by length.
-   `max(...)` finds the longest.
-   `orElse(null)` returns `null` if the stream is empty.

If multiple strings share the maximum length, `max()` returns one of the
tied maximum elements; do not rely on which tied item is selected unless
the comparator defines a tie-breaker.

### Approach B --- `reduce()`

``` java
String result = list.stream()
        .reduce((a, b) -> a.length() >= b.length() ? a : b)
        .orElse(null);
```

Compare two strings at a time and keep the longer one. If lengths tie,
this expression keeps `a`.

### Approach C --- `sorted()`

``` java
String result = list.stream()
        .sorted(Comparator.comparingInt(String::length).reversed())
        .findFirst()
        .orElse(null);
```

This sorts the whole list by length, longest first, then takes the first
item. It is usually less efficient than `max()` because sorting costs
about O(n log n).

### Interview tip

Use `max(Comparator.comparingInt(String::length))` when only the longest
string is needed.

## 4. Find the First Non-Repeated Character

Example: `"programming"` → `p`.

### Approach A --- Frequency map (preserves character order)

``` java
String str = "programming";

Map<Character, Long> frequency = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(
                Function.identity(),
                LinkedHashMap::new,
                Collectors.counting()));

Character result = frequency.entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst()
        .orElse(null);

System.out.println(result); // p
```

### How it works

-   `str.chars()` creates an `IntStream` of character values.
-   `mapToObj(c -> (char) c)` converts each value to a `Character`
    object.
-   `groupingBy(Function.identity(), LinkedHashMap::new, counting())`
    builds a frequency map while preserving first-occurrence order.
-   `entrySet()` provides key-value pairs.
-   `filter(e -> e.getValue() == 1)` keeps characters appearing once.
-   `map(Map.Entry::getKey)` keeps only the character.
-   `findFirst()` returns the first matching character.
-   `orElse(null)` gives `null` if every character repeats or the string
    is empty.

The count type is `Long`, because `Collectors.counting()` returns
`Long`.

### Understand the collector

``` java
Collectors.groupingBy(
        Function.identity(),   // use the character itself as key
        LinkedHashMap::new,    // preserve insertion order
        Collectors.counting()  // count each occurrence
)
```

`entrySet()` means the set of map entries. Each entry has a key and a
value: - `entry.getKey()` → character - `entry.getValue()` → frequency

### Approach B --- `indexOf()` and `lastIndexOf()`

``` java
Character result = str.chars()
        .mapToObj(c -> (char) c)
        .filter(c -> str.indexOf(c) == str.lastIndexOf(c))
        .findFirst()
        .orElse(null);
```

A character appears exactly once if its first and last index are equal.
This approach is simple but can take O(n²) time because index searches
are repeated for each character.

> Note: `chars()` processes UTF-16 code units. For full Unicode code
> points (such as some emoji), use `codePoints()` and handle code points
> as integers.

## 5. `Function.identity()`

`Function.identity()` returns a function that returns its input
unchanged.

``` java
Function<String, String> f = Function.identity();
System.out.println(f.apply("Java")); // Java
```

It is equivalent to:

``` java
Function<String, String> f = value -> value;
```

Common use: use each stream element itself as the grouping key.

## 6. `Collectors.groupingBy()`

Groups stream elements by a classifier function.

``` java
Map<String, List<String>> byLengthGroup = words.stream()
        .collect(Collectors.groupingBy(String::toLowerCase));
```

Common overloads:

``` java
Collectors.groupingBy(classifier)
Collectors.groupingBy(classifier, downstreamCollector)
Collectors.groupingBy(classifier, mapFactory, downstreamCollector)
```

Example for counting:

``` java
Collectors.groupingBy(
        Function.identity(),
        LinkedHashMap::new,
        Collectors.counting())
```

This creates a map of each distinct element to its count, preserving
encounter order when the stream has an encounter order and the map is a
`LinkedHashMap`.

## 7. `Map.Entry` and `entrySet()`

A `Map.Entry<K, V>` represents one key-value pair.

``` java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey());
    System.out.println(entry.getValue());
}
```

-   `entrySet()` --- all key-value entries.
-   `keySet()` --- all keys.
-   `values()` --- all values.

Use `entrySet()` when both key and value are needed.

## 8. `Optional` and `Optional.get()`

`Optional<T>` represents a value that may or may not be present.

``` java
Optional<String> value = Optional.of("Java");
System.out.println(value.get()); // Java
```

`get()` extracts the value, but throws `NoSuchElementException` if the
Optional is empty. Prefer safe alternatives when absence is possible:

``` java
value.orElse("default");
value.orElseGet(() -> createDefault());
value.ifPresent(System.out::println);
```

-   `orElse(defaultValue)` evaluates the default expression before the
    call.
-   `orElseGet(supplier)` calls the supplier only if the Optional is
    empty.
-   `orElseThrow()` throws if empty.

## 9. Find the Highest-Paid Employee in Each Department

``` java
class Employee {
    int id;
    String name;
    String dept;
    double salary;

    Employee(int id, String name, String dept, double salary) {
        this.id = id;
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }

    public String getDept() { return dept; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return name + " " + salary;
    }
}
```

Data:

``` java
List<Employee> employees = Arrays.asList(
        new Employee(1, "John", "IT", 80000),
        new Employee(2, "Bob", "IT", 90000),
        new Employee(3, "David", "HR", 70000),
        new Employee(4, "Alex", "HR", 95000)
);
```

Solution:

``` java
Map<String, Employee> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDept,
                Collectors.collectingAndThen(
                        Collectors.maxBy(
                                Comparator.comparing(Employee::getSalary)),
                        Optional::get)));

System.out.println(result);
```

Conceptual result:

``` text
IT -> Bob 90000.0
HR -> Alex 95000.0
```

### Why is `Optional::get` used?

-   `Collectors.maxBy(...)` returns `Optional<Employee>`.
-   `collectingAndThen(collector, finisher)` runs a finishing function
    after collecting.
-   `Optional::get` extracts the `Employee` from the Optional, so the
    final map value is `Employee`, not `Optional<Employee>`.

In this specific `groupingBy` pipeline, every group is formed from at
least one employee, so each group's `maxBy` result is present. In a
general situation where an Optional might be empty, calling `get()`
could throw an exception.

A more readable alternative uses a helper collector or computes the
maximum per group separately. For interview purposes, understand the
type flow:

``` text
groupingBy department
  -> maxBy salary
  -> Optional<Employee>
  -> collectingAndThen + Optional::get
  -> Employee
```

------------------------------------------------------------------------

# Part 2: Java Method References

## 10. What is a Method Reference?

A method reference is a shorter form of a lambda expression when the
lambda simply calls an existing method.

### A. Static method reference

``` java
ClassName::staticMethod
```

Example:

``` java
list.forEach(System.out::println);
```

Similar to:

``` java
list.forEach(item -> System.out.println(item));
```

### B. Particular object instance method

``` java
object::method
```

Example:

``` java
String str = "hello";
Supplier<String> supplier = str::toUpperCase;
System.out.println(supplier.get()); // HELLO
```

The existing `str` object is used.

### C. Arbitrary object instance method

``` java
ClassName::instanceMethod
```

Example:

``` java
list.sort(String::compareTo);
```

The functional interface supplies the object on which the method is
called. This is similar to:

``` java
list.sort((a, b) -> a.compareTo(b));
```

### Memory trick

-   `object::method` --- a particular existing object.
-   `ClassName::method` --- an object is supplied as an argument by the
    functional interface.
-   `ClassName::staticMethod` --- a static method.

------------------------------------------------------------------------

# Part 3: Java Collections Framework

## 11. `Collection` vs `Collections`

  -----------------------------------------------------------------------
  `Collection`                        `Collections`
  ----------------------------------- -----------------------------------
  Interface                           Utility class

  Represents a group of objects       Provides static helper methods

  Parent interface of `List`, `Set`,  Used for sorting, reversing,
  and `Queue`                         searching, etc.
  -----------------------------------------------------------------------

Examples:

``` java
Collection<String> items = new ArrayList<>();
Collections.sort(list);
Collections.reverse(list);
```

## 12. Common `Collections` Utility Methods

``` java
Collections.sort(list);
Collections.reverse(list);
Collections.shuffle(list);
Collections.max(list);
Collections.min(list);
Collections.frequency(list, value);
Collections.swap(list, i, j);
Collections.binarySearch(sortedList, value);
```

Typical complexity: - `sort()` --- O(n log n). - `reverse()` --- O(n). -
`shuffle()` --- O(n). - `max()` / `min()` --- O(n). - `frequency()` ---
O(n). - `binarySearch()` --- O(log n) on a suitable random-access sorted
list.

For custom sorting:

``` java
list.sort(Comparator.comparingInt(String::length));
```

or:

``` java
Collections.sort(list, Comparator.comparingInt(String::length));
```

For numeric comparisons, prefer `Integer.compare(a, b)` instead of
`a - b`, which can overflow.

## 13. `ArrayList` Time Complexity

  Operation                 Typical complexity
  ----------------------- --------------------
  `get(index)`                            O(1)
  `set(index, element)`                   O(1)
  `add(element)`                O(1) amortized
  `add(index, element)`                   O(n)
  `remove(index)`                         O(n)
  `contains(element)`                     O(n)

`set(index, element)` replaces an element at an index; it does not shift
the remaining elements.

## 14. `contains()` Complexity

  Collection                     Typical complexity
  ---------------------------- --------------------
  `ArrayList.contains()`                       O(n)
  `LinkedList.contains()`                      O(n)
  `HashSet.contains()`                 O(1) average
  `LinkedHashSet.contains()`           O(1) average
  `TreeSet.contains()`                     O(log n)

For maps: - `HashMap.containsKey()` --- O(1) average. -
`HashMap.containsValue()` --- O(n).

## 15. How `HashSet` Works Internally

`HashSet` uses a `HashMap` internally: - Set elements are stored as map
keys. - The values use a shared dummy object. - `hashCode()` helps
locate a bucket. - `equals()` helps distinguish keys that land in the
same bucket.

Conceptually:

``` text
HashSet
  -> HashMap
       "Java"   -> PRESENT
       "Spring" -> PRESENT
```

Operations such as `add()` and `contains()` are O(1) on average,
assuming a good hash distribution.

## 16. `LinkedHashSet` and `LinkedHashMap`

### `LinkedHashSet`

-   Uses `LinkedHashMap` internally.
-   Does not allow duplicate elements.
-   Preserves insertion order.
-   `add()`, `contains()`, and `remove()` are O(1) on average.

### `LinkedHashMap`

-   Extends `HashMap`.
-   Maintains a doubly linked list across entries to preserve order.
-   Default is insertion order.
-   Can be configured for access order, useful for LRU-style logic:

``` java
Map<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);
```

## 17. `HashMap` vs `Hashtable`

  -----------------------------------------------------------------------
  Feature                 `HashMap`               `Hashtable`
  ----------------------- ----------------------- -----------------------
  Thread safety           Not synchronized by     Methods are
                          default                 synchronized

  Null key/value          Allows one null key and Does not allow null
                          null values             keys or values

  Status                  Common modern choice    Legacy class
                          for non-concurrent use  

  Concurrent alternative  Use `ConcurrentHashMap` Usually not preferred
                          for many concurrent use for new code
                          cases                   
  -----------------------------------------------------------------------

### Internal implementation

Both use hash buckets to locate entries. In modern Java, `HashMap` can
use a linked list or a balanced tree for a heavily collided bucket.

## 18. `ConcurrentHashMap`

A thread-safe map designed for concurrent access by multiple threads.

Key points: - Allows concurrent reads and updates with less contention
than locking the whole map. - Does not allow `null` keys or values. -
`get()`, `put()`, and `remove()` are generally O(1) on average. - In
Java 8+, it uses a bucket table, linked nodes and tree bins for heavy
collisions. - Uses CAS for certain updates and synchronizes on a
particular bin when needed, instead of locking the entire map for every
update.

### What is CAS?

**CAS (Compare-And-Swap)** atomically checks whether a value is still
what was expected and, if so, replaces it. If another thread changed it
first, the update can fail and be retried.

### What does synchronization on a bin mean?

A bin is a bucket in the hash table. If an update needs a lock for a
non-empty bin, that bin can be synchronized rather than locking the
entire map. Other bins may still be accessed concurrently.

> Java 7 used a segmented design. Do not describe Java 8+
> `ConcurrentHashMap` as using the old segment-per-lock architecture.

## 19. `Iterator`

An `Iterator` traverses a collection one element at a time.

``` java
Iterator<String> it = list.iterator();

while (it.hasNext()) {
    String value = it.next();
    System.out.println(value);
}
```

Methods: - `hasNext()` --- checks whether another element exists. -
`next()` --- returns the next element. - `remove()` --- removes the last
element returned by the iterator, if supported.

Many standard collection iterators are fail-fast: if the collection is
structurally modified outside the iterator during iteration, they may
throw `ConcurrentModificationException`. Use the iterator's own
`remove()` when removing during iteration.

## 20. Iterating Lists, Sets, Stacks, and Maps

### List or Set

``` java
for (String item : list) {
    System.out.println(item);
}
```

Or use an iterator.

### Stack

A `Stack` iterator traverses from the bottom toward the top, not in pop
order. For LIFO processing:

``` java
while (!stack.empty()) {
    System.out.println(stack.pop());
}
```

`Stack` is a legacy class; `Deque` / `ArrayDeque` is generally preferred
for stack behavior.

### Map

A `Map` is not a `Collection` and does not directly provide
`iterator()`.

For both key and value:

``` java
for (Map.Entry<Integer, String> entry : map.entrySet()) {
    System.out.println(entry.getKey());
    System.out.println(entry.getValue());
}
```

For keys only:

``` java
for (Integer key : map.keySet()) {
    System.out.println(key);
}
```

For values only:

``` java
for (String value : map.values()) {
    System.out.println(value);
}
```

## 21. Static Methods: Inheritance and Interfaces

### Static method in a class

A subclass can access an inherited static method, but static methods are
not overridden polymorphically. If a subclass declares a static method
with the same signature, it **hides** the parent method.

``` java
class Parent {
    static void show() {
        System.out.println("Parent");
    }
}

class Child extends Parent { }

Child.show(); // Parent
```

If `Child` declares its own static `show()`, the selected method depends
on the class/reference type, not the runtime object.

### Static method in an interface

Static interface methods are **not inherited** by implementing classes
and cannot be overridden as instance methods.

``` java
interface A {
    static void show() {
        System.out.println("A");
    }
}

class B implements A { }

// Correct:
A.show();

// B.show(); // Does not compile
```

------------------------------------------------------------------------

# Part 4: `CompletableFuture` and `ExecutorService`

## 22. What is `CompletableFuture`?

`CompletableFuture` represents a result that may become available in the
future. It supports asynchronous tasks and chaining operations without
needing to block after each step.

## 23. What is `ExecutorService`?

`ExecutorService` manages a pool of threads and executes submitted
tasks.

``` java
ExecutorService executor = Executors.newFixedThreadPool(3);
```

-   `ExecutorService` manages threads and task execution.
-   `CompletableFuture` manages an asynchronous result and the stages
    that depend on it.

## 24. `supplyAsync()` vs `runAsync()`

### `supplyAsync()` --- returns a result

``` java
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(() -> {
            // This lambda is the task
            return "Hello";
        }, executor);
```

Type: `CompletableFuture<String>`.

### `runAsync()` --- no result

``` java
CompletableFuture<Void> future =
        CompletableFuture.runAsync(() -> {
            System.out.println("Running task");
        }, executor);
```

Type: `CompletableFuture<Void>`.

If no executor is provided, async methods normally use the common
ForkJoinPool.

## 25. Common Completion Methods

``` java
future.thenApply(value -> value.toUpperCase());
```

-   `thenApply()` transforms a result and returns a new future.
-   `thenAccept()` consumes a result and returns
    `CompletableFuture<Void>`.
-   `thenRun()` runs an action without receiving the previous result.
-   `thenCompose()` chains an operation that itself returns a
    `CompletionStage`, avoiding nested futures.
-   `thenCombine()` combines results from two futures.

Example:

``` java
CompletableFuture<String> upperFuture =
        CompletableFuture.supplyAsync(() -> "hello", executor)
                .thenApply(String::toUpperCase);
```

The result is stored in the completed future. You can continue chaining
other stages without immediately extracting a plain value.

## 26. `join()` vs `get()`

``` java
String result = future.join();
```

-   `join()` returns the result, but blocks the calling thread if the
    future is not complete.
-   `get()` also blocks if incomplete, but throws checked exceptions
    such as `InterruptedException` and `ExecutionException`.
-   `join()` wraps exceptional completion in an unchecked
    `CompletionException`.

If the future has already completed, `join()` / `get()` can return
immediately.

### What is non-blocking chaining?

``` java
future.thenApply(value -> value.toUpperCase())
      .thenAccept(System.out::println);
```

This registers dependent stages rather than immediately waiting for the
result on the current thread. The stages execute when the preceding
future completes, using the applicable executor/thread rules.

### Shutdown the executor

For a standalone example, clean up the executor when it is no longer
needed:

``` java
executor.shutdown();
```

Do not shut down a shared executor that your application still needs.

------------------------------------------------------------------------

# Part 5: Angular Routing

## 27. What is `ActivatedRoute`?

`ActivatedRoute` is an Angular Router service that provides information
about the currently active route, including: - Route parameters - Query
parameters - Static route data - Other route information such as URL
segments and route configuration

Import:

``` typescript
import { ActivatedRoute } from '@angular/router';
```

A component can inject it:

``` typescript
constructor(private route: ActivatedRoute) {}
```

In newer Angular code, `inject(ActivatedRoute)` is another supported way
to obtain the service.

## 28. Route Parameters

Route parameters are dynamic values defined in the route path.

Route configuration:

``` typescript
const routes: Routes = [
  { path: 'employee/:id', component: EmployeeComponent }
];
```

URL:

``` text
/employee/101
```

Read the parameter once:

``` typescript
const id = this.route.snapshot.paramMap.get('id');
console.log(id); // "101"
```

`paramMap.get()` returns a string or `null`.

### Read route parameters reactively

``` typescript
this.route.paramMap.subscribe(params => {
  const id = params.get('id');
  console.log(id);
});
```

Use a subscription when the component may stay active while the route
parameter changes, such as navigating from `/employee/101` to
`/employee/102`.

## 29. Query Parameters

Query parameters appear after `?` in a URL and are often used for
filters, pagination, or sorting.

URL:

``` text
/employee/101?dept=IT&page=2
```

Read one value:

``` typescript
const dept = this.route.snapshot.queryParamMap.get('dept');
console.log(dept); // "IT"
```

Read reactively:

``` typescript
this.route.queryParamMap.subscribe(params => {
  const page = params.get('page');
  console.log(page);
});
```

## 30. Static Route Data

Static route data is configured directly on a route. Common uses include
page titles, labels, or other non-sensitive metadata.

Route configuration:

``` typescript
const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    data: {
      title: 'Admin Dashboard',
      role: 'admin'
    }
  }
];
```

Read the data once:

``` typescript
const title = this.route.snapshot.data['title'];
const role = this.route.snapshot.data['role'];

console.log(title); // Admin Dashboard
console.log(role);  // admin
```

Read reactively:

``` typescript
this.route.data.subscribe(data => {
  console.log(data['title']);
  console.log(data['role']);
});
```

Static route data is metadata, not a security control. Use route guards
for navigation rules and enforce authorization on the server as well.

## 31. `snapshot` vs Observable Subscription

  -----------------------------------------------------------------------
  `snapshot`                          Observable subscription
  ----------------------------------- -----------------------------------
  Reads the current value once        Receives emissions when the route
                                      information changes

  Simple for one-time access          Useful when the same component
                                      instance can receive changed route
                                      values

  Does not automatically update a     Lets code react to emitted updates
  local variable later                
  -----------------------------------------------------------------------

Example using `snapshot`:

``` typescript
const id = this.route.snapshot.paramMap.get('id');
```

Example using subscription:

``` typescript
this.route.paramMap.subscribe(params => {
  const id = params.get('id');
});
```

If you subscribe manually to long-lived observables, manage the
subscription lifecycle when necessary. Angular also provides patterns
such as `takeUntilDestroyed()` in supported Angular versions.

## 32. Route Parameters vs Query Parameters vs Route Data

  ----------------------------------------------------------------------------
  Type                    Example                      Typical purpose
  ----------------------- ---------------------------- -----------------------
  Route parameter         `/employee/101`              Identifies a specific
                                                       resource

  Query parameter         `/employee?page=2`           Optional filters,
                                                       sorting, pagination

  Static route data       `data: { title: 'Admin' }`   Metadata configured
                                                       with the route
  ----------------------------------------------------------------------------

Interview answer:

> `ActivatedRoute` provides information about the currently active
> route. We use `paramMap` for route parameters, `queryParamMap` for
> query parameters, and `data` for route metadata. `snapshot` reads the
> current value once, while observable subscriptions let us react to
> changes.

------------------------------------------------------------------------

# Part 6: Quick Interview Revision

## Stream API

-   `filter()` selects elements matching a condition.
-   `map()` transforms each element.
-   `sorted()` sorts elements.
-   `max()` returns the maximum element as an `Optional`.
-   `reduce()` combines elements into one result.
-   `collect()` gathers stream elements into a collection or other
    result.
-   `forEach()` performs an action for each element.
-   `groupingBy()` groups elements by a key.
-   `counting()` counts elements and returns `Long`.
-   `Function.identity()` returns its input unchanged.
-   `entrySet()` provides map key-value pairs.

## Optional

-   `Optional<T>` represents a potentially absent value.
-   `get()` throws if empty.
-   `orElse()` supplies a fallback value.
-   `orElseGet()` supplies a fallback lazily.

## Collections

-   `Collection` is an interface; `Collections` is a utility class.
-   `ArrayList` indexed get/set: O(1).
-   `HashSet` uses `HashMap` internally.
-   `LinkedHashSet` preserves insertion order.
-   `LinkedHashMap` maintains order with a doubly linked list.
-   `HashMap` is not synchronized by default.
-   `ConcurrentHashMap` supports concurrent access without a single
    whole-map lock for every operation.
-   `Iterator` uses `hasNext()` and `next()`.
-   A `Map` is not a `Collection`; use `entrySet()`, `keySet()`, or
    `values()` to iterate.

## Concurrency

-   `supplyAsync()` returns a result.
-   `runAsync()` does not return a result.
-   `ExecutorService` manages task execution and threads.
-   `CompletableFuture` represents and chains asynchronous results.
-   `join()` and `get()` block if the future has not completed.
-   `thenApply()` transforms; `thenAccept()` consumes; `thenCompose()`
    chains futures.

## Angular Routing

-   `ActivatedRoute` reads information about the active route.
-   `paramMap` reads route parameters.
-   `queryParamMap` reads query parameters.
-   `data` reads route metadata.
-   `snapshot` is a one-time read.
-   Subscribing to route observables lets code react to changes.
-   Route data is not a substitute for authorization.

------------------------------------------------------------------------

# Quick Self-Test

1.  What does `Collectors.counting()` return? --- `Long`.
2.  Why use `LinkedHashMap::new` in `groupingBy()`? --- To preserve
    insertion order.
3.  Why does `maxBy()` return `Optional<T>`? --- A maximum may not exist
    for an empty input.
4.  What does `Function.identity()` do? --- Returns the input unchanged.
5.  What is `entrySet()` used for? --- Iterating over key-value pairs.
6.  What is the average lookup complexity of `HashSet.contains()`? ---
    O(1).
7.  Which collection preserves insertion order and prevents duplicates?
    --- `LinkedHashSet`.
8.  Does `join()` block? --- Yes, if the future is incomplete.
9.  What is the difference between `supplyAsync()` and `runAsync()`? ---
    Result vs no result.
10. Which Angular API reads `?page=2`? --- `queryParamMap`.
11. Which Angular API reads `data: { title: 'Admin' }`? --- `route.data`
    or `route.snapshot.data`.
12. What is the difference between `snapshot` and subscription? ---
    One-time value vs reacting to emissions.
