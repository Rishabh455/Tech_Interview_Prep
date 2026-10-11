# Java Stream API Interview Question Bank — 41 Questions

**Target:** Java 8+ interviews, beginner through hard  
**Companion code:** `JavaStreamInterviewQuestions.java` — one self-contained class with all 41 solutions, sample data, model classes, and a `main()` method.

## Before you start

The transcript's announced section counts add to 42, but it contains **41 distinct question topics**. This bank follows the 41 actual topics: 10 super-easy, 10 easy, 6 intermediate, 5 intermediate, 5 hard, and 5 hard.

The code is compatible with **Java 8**: it uses `Collectors.toList()` rather than Java 16+'s `Stream.toList()`. Compile and run it with:

```bash
javac JavaStreamInterviewQuestions.java
java JavaStreamInterviewQuestions
```

---

## Level 1 — Super Easy (Q1–Q10)

### Q1. What are different ways to create a Java Stream?
**Tests:** `Collection.stream()`, `Arrays.stream()`, `Stream.of()`, `Stream.generate()`, and `Stream.iterate()`.

```java
List<String> names = Arrays.asList("Alice", "Bob");
Stream<String> fromList = names.stream();
Stream<String> fromArray = Arrays.stream(new String[]{"red", "blue"});
Stream<Integer> values = Stream.of(1, 2, 3);
Stream<Double> randoms = Stream.generate(Math::random).limit(5);
Stream<Integer> sequence = Stream.iterate(0, n -> n + 1).limit(5);
```

**Interview note:** `generate()` and `iterate()` may be unbounded; use a short-circuiting operation such as `limit()` when a finite result is required.

### Q2. Filter even numbers from a list.
**Tests:** `filter`, predicate, `collect`.

```java
List<Integer> result = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

### Q3. Convert each number into its square.
**Tests:** `map` for one-to-one transformation.

```java
List<Integer> result = numbers.stream()
    .map(n -> n * n)
    .collect(Collectors.toList());
```

### Q4. Square only the even numbers.
**Tests:** chaining `filter` and `map`.

```java
List<Integer> result = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .collect(Collectors.toList());
```

### Q5. Find the first number greater than 10.
**Tests:** `filter`, `findFirst`, `Optional`.

```java
Optional<Integer> result = numbers.stream()
    .filter(n -> n > 10)
    .findFirst();
```

**Important distinction:** this returns the first qualifying value in encounter order, not necessarily the smallest qualifying value. To find the **smallest** number above 10, use `.filter(n -> n > 10).min(Integer::compareTo)`.

### Q6. Count numbers greater than 5.
**Tests:** `filter`, `count` (`count()` returns `long`).

```java
long count = numbers.stream().filter(n -> n > 5).count();
```

### Q7. Find the sum of all numbers using `reduce`.
**Tests:** accumulator/identity and reduction.

```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

### Q8. Find the sum of even numbers.
**Tests:** filtering followed by a primitive-stream terminal operation.

```java
int sum = numbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
```

### Q9. Find the maximum number in a list.
**Tests:** `max`, `Optional` for a possibly empty stream.

```java
Optional<Integer> max = numbers.stream().max(Integer::compareTo);
```

### Q10. Find the sum of squares of even numbers.
**Tests:** combining `filter`, `mapToInt`, and `sum`.

```java
int result = numbers.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(n -> n * n)
    .sum();
```

---

## Level 2 — Easy (Q11–Q20)

### Q11. Remove duplicate elements while preserving encounter order.
**Tests:** `distinct`, equality semantics.

```java
List<Integer> result = numbers.stream().distinct().collect(Collectors.toList());
```

**Interview note:** `distinct()` uses `equals()` to identify duplicates. For an ordered list stream, it preserves encounter order; collecting into a `HashSet` does not guarantee list order.

### Q12. Find the average of a list of integers.
**Tests:** `mapToInt`, `average`, `OptionalDouble`.

```java
OptionalDouble average = numbers.stream().mapToInt(Integer::intValue).average();
```

An empty input returns `OptionalDouble.empty()`. Handle that with `orElse(0.0)` only if zero is the intended fallback.

### Q13. Sort integers in ascending and descending order.
**Tests:** `sorted`, `Comparator.reverseOrder()`.

```java
List<Integer> asc = numbers.stream().sorted().collect(Collectors.toList());
List<Integer> desc = numbers.stream()
    .sorted(Comparator.reverseOrder()).collect(Collectors.toList());
```

### Q14. Count strings that start with a specific letter.
**Tests:** string predicates and `count`.

```java
long count = words.stream().filter(s -> s.startsWith("a")).count();
```

This example is case-sensitive. Normalize case first if the requirement is case-insensitive.

### Q15. Join strings into one comma-separated string.
**Tests:** `Collectors.joining`.

```java
String result = words.stream().collect(Collectors.joining(","));
```

Use `Collectors.joining(", ")` if a space after each comma is desired.

### Q16. Check whether all numbers are positive.
**Tests:** `allMatch` and short-circuiting.

```java
boolean allPositive = numbers.stream().allMatch(n -> n > 0);
```

Positive means strictly greater than zero. `allMatch` returns `true` for an empty stream.

### Q17. Check whether any number is divisible by 3.
**Tests:** `anyMatch` and short-circuiting.

```java
boolean exists = numbers.stream().anyMatch(n -> n % 3 == 0);
```

### Q18. Flatten a list of lists into one list.
**Tests:** `flatMap`.

```java
List<Integer> flattened = nestedLists.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
```

**Remember:** `map` would produce a stream of inner lists; `flatMap` turns the nested elements into one stream.

### Q19. Remove empty strings from a list.
**Tests:** `filter`, null handling.

```java
List<String> result = words.stream()
    .filter(Objects::nonNull)
    .filter(s -> !s.isEmpty())
    .collect(Collectors.toList());
```

This removes `""`, but not a whitespace-only string like `"   "`. To remove both, test `!s.trim().isEmpty()`.

### Q20. Find the second-highest number.
**Tests:** `distinct`, reverse sorting, `skip`, `findFirst`.

```java
Optional<Integer> secondHighest = numbers.stream()
    .distinct()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst();
```

This finds the **second-highest distinct value**. If the interviewer wants the second item after sorting, including duplicates, remove `distinct()`.

---

## Level 3 — Intermediate (Q21–Q26)

For Q21, Q22, and Q26–Q29, the companion source defines simple `Employee`/`Person` model classes so the examples compile as-is.

### Q21. Sort employees by salary in ascending and descending order.
**Tests:** `Comparator.comparingDouble`, custom-object sorting.

```java
List<Employee> asc = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary))
    .collect(Collectors.toList());
List<Employee> desc = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
    .collect(Collectors.toList());
```

### Q22. Calculate the average age of a list of people.
**Tests:** extracting a numeric field with `mapToInt`.

```java
OptionalDouble averageAge = people.stream().mapToInt(Person::getAge).average();
```

### Q23. Split integers into even and odd lists.
**Tests:** `Collectors.partitioningBy`.

```java
Map<Boolean, List<Integer>> result = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// true -> even numbers; false -> odd numbers
```

**Distinction:** `partitioningBy` always partitions by a boolean predicate; `groupingBy` can create many groups.

### Q24. Group words by their length.
**Tests:** `groupingBy` with a derived key.

```java
Map<Integer, List<String>> grouped = words.stream()
    .collect(Collectors.groupingBy(String::length));
```

### Q25. Count the occurrences of every element in a list.
**Tests:** `groupingBy`, `Function.identity()`, `counting`.

```java
Map<String, Long> counts = words.stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

`counting()` produces `Long` values, not `Integer` values.

### Q26. Calculate the average salary in each department.
**Tests:** downstream collectors: `groupingBy` + `averagingDouble`.

```java
Map<String, Double> averageByDepartment = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)));
```

---

## Level 4 — Intermediate (Q27–Q31)

### Q27. Find the highest-paid employee in every department.
**Tests:** `groupingBy` + `maxBy`.

```java
Map<String, Optional<Employee>> result = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment,
        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));
```

The downstream `maxBy` collector returns an `Optional<Employee>` for each department. Avoid assuming every group is non-empty if the design changes to allow empty groups.

### Q28. Find departments that have more than two employees.
**Tests:** aggregate first, then filter the map entries.

```java
Map<String, Long> counts = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
List<String> departments = counts.entrySet().stream()
    .filter(e -> e.getValue() > 2)
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());
```

This counts employee records. If records can repeat the same employee, deduplicate by employee ID before counting.

### Q29. Find the department with the highest average salary.
**Tests:** composing aggregation results with `max`.

```java
Optional<Map.Entry<String, Double>> result = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)))
    .entrySet().stream()
    .max(Map.Entry.comparingByValue());
```

### Q30. Find the most frequent character in a string.
**Tests:** `chars`, grouping/counting, max selection.

```java
Map<Character, Long> counts = text.chars().mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
Optional<Map.Entry<Character, Long>> mostFrequent = counts.entrySet().stream()
    .max(Map.Entry.comparingByValue());
```

If several characters tie, define a tie-break rule (for example, first encountered) rather than relying on a `HashMap`'s iteration order. `chars()` operates on UTF-16 code units; use `codePoints()` for full Unicode code-point handling.

### Q31. Find the first non-repeating character in a string.
**Tests:** counting while retaining encounter order.

```java
Map<Character, Long> counts = text.chars().mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
Optional<Character> firstUnique = counts.entrySet().stream()
    .filter(e -> e.getValue() == 1L)
    .map(Map.Entry::getKey)
    .findFirst();
```

The `LinkedHashMap` matters: it preserves the first-seen order of characters, unlike `HashMap`.

---

## Level 5 — Hard (Q32–Q36)

### Q32. Find the most common first letter among employee names.
**Tests:** map objects to derived values, then group/count.

```java
Map<Character, Long> counts = employees.stream()
    .map(Employee::getName)
    .filter(Objects::nonNull).filter(name -> !name.isEmpty())
    .map(name -> Character.toLowerCase(name.charAt(0)))
    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
Optional<Map.Entry<Character, Long>> result = counts.entrySet().stream()
    .max(Map.Entry.comparingByValue());
```

The companion solution makes the comparison case-insensitive and uses a stable, explicit tie policy. If the expected result is just the letter, map the winning entry to its key.

### Q33. Calculate the average of each three-element sliding window.
**Tests:** index ranges, `subList`, nested stream operations, `average`.

```java
int windowSize = 3;
List<Double> averages = IntStream.range(0, numbers.size() - windowSize + 1)
    .mapToObj(i -> numbers.subList(i, i + windowSize))
    .map(window -> window.stream().mapToInt(Integer::intValue).average().orElse(Double.NaN))
    .collect(Collectors.toList());
```

Validate that `windowSize > 0`. If the list is shorter than the window, the result is an empty list. The companion implementation validates the window size.

### Q34. Find the longest word in a sentence.
**Tests:** text normalization, split, `max` with a comparator.

```java
String cleaned = sentence.replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
Optional<String> longest = cleaned.isEmpty() ? Optional.empty() :
    Arrays.stream(cleaned.split("\\s+"))
        .max(Comparator.comparingInt(String::length));
```

Replacing punctuation with spaces avoids accidentally joining two words together. If there is a tie, define whether the first or last longest word should be returned.

### Q35. Find the top three most frequent words in a paragraph.
**Tests:** tokenization, frequency maps, sorting, tie handling.

```java
String cleaned = paragraph.toLowerCase(Locale.ROOT)
    .replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
Map<String, Long> counts = Arrays.stream(cleaned.split("\\s+"))
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
List<Map.Entry<String, Long>> topThree = counts.entrySet().stream()
    .sorted((a, b) -> {
        int byCount = Long.compare(b.getValue(), a.getValue());
        return byCount != 0 ? byCount : a.getKey().compareTo(b.getKey());
    })
    .limit(3)
    .collect(Collectors.toList());
```

**Clarify the requirement:** `limit(3)` returns at most three words. The companion method instead returns **all words belonging to the top three distinct frequency levels**, including ties, matching the transcript's tie-handling extension. These are different interpretations.

### Q36. Reverse every word in a sentence using streams.
**Tests:** splitting into words, mapping, joining; optional alternative with `reduce`.

```java
String result = Arrays.stream(sentence.trim().split("\\s+"))
    .map(word -> new StringBuilder(word).reverse().toString())
    .collect(Collectors.joining(" "));
```

This reverses each token, including punctuation attached to it. If asked not to use `StringBuilder.reverse()`, build each reversed word with a character loop or a `reduce` operation.

---

## Level 6 — Hard (Q37–Q41)

### Q37. From transactions, find the day with the highest total spend.
**Tests:** `groupingBy` + `summingDouble`, followed by maximum selection.

```java
Map<LocalDate, Double> totals = transactions.stream()
    .collect(Collectors.groupingBy(Transaction::getDate,
        Collectors.summingDouble(Transaction::getAmount)));
Optional<Map.Entry<LocalDate, Double>> result = totals.entrySet().stream()
    .max(Map.Entry.comparingByValue());
```

For financial systems, prefer `BigDecimal` for monetary amounts instead of binary floating-point `double`.

### Q38. Group employees into low, medium, and high salary ranges.
**Tests:** `groupingBy` with a derived category not stored directly on the employee.

```java
Map<SalaryRange, List<Employee>> grouped = employees.stream()
    .collect(Collectors.groupingBy(e -> salaryRange(e.getSalary())));
```

Define the salary boundaries explicitly. In the companion code: LOW is `< 50,000`, MEDIUM is `>= 50,000` and `< 80,000`, HIGH is `>= 80,000`. The thresholds are example business rules, not universal values.

### Q39. Group characters as uppercase, lowercase, or other.
**Tests:** classification into a derived enum/category.

```java
Map<CharacterCategory, List<Character>> grouped = text.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(JavaStreamInterviewQuestions::characterCategory));
```

`characterCategory` returns UPPERCASE for `Character.isUpperCase(ch)`, LOWERCASE for `Character.isLowerCase(ch)`, and OTHER otherwise. This classifies digits, whitespace, punctuation, and symbols as OTHER.

### Q40. Find employees who have worked in at least three distinct departments.
**Tests:** grouping + downstream `mapping` to a set + filtering by set size.

```java
Map<String, Set<String>> departmentsByEmployee = workRecords.stream()
    .collect(Collectors.groupingBy(WorkRecord::getEmployeeName,
        Collectors.mapping(WorkRecord::getDepartment, Collectors.toSet())));
List<String> result = departmentsByEmployee.entrySet().stream()
    .filter(e -> e.getValue().size() >= 3)
    .map(Map.Entry::getKey)
    .collect(Collectors.toList());
```

Counting **distinct departments** prevents duplicate work records from inflating the count. In real applications, group by a stable employee ID rather than a name if names are not unique.

### Q41. Count every bigram (pair of adjacent words) in a paragraph.
**Tests:** indexed traversal with `IntStream`, pair creation, frequency counting.

```java
String cleaned = paragraph.toLowerCase(Locale.ROOT)
    .replaceAll("[^\\p{L}\\p{N}\\s]", " ").trim();
String[] words = cleaned.isEmpty() ? new String[0] : cleaned.split("\\s+");
Map<String, Long> bigrams = IntStream.range(0, words.length - 1)
    .mapToObj(i -> words[i] + " " + words[i + 1])
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
```

For `"Java is great and Java is fun"`, the adjacent pairs include `"java is"`, `"is great"`, `"great and"`, `"and java"`, `"is fun"`. Punctuation is removed and case normalized first.

---

## High-value interview reminders

1. **Intermediate vs terminal:** `filter`, `map`, `flatMap`, `distinct`, `sorted`, and `peek` are intermediate operations. `collect`, `count`, `reduce`, `min`, `max`, `findFirst`, `anyMatch`, and `average`-style primitive-stream operations are terminal operations. Intermediate operations are lazy until a terminal operation runs.
2. **Streams are single-use:** do not reuse a stream after a terminal operation; create a new stream from the source when another pipeline is needed.
3. **`map` vs `flatMap`:** `map` transforms each element; `flatMap` transforms each element into a stream and flattens the results.
4. **`groupingBy` vs `partitioningBy`:** grouping can produce many keys; partitioning produces the boolean groups `true` and `false`.
5. **Primitive streams:** use `mapToInt`, `mapToLong`, or `mapToDouble` for numeric operations such as `sum`, `average`, `min`, and `max`.
6. **Empty results:** operations such as `findFirst`, `max`, and `average` may return an `Optional` type. Decide how to handle the empty case rather than calling `get()` blindly.
7. **Encounter order and ties:** `HashMap` does not promise insertion order. Use `LinkedHashMap` or an explicit tie-breaker when order affects the answer.
8. **Do not overuse streams:** for index-based windows or complicated stateful logic, a loop may be clearer. Choose the solution that is correct, readable, and maintainable.
