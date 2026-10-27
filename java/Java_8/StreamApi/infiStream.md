# Java 8 Stream API — Infosys In-Person Interview Preparation
## 25 Important Questions, Answers, Coding Solutions & Interview Traps

**Target:** Java Developer | 3+ Years Experience  
**Level:** Basic to Medium-Advanced  
**Format:** Direct interview answers + Java 8 code + follow-up questions

### Common Employee Class

Assume the following class is available for all employee-related questions.

```java
class Employee {
    private String name;
    private String department;
    private int salary;

    public Employee(String name, String department, int salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return name + " - " + department + " - " + salary;
    }
}
```

Required imports for the examples:

```java
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
```

---

# PART 1: Basic Stream Operations

## Q1. Remove duplicates from a list and sort in descending order.

**Answer:** Use `distinct()` to remove duplicates and `sorted(Comparator.reverseOrder())` to sort in descending order.

```java
List<Integer> numbers = Arrays.asList(10, 20, 10, 30, 40, 20);

List<Integer> result = numbers.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());

System.out.println(result);
```

**Output:**
```text
[40, 30, 20, 10]
```

**How to explain in the interview:**
- `stream()` creates a stream from the list.
- `distinct()` removes duplicate elements.
- `sorted()` sorts the elements.
- `Comparator.reverseOrder()` gives descending order.
- `collect(Collectors.toList())` collects the result into a list.

**Follow-up:** Does `distinct()` preserve order?

Yes. For an ordered stream, `distinct()` preserves the encounter order of the first occurrence of each element.

**Interview trap:** `sorted()` without a comparator sorts in natural ascending order for integers.

---

## Q2. Find odd numbers and return their squares.

**Answer:** Use `filter()` to select odd numbers and `map()` to square them.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

List<Integer> result = numbers.stream()
        .filter(n -> n % 2 != 0)
        .map(n -> n * n)
        .collect(Collectors.toList());

System.out.println(result);
```

**Output:**
```text
[1, 9, 25]
```

**Interview explanation:**
- `filter()` selects elements satisfying a condition.
- `map()` transforms each selected element.
- Here, odd numbers are selected and converted into their squares.

**Follow-up:** What is the difference between `filter()` and `map()`?

`filter()` selects or removes elements based on a condition. `map()` transforms each element into another value.

---

## Q3. Get the second and third elements from a list.

**Answer:** Use `skip(1)` to skip the first element and `limit(2)` to take the next two elements.

```java
List<Integer> numbers = Arrays.asList(10, 20, 30, 40, 50);

List<Integer> result = numbers.stream()
        .skip(1)
        .limit(2)
        .collect(Collectors.toList());

System.out.println(result);
```

**Output:**
```text
[20, 30]
```

**Interview trap:** `skip(1)` means skip one element, not start at index 1 explicitly. For an ordered list, it produces the expected second and third elements.

**Follow-up:** Does `limit(2)` always return two elements?

No. If fewer than two elements remain, it returns only the available elements.

---

## Q4. Find the second-highest distinct number.

**Answer:** Remove duplicates, sort in descending order, skip the highest number and retrieve the next one.

```java
List<Integer> numbers = Arrays.asList(45, 20, 45, 30, 10, 30);

Optional<Integer> result = numbers.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .skip(1)
        .findFirst();

result.ifPresent(System.out::println);
```

**Output:**
```text
30
```

**Why use `distinct()`?**

If the highest number appears multiple times, we still want the second-highest distinct value.

**Important interview trap:** `findFirst()` returns an `Optional`. Do not call `get()` without checking whether a value exists.

```java
Integer result = numbers.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .skip(1)
        .findFirst()
        .orElse(null);
```

This returns `null` if no second-highest distinct number exists. In production code, returning `Optional<Integer>` is often preferable to using `null`.

**Follow-up:** What if all numbers are equal?

After `distinct()`, only one value remains, so there is no second-highest distinct number.

**Alternative:** For a large dataset, a one-pass approach can find the two highest distinct values without sorting the entire list.

---

## Q5. Find the longest string in a list.

**Answer:** Use `max()` with `Comparator.comparingInt()`.

```java
List<String> words = Arrays.asList(
        "Java", "Spring", "SpringBoot", "Microservices"
);

Optional<String> result = words.stream()
        .max(Comparator.comparingInt(String::length));

result.ifPresent(System.out::println);
```

**Output:**
```text
Microservices
```

**Interview explanation:** `max()` selects the element considered greatest by the supplied comparator. Here, the comparison is based on string length.

**Follow-up:** What happens if the list is empty?

`max()` returns `Optional.empty()`.

**Interview trap:** If multiple strings have the same maximum length, this comparator does not specify a special tie-breaking rule. Do not assume it returns the alphabetically first string.

---

# PART 2: Employee-Based Coding Questions

## Q6. Find the first employee whose salary is greater than ₹50,000.

**Answer:** Use `filter()` followed by `findFirst()`.

```java
Optional<Employee> result = employees.stream()
        .filter(e -> e.getSalary() > 50000)
        .findFirst();

result.ifPresent(System.out::println);
```

**Interview explanation:**
- `filter()` retains employees whose salary is greater than ₹50,000.
- `findFirst()` returns the first matching employee in encounter order.
- `Optional` safely represents the possibility that no employee matches.

**Important trap:** `findFirst()` does not mean the employee with the lowest salary above ₹50,000. It means the first matching employee in the stream's encounter order.

**Follow-up:** How would you find the employee with the lowest salary above ₹50,000?

```java
Optional<Employee> result = employees.stream()
        .filter(e -> e.getSalary() > 50000)
        .min(Comparator.comparingInt(Employee::getSalary));
```

---

## Q7. Find the top two highest-paid employees.

**Answer:** Sort employees by salary in descending order and limit the result to two.

```java
List<Employee> result = employees.stream()
        .sorted(Comparator.comparingInt(Employee::getSalary)
                .reversed())
        .limit(2)
        .collect(Collectors.toList());
```

**Interview explanation:** `comparingInt()` compares salaries, `reversed()` changes the ordering to descending, and `limit(2)` returns the first two employees in that order.

**Interview trap:** Do not use `Comparator.reverseOrder()` directly for `Employee` unless the class implements `Comparable<Employee>` or an appropriate natural ordering is otherwise defined.

**Follow-up:** What is the time complexity?

Sorting generally takes O(n log n) time. If only the top two elements are required from a very large dataset, a bounded priority queue can find them in O(n log 2), which is O(n), though the implementation is more involved.

---

## Q8. Sort employees by salary and then by name.

**Answer:** Use `thenComparing()` for secondary sorting.

```java
List<Employee> result = employees.stream()
        .sorted(
            Comparator.comparingInt(Employee::getSalary)
                    .thenComparing(Employee::getName)
        )
        .collect(Collectors.toList());
```

**Sorting behavior:**
1. Salary is sorted in ascending order.
2. If salaries are equal, names are sorted alphabetically.

**Follow-up:** How would you sort by salary descending and name ascending?

```java
List<Employee> result = employees.stream()
        .sorted(
            Comparator.comparingInt(Employee::getSalary)
                    .reversed()
                    .thenComparing(Employee::getName)
        )
        .collect(Collectors.toList());
```

**Interview trap:** Avoid implementing comparators with arbitrary `1` and `-1` return values. A comparator must follow its contract, including returning zero when the compared values are equal. Prefer comparator factory methods whenever possible.

---

# PART 3: Grouping, Partitioning and Collectors

## Q9. Divide numbers into even and odd groups.

**Answer:** Use `Collectors.partitioningBy()`.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

Map<Boolean, List<Integer>> result = numbers.stream()
        .collect(Collectors.partitioningBy(n -> n % 2 == 0));

System.out.println(result);
```

**Output:**
```text
{false=[1, 3, 5], true=[2, 4, 6]}
```

**Interview explanation:** `partitioningBy()` divides elements into two groups based on a predicate. The key `true` contains even numbers, and `false` contains odd numbers.

**Follow-up:** What is the difference between `partitioningBy()` and `groupingBy()`?

- `partitioningBy()` produces two Boolean groups based on a condition.
- `groupingBy()` can produce multiple groups based on a classification key.

---

## Q10. Count the frequency of each element.

**Answer:** Use `groupingBy()` with `counting()`.

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4);

Map<Integer, Long> result = numbers.stream()
        .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
        ));

System.out.println(result);
```

**Output:**
```text
{1=1, 2=2, 3=3, 4=1}
```

**Interview explanation:**
- `Function.identity()` uses the element itself as the map key.
- `groupingBy()` groups identical elements.
- `counting()` counts the elements in each group.

**Interview trap:** `Collectors.counting()` returns a `Long`, not an `Integer`.

---

## Q11. Count employees in each department.

**Answer:** Group employees by department and count each group.

```java
Map<String, Long> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.counting()
        ));
```

**Example output:**
```text
{IT=3, HR=2, Finance=2}
```

**Follow-up:** Why is the return type `Map<String, Long>`?

Department is a `String`, and `Collectors.counting()` produces a `Long`.

**Interview trap:** `groupingBy()` returns a map, not a list. Do not assume its iteration order is guaranteed when using the default collector.

---

## Q12. Find the total transaction amount per category.

Assume each transaction has `getCategory()` and `getAmount()` methods.

**Answer:** Use `groupingBy()` with `summingInt()`.

```java
Map<String, Integer> result = transactions.stream()
        .collect(Collectors.groupingBy(
                Transaction::getCategory,
                Collectors.summingInt(Transaction::getAmount)
        ));
```

**Example output:**
```text
{Travel=1000, Utilities=700}
```

**Interview explanation:** Transactions are grouped by category, and the amount of each transaction is added to that category's total.

**Follow-up:** What if amounts are stored as `long`?

Use `Collectors.summingLong()` and return `Map<String, Long>`.

---

## Q13. Find the average salary in each department.

**Answer:** Use `groupingBy()` with `averagingInt()`.

```java
Map<String, Double> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.averagingInt(Employee::getSalary)
        ));
```

**Interview explanation:** Employees are grouped by department, and the average salary is calculated separately for each group.

**Interview trap:** The result is `Double`, even though salary is an integer, because an average may contain a fractional part.

---

## Q14. Find the highest-paid employee in each department.

**Answer:** Use `groupingBy()` with `maxBy()`.

```java
Map<String, Optional<Employee>> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.maxBy(
                        Comparator.comparingInt(Employee::getSalary)
                )
        ));
```

**Interview explanation:** Employees are grouped by department, and the employee with the maximum salary is selected within each group.

**Follow-up:** Why is the map value an `Optional<Employee>`?

`maxBy()` returns an `Optional`. With this exact `groupingBy()` operation, each non-empty group produces a present optional. A different collector or a downstream operation may be used to obtain a different result type.

**Alternative:** If you want the maximum salary amount rather than the employee object:

```java
Map<String, Integer> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.collectingAndThen(
                        Collectors.maxBy(
                                Comparator.comparingInt(Employee::getSalary)
                        ),
                        optional -> optional.get().getSalary()
                )
        ));
```

Use the alternative only when every department group is non-empty, as is the case for groups produced from an ordinary non-empty employee stream. For general-purpose code, handle the optional explicitly.

---

## Q15. Find the total salary per department.

**Answer:** Use `groupingBy()` with `summingInt()`.

```java
Map<String, Integer> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::getDepartment,
                Collectors.summingInt(Employee::getSalary)
        ));
```

**Interview explanation:** This groups employees by department and calculates the combined salary for each department.

**Follow-up:** What is the difference between `summingInt()` and `averagingInt()`?

- `summingInt()` calculates the total.
- `averagingInt()` calculates the average and returns a `Double`.

---

## Q16. Convert employee names into a comma-separated string.

**Answer:** Use `map()` to extract names and `Collectors.joining()` to combine them.

```java
String result = employees.stream()
        .map(Employee::getName)
        .collect(Collectors.joining(", "));

System.out.println(result);
```

**Example output:**
```text
Amit, Rahul, Priya
```

**Follow-up:** How can you add a prefix and suffix?

```java
String result = employees.stream()
        .map(Employee::getName)
        .collect(Collectors.joining(", ", "[", "]"));
```

**Output:**
```text
[Amit, Rahul, Priya]
```

---

# PART 4: FlatMap, Common Elements and Reduce

## Q17. Flatten a list of lists and remove duplicates.

**Answer:** Use `flatMap()` to flatten the nested lists and `distinct()` to remove duplicates.

```java
List<List<Integer>> nested = Arrays.asList(
        Arrays.asList(1, 2, 3),
        Arrays.asList(3, 4, 5),
        Arrays.asList(5, 6)
);

List<Integer> result = nested.stream()
        .flatMap(List::stream)
        .distinct()
        .collect(Collectors.toList());

System.out.println(result);
```

**Output:**
```text
[1, 2, 3, 4, 5, 6]
```

**Interview explanation:** Each inner list is converted into a stream. `flatMap()` combines these into one stream, after which `distinct()` removes duplicates.

**Follow-up:** What is the difference between `map()` and `flatMap()`?

`map()` transforms each element into one result. `flatMap()` transforms each element into a stream or other supported nested structure and flattens the results into one stream.

---

## Q18. Find common elements between two lists.

**Answer:** Convert one list into a `Set` and filter the other list using `contains()`.

```java
List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> list2 = Arrays.asList(3, 4, 5, 6, 7);

Set<Integer> set = new HashSet<>(list1);

List<Integer> result = list2.stream()
        .filter(set::contains)
        .distinct()
        .collect(Collectors.toList());

System.out.println(result);
```

**Output:**
```text
[3, 4, 5]
```

**Interview explanation:** A `HashSet` provides average O(1) membership checks. This avoids repeatedly searching the first list.

**Complexity:**
- Using `List.contains()` repeatedly: O(n × m) in the general case.
- Using a `HashSet`: average O(n + m), assuming well-distributed hashes.

**Interview trap:** `HashSet` removes duplicates from the lookup structure, but it does not automatically remove duplicate matches from the streamed list. Here, `distinct()` ensures unique results.

---

## Q19. Calculate the total salary using `reduce()`.

**Answer:** Use `map()` to extract salaries and `reduce()` to combine them.

```java
int totalSalary = employees.stream()
        .map(Employee::getSalary)
        .reduce(0, Integer::sum);

System.out.println(totalSalary);
```

**Interview explanation:**
- `map()` extracts the salary from every employee.
- `reduce()` combines the salary values into one result.
- `0` is the identity value for addition.
- `Integer::sum` adds two values.

**Better alternative for numeric streams:**

```java
int totalSalary = employees.stream()
        .mapToInt(Employee::getSalary)
        .sum();
```

**Follow-up:** Why does `reduce()` take an identity value?

The identity is the starting value and must be neutral for the operation. For addition, it is zero; for multiplication, it is one.

---

# PART 5: Parallel Streams and Concurrency Traps

## Q20. Print employee names using a parallel stream.

**Answer:** Use `parallelStream()` when parallel processing is suitable.

```java
employees.parallelStream()
        .map(Employee::getName)
        .forEach(System.out::println);
```

**Interview explanation:** A parallel stream can process elements using multiple threads, which may improve performance for suitable workloads.

**Important interview trap:** A parallel stream is not automatically faster. For a small list or a simple operation, parallel-processing overhead may make it slower.

Also, `forEach()` does not guarantee encounter order for a parallel stream. Use `forEachOrdered()` when preserving encounter order is necessary.

---

## Q21. Identify the bug in parallel processing with an ArrayList.

**Problematic code:**

```java
List<Integer> result = new ArrayList<>();

IntStream.rangeClosed(1, 1000)
        .parallel()
        .forEach(result::add);
```

**What is wrong?**

`ArrayList` is not thread-safe. Multiple threads may modify the same list concurrently, causing data races, lost updates or inconsistent results.

**Correct alternative 1: Collect the results safely.**

```java
List<Integer> result = IntStream.rangeClosed(1, 1000)
        .parallel()
        .boxed()
        .collect(Collectors.toList());
```

**Correct alternative 2: Use a concurrent collection when shared concurrent mutation is genuinely needed.**

```java
List<Integer> result =
        new java.util.concurrent.CopyOnWriteArrayList<>();

IntStream.rangeClosed(1, 1000)
        .parallel()
        .forEach(result::add);
```

For large numbers of writes, `CopyOnWriteArrayList` can be inefficient because writes copy its underlying array. Prefer stream collection when possible.

**Interview explanation:** Do not mutate a shared, non-thread-safe collection from parallel stream operations. Prefer a collector or an appropriate concurrent data structure.

---

## Q22. Calculate total salary using a parallel stream.

**Answer:** A parallel stream can be used with a suitable reduction operation.

```java
int totalSalary = employees.parallelStream()
        .map(Employee::getSalary)
        .reduce(0, Integer::sum);
```

**Recommended numeric version:**

```java
int totalSalary = employees.parallelStream()
        .mapToInt(Employee::getSalary)
        .sum();
```

**Interview explanation:** The stream can calculate partial sums in parallel and combine the results. Addition is suitable because it is associative, assuming no integer overflow.

**Important trap:** Do not assume `reduce()` makes every operation thread-safe. The reduction must satisfy the required identity and associativity rules. Avoid shared mutable state in accumulator operations.

**Follow-up:** When should you use parallel streams?

Use them when the workload is suitable for parallel processing, the data size justifies the overhead, operations can be performed independently, and performance testing shows a benefit.

---

# PART 6: Five Additional Must-Know Questions

These are useful follow-ups for a Java Developer technical manager round and complement the video.

## Q23. What is the difference between intermediate and terminal operations?

**Answer:**

Intermediate operations return another stream and are generally lazy. Terminal operations trigger processing and produce a result or side effect.

**Intermediate operations:**
- `filter()`
- `map()`
- `flatMap()`
- `distinct()`
- `sorted()`
- `limit()`
- `skip()`

**Terminal operations:**
- `collect()`
- `forEach()`
- `reduce()`
- `count()`
- `min()`
- `max()`
- `findFirst()`

**Example:**

```java
long count = Arrays.asList(1, 2, 3, 4, 5)
        .stream()
        .filter(n -> n > 2)
        .count();
```

Here, `filter()` is an intermediate operation and `count()` is a terminal operation.

**Interview trap:** Streams are lazy. Intermediate operations are not processed until a terminal operation is invoked.

---

## Q24. What is the difference between `findFirst()` and `findAny()`?

**Answer:**

- `findFirst()` returns the first element according to encounter order when that order exists.
- `findAny()` returns any element and is useful when the specific element does not matter, particularly with parallel streams.

Both return an `Optional`.

**Example:**

```java
Optional<Integer> first = numbers.stream()
        .filter(n -> n > 10)
        .findFirst();

Optional<Integer> any = numbers.parallelStream()
        .filter(n -> n > 10)
        .findAny();
```

**Interview trap:** Do not expect `findAny()` to return the same element on every execution.

---

## Q25. Explain `anyMatch()`, `allMatch()` and `noneMatch()`.

**Answer:** All three evaluate a predicate and return a Boolean.

```java
List<Integer> numbers = Arrays.asList(2, 4, 6, 8);

boolean anyOdd = numbers.stream()
        .anyMatch(n -> n % 2 != 0);

boolean allEven = numbers.stream()
        .allMatch(n -> n % 2 == 0);

boolean noneNegative = numbers.stream()
        .noneMatch(n -> n < 0);
```

**Results:**
- `anyOdd` → `false`
- `allEven` → `true`
- `noneNegative` → `true`

**Interview explanation:**
- `anyMatch()` is true if at least one element matches.
- `allMatch()` is true if every element matches.
- `noneMatch()` is true if no element matches.

**Interview trap:** These operations can short-circuit. They do not necessarily evaluate every element.

---

# Final Quick Revision

| Method | Purpose |
|---|---|
| `filter()` | Select elements matching a condition |
| `map()` | Transform each element |
| `flatMap()` | Flatten nested streams |
| `distinct()` | Remove duplicates |
| `sorted()` | Sort elements |
| `skip()` | Skip the first N elements |
| `limit()` | Return at most N elements |
| `findFirst()` | Return the first matching element |
| `max()` / `min()` | Find maximum or minimum |
| `groupingBy()` | Group elements by a key |
| `partitioningBy()` | Split elements into two Boolean groups |
| `counting()` | Count elements in a group |
| `summingInt()` | Sum integer values |
| `averagingInt()` | Calculate average |
| `maxBy()` | Find the maximum element using a comparator |
| `joining()` | Combine strings with a delimiter |
| `reduce()` | Combine values into a single result |
| `collect()` | Accumulate stream results |
| `anyMatch()` | Check whether any element matches |
| `allMatch()` | Check whether all elements match |
| `noneMatch()` | Check whether no element matches |
| `parallelStream()` | Process a stream in parallel when suitable |

## What to prioritize for the interview

1. Practice Q4, Q7 and Q8: second-highest number, top-paid employees, and custom sorting.
2. Practice Q10–Q15: `groupingBy()` with `counting()`, `summingInt()`, `averagingInt()` and `maxBy()`.
3. Practice Q17–Q19: `flatMap()`, common elements, and `reduce()`.
4. Understand Q20–Q22 carefully: parallel streams, thread safety, and reduction correctness.
5. Be ready to explain `map()` vs `filter()`, `map()` vs `flatMap()`, intermediate vs terminal operations, and `findFirst()` vs `findAny()` without looking at code.

**Interview strategy:** First explain the approach in one or two sentences, then write the code, state the output or return type, and mention one edge case or performance consideration. That demonstrates practical understanding rather than memorized syntax.