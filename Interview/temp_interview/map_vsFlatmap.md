## Map vs FlatMap — 2-Minute Interview Revision

### 1. `map()` → Transformation

**One input → one output**

Example: `Employee → Employee Name`

```java
List<String> names = employees.stream()
        .map(Employee::getName)
        .toList();
```

If:

```text
Employee1 → Rishabh
Employee2 → Amit
Employee3 → Rahul
```

Result:

```text
[Rishabh, Amit, Rahul]
```

So remember:

```text
map()
Employee → String
   1        1
```

---

### 2. `flatMap()` → Transformation + Flattening

Used when **one input produces multiple values**, especially nested collections.

Suppose:

```text
Employee1 → [Mumbai, Pune]
Employee2 → [Delhi, Mumbai]
Employee3 → [Pune, Bangalore]
```

Using `map()`:

```java
List<List<String>> cities = employees.stream()
        .map(Employee::getCities)
        .toList();
```

Result:

```text
[[Mumbai, Pune], [Delhi, Mumbai], [Pune, Bangalore]]
```

It's still a **nested list**.

Using `flatMap()`:

```java
List<String> cities = employees.stream()
        .flatMap(e -> e.getCities().stream())
        .toList();
```

Result:

```text
[Mumbai, Pune, Delhi, Mumbai, Pune, Bangalore]
```

It **flattens** the nested lists into one stream.

### The interview line to memorize:

> **"`map()` is used for one-to-one transformation, whereas `flatMap()` is used for transformation plus flattening when each element can produce multiple values."**

### Mental shortcut

```text
map:
Employee → Name
   1        1

flatMap:
Employee → [City1, City2]
             ↓
       City1, City2
             ↓
       ONE FLAT STREAM
```

**Important:** `flatMap()` does **not** remove duplicates. Use `.distinct()` or collect into a `Set` if you need unique values.

```java
Set<String> cities = employees.stream()
        .flatMap(e -> e.getCities().stream())
        .collect(Collectors.toSet());
```

**Nomura one-liner:**  
**map = transformation | flatMap = transformation + flattening.**