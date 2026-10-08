# Java Stream API — Interview Notes

## 1. What is Stream API?

Stream API was introduced in **Java 8**.

It is used to **process collections of data** in a simple and readable way.

For example:

```java
List<Integer> numbers =
        Arrays.asList(1, 2, 3, 4, 5);
```

Instead of manually using a loop:

```java
for (Integer n : numbers) {
    if (n % 2 == 0) {
        System.out.println(n);
    }
}
```

We can use Stream API:

```java
numbers.stream()
       .filter(n -> n % 2 == 0)
       .forEach(System.out::println);
```

Output:

```text
2
4
```

---

# 2. What is a Stream?

A Stream is a **sequence of elements** that allows us to perform operations on data.

Important:

> A Stream does **not store data**. It processes data from a source such as a List, Set, or Array.

Think:

```text
Collection
    ↓
  Stream
    ↓
Process data
    ↓
Result
```

---

# 3. Stream Pipeline

A Stream normally has three parts:

```text
Source
   ↓
Intermediate Operations
   ↓
Terminal Operation
```

Example:

```java
numbers.stream()
       .filter(n -> n % 2 == 0)
       .map(n -> n * 2)
       .forEach(System.out::println);
```

Here:

```text
numbers       → Source
stream()      → Creates Stream
filter()      → Intermediate
map()         → Intermediate
forEach()     → Terminal
```

---

# 4. How to Create a Stream

## From List

```java
List<Integer> numbers =
        Arrays.asList(1, 2, 3, 4);

Stream<Integer> stream =
        numbers.stream();
```

## From Set

```java
Set<String> names =
        new HashSet<>();

names.stream();
```

## From Array

```java
int[] numbers = {1, 2, 3, 4};

Ar
```
